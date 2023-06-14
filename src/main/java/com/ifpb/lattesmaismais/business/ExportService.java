package com.ifpb.lattesmaismais.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;
import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import com.ifpb.lattesmaismais.presentation.exception.HashException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ExportService {
	
	@Autowired
	private HashService hashService;
	
	@Autowired
	private FileUploadService uploadService;
	
	private Font unicodeFont;

	private Curriculum curriculum;
	private Document doc;
	private Map<Integer, String> pdfListToInsert;
	private String userFolder;
	private String hashUserId;
	
	private static final String CURRICULUM_OUTPUT = "\\Curriculum.pdf";
	
	public String generatePdf(Curriculum curriculum, Integer userId) throws DocumentException, MalformedURLException, IOException, HashException, FileConversionException, DecryptionException {
		
		// fonte de caracteres Unicode
		BaseFont bf = BaseFont.createFont("fonts/Tahoma.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		unicodeFont = new Font(bf, 12);
		
		this.curriculum = curriculum;
		doc = new Document(PageSize.A4);
		pdfListToInsert = new HashMap<>();
		hashUserId = hashService.hashingSHA256(userId.toString());
		userFolder = System.getProperty("user.dir") + "\\src\\main\\resources\\server\\dr\\" + hashUserId;
		
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(userFolder + CURRICULUM_OUTPUT));
		doc.open();
		
		addProperties();
		addHeader();
		addEntries();
		
		doc.close();
		writer.close();
		
		// torna o arquivo criado temporário
		File file = new File(userFolder + CURRICULUM_OUTPUT);
		file.deleteOnExit();
		
		// monta o caminho absoluto do arquivo e seta o correto dependendo se o mesmo for mesclado com outro PDF
		String absoluteFilePath = userFolder + CURRICULUM_OUTPUT;
		if(!pdfListToInsert.isEmpty()) {
			// realiza merge do documento PDF a ser gerado e documento PDF de comprovação
			absoluteFilePath = copyPagesPdfToCurrentPdf();
		}
		// retorna apenas a parte de acesso complementar ao simulador Http-server (sufixo)
		return absoluteFilePath.substring(absoluteFilePath.indexOf("\\dr"));
	}

	private void addProperties() {
		doc.addCreationDate();
		doc.addCreator("Lattes++");
		doc.addAuthor(curriculum.getOwner().getName());
	    doc.addTitle("Curriculo_" + curriculum.getVersion());
	    doc.addSubject("Gerado com a aplicação Lattes++");
	}

	private void addHeader() throws DocumentException {
		addParagraph(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")), 0);
		addParagraph("Currículo Lattes++", 1);
		addParagraph(" ", 0);
		addParagraph(curriculum.getOwner().getName(), 0);
		addParagraph(curriculum.getVersion(), 0);
	}

	private void addEntries() throws DocumentException, MalformedURLException, IOException, FileConversionException, DecryptionException {
		addParagraph(" ", 0);
		addParagraph("--- Entradas identificadas ---", 0);
		addParagraph(" ", 0);
		
		String group = "";
		for(Entry entry : curriculum.getEntries()) {
			// apenas inclui entradas com comprovantes
			if(entry.getReceipts() != null && !entry.getReceipts().isEmpty()) {
				
				if(!entry.getGroup().equals(group)) {
					group = entry.getGroup();
					addParagraph(group, 0);
					addParagraph(" ", 0);
				}
				
				addParagraph(entry.getName(), 0);
				for(Receipt rec : entry.getReceipts()) {
					// se a comprovação não for digital, entra nesta primeira condição
					if(rec.getUrl() == null) {
						// o endereço retornado é algo no padrão "\\dr\\pastaUser\\pastaTemp\\file.ext"
						String folderAndFileName;
						// não havendo herança de arquivos na classe Comprovante, usa os dados do comprovante
						if(rec.getHeritage() == null) {
							folderAndFileName = uploadService.readFile(rec.getId() + rec.getExtension(), hashUserId);
						} else {
							folderAndFileName = uploadService.readFile(rec.getHeritage(), hashUserId);
						}
						
						// gera caminho absoluto do arquivo
						String absolutePathFile = userFolder.substring(0, userFolder.indexOf("\\dr")) + folderAndFileName;

						// se for PDF entra na primeira condição, se for imagem, na segunda.
						if(rec.getExtension().equals(".pdf")) {
							// usar quebra de página quando uma entrada pdf for identificada.
							// pega a página atual e o path do arquivo para assim poder colocar as páginas do pdf posteriormente.
							addParagraph("< PDF na próxima folha >", 0);
							// registra onde o arquivo PDF de comprovante deve ser inserido no PDF de exportação.
							// a contagem de páginas inicia em 0 ao que tudo indica
							pdfListToInsert.put(doc.getPageNumber() + 1, absolutePathFile);
							doc.newPage();
						
						} else {
							addImg(absolutePathFile);
						}
						
					} else {
						addParagraph("Comprovação digital: " + rec.getUrl(), 0);
					}
				}
			}
		}
	}

	private String copyPagesPdfToCurrentPdf() throws IOException, DocumentException {
		
		String outputPath = userFolder + "\\FinalCurriculum.pdf"; 
		
		// criar leitores de cada PDF a serem usados para gerar o terceiro
		PdfReader readerBase = new PdfReader(userFolder + CURRICULUM_OUTPUT);
		PdfReader readerAux;
		
		// cria o novo documento e estabelece sua saída
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
		document.open();
		
		// irá adicionar o conteúdo selecionado ao pdf de saída
		PdfContentByte cb = writer.getDirectContent();
		PdfImportedPage impPage;
		
		// mescla ambos os arquivos para gerar o novo pdf
		for(int i = 1; i <= readerBase.getNumberOfPages(); i++) {
			// adiciona nova página se não for a primeira
			if(i > 1) {
				document.newPage();
			}

			// adiciona página original base
			impPage = writer.getImportedPage(readerBase, i);
			cb.addTemplate(impPage, 0, 0);

			if(pdfListToInsert.containsKey(i)) {
				// adiciona todos as páginas do pdf de comprovante se contiver a key
				readerAux = new PdfReader(pdfListToInsert.get(i));
				for(int j = 1; j <= readerAux.getNumberOfPages(); j++) {
					// adiciona nova página para cada comprovante no PDF
					document.newPage();
					impPage = writer.getImportedPage(readerAux, j);
					cb.addTemplate(impPage, 0, 0);
				}
			}
		}
		
		document.close();
		writer.close();
		
		return outputPath;
	}

	private void addParagraph(String text, int alig) throws DocumentException {
		Paragraph p = new Paragraph(text, unicodeFont);
		p.setAlignment(alig);
		
		doc.add(p);
	}

	private void addImg(String path)
			throws MalformedURLException, IOException, DocumentException {

		Image img = Image.getInstance(path);
		// redicionamento de imagem
		float originalWidth = img.getScaledWidth();
		float originalHeight = img.getScaledHeight();
		// estabelece que a maior dimensão será de 400px e a outra proporcionalmente reduzida
		// da imagem original.
		float fixedValue = 400f;
		float larger = originalHeight > originalWidth ? originalHeight : originalWidth;
		
		float convertedWidth;
		float convertedHeight;
		if(larger == originalWidth) {
			convertedWidth = fixedValue;
			convertedHeight = originalHeight / (originalWidth / fixedValue);
		} else {
			convertedHeight = fixedValue;
			convertedWidth = originalWidth / (originalHeight / fixedValue);
		}
		img.scaleAbsolute(convertedWidth, convertedHeight);
		
		doc.add(img);
	}

}
