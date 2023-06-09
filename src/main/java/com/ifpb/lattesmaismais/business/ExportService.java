package com.ifpb.lattesmaismais.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;
import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.entity.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ExportService {
	
	@Autowired
	private HashService hashService;
	
	@Autowired
	private FileUploadService uploadService;

	private Curriculum curriculum;
	private Document doc;
	private Map<Integer, String> pdfListToInsert;
	
	public String generatePdf(Curriculum curriculum) throws DocumentException, MalformedURLException, IOException {
		
		this.curriculum = curriculum;
		doc = new Document(PageSize.A4);
		pdfListToInsert = new HashMap<>();
		//TODO caminho
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\Users\\Danilo\\Desktop\\Curriculum.pdf"));
		doc.open();
		
		addProperties();
		addHeader();
		addEntries();
		
		doc.close();
		writer.close();
		
		copyPagesPdfToCurrentPdf();

		return "Link de acesso ao PDF gerado";
	}

	private void addProperties() {
		doc.addCreationDate();
		doc.addCreator("Lattes++");
		doc.addAuthor(curriculum.getOwner().getName());
	    doc.addTitle("Curriculo_" + curriculum.getVersion());
	    doc.addSubject("Gerado com a aplicação Lattes++");
	}

	private void addHeader() throws DocumentException {
		addParagraph(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")), 2);
		addParagraph("Currículo Lattes++", 1);
		addParagraph(" ", 0);
		addParagraph(curriculum.getOwner().getName(), 0);
		addParagraph(curriculum.getVersion(), 0);
	}

	private void addEntries() throws DocumentException, MalformedURLException, IOException {
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
						String serverFolder = System.getProperty("user.dir") + "\\src\\main\\resources\\server";
						String filePathFileUp = "\\dr\\img-01.jpg";
						// se for PDF entra na primeira condição, se for imagem, na segunda.
						if(rec.getExtension().equals(".pdf")) {
							// usar quebra de página quando uma entrada pdf for identificada.
							// pega a página atual e o path do arquivo para assim poder colocar as
							// páginas do pdf posteriormente.
							addParagraph("PDF de comprovação carregado logo abaixo.", 0);
							pdfListToInsert.put(doc.getPageNumber(), serverFolder + filePathFileUp);
							doc.newPage();
						// imagens entram nessa condição
						} else {
							//TODO usar FileUpload para converter arquivo e gerar seu path
							// para adicionar abaixo
							addImg(serverFolder + filePathFileUp);
						}
						
					} else {
						addParagraph("Comprovação digital: " + rec.getUrl(), 0);
					}
				}
			}
		}
	}

	private void copyPagesPdfToCurrentPdf() {
		
	}

	public static void main(String[] args) {
		
		User user = new User();
		user.setName("Danilo de Sousa Costa");
		
		Curriculum curriculum = new Curriculum();
		curriculum.setOwner(user);
		curriculum.setVersion("V_10062023_114023");
		
		ExportService exp = new ExportService();
		try {
			exp.generatePdf(curriculum);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Terminado");
		}
		
	}

	private void addParagraph(String text, int alig) throws DocumentException {
		Paragraph p = new Paragraph(text);
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
