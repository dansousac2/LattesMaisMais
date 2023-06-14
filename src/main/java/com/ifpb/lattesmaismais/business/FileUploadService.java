package com.ifpb.lattesmaismais.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.exception.*;

import org.junit.platform.engine.support.descriptor.ClasspathResourceSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	private String path;
	
	@Value("${directory.file}")
	private String directoryFile;

	@Autowired
	private FileConverterService fileConverterService;

	@Autowired
	private FileEncryptionService fileEncryptionService;

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private ReceiptConverterService converterService;


	public Integer uploadFile(MultipartFile file, String hashUserId, String commentary, String url) throws IllegalStateException, IOException, EncryptionException, FileConversionException, FileWithoutNameException {

		createDiretory(hashUserId);

		// Convertendo file para array
		byte[] fileData = file.getBytes();

		// Criptografar dados e gerar novo file para upload
		byte[] encryptedData = fileEncryptionService.encryptData(fileData);
		
		// Convertendo arquivo para entidade e salvando no DB
		Receipt entity = converterService.fileToEntity(file, commentary, url);
		entity = receiptService.save(entity);
		
		String nameOnDB = entity.getId() + entity.getExtension();
		fileConverterService.writeFile(path  + "\\" + nameOnDB, encryptedData);
		
		return entity.getId();
	}

	public void uploadOnlyFiles(MultipartFile file, String hashUserId, String nameOnDB) throws IOException, EncryptionException, FileConversionException {
	
		createDiretory(hashUserId);

		// Convertendo file para array
		byte[] fileData = file.getBytes();

		// Criptografar dados e gerar novo file para upload
		byte[] encryptedData = fileEncryptionService.encryptData(fileData);
		
		// "nameOnDB" é formado por "id" + "extension" da primeira Receipt a referenciar o arquivo
		fileConverterService.writeFile(path  + "\\" + nameOnDB, encryptedData);
	}

	public void uploadCurriculum(MultipartFile file, String hashUserId) throws IOException, FileConversionException {
		
		createDiretory(hashUserId);
		byte[] xml = file.getBytes();
		fileConverterService.writeFile(path  + "\\curriculum.xml", xml);
	}

	public String readFile(String fileName, String hashUserID) throws FileConversionException, DecryptionException, IOException {
		String path = directoryFile + "\\" + hashUserID;

		if(!new File(path + "\\" + fileName).exists()) {
			throw new FileNotFoundException("Arquivo não encontrado");
		}

		// Lendo os dados criptografados:
		byte[] encryptedData = fileConverterService.readFile(path + "\\" + fileName);

		// Descriptografando dados:
		byte[] decryptedData = fileEncryptionService.decryptData(encryptedData);

		// diretório do projeto Mavem na máquina + diretório do "servidor"
		// diretório do servidor (aqui simulado com Http-server, ref = https://www.youtube.com/watch?v=59OG7vg4nYI)
		// o servidor virtual DEVE ser iniciado na pasta "server" do caminho abaixo (contexto desta aplicação)
		// usar o comando sem parênteses: (http-server -p 8082) -> este comando habilita esta porta para uso do "servidor"
		String serverPath = System.getProperty("user.dir") + "\\src\\main\\resources\\server\\dr";
		
		// cria pasta de usuário
		File userFolder = new File(serverPath + "\\" + hashUserID);
		if(!userFolder.exists()) {
			userFolder.mkdir();
		}
		
		// cria pasta temporária para o arquivo que será criado (uma pasta para cada arquivo)
		Path tempDirectory = Files.createTempDirectory(Paths.get(userFolder.getPath()), "rec_");
		
		String completePathOfFile = tempDirectory + "\\" + fileName;
				
		fileConverterService.writeFile(completePathOfFile, decryptedData);
		
		
		// retorna o caminho a ser usado para acessar o arquivo via Http-server
		return completePathOfFile.substring(completePathOfFile.indexOf("\\dr"));
	}

	// Deixei o método público para utilizar nos testes
	public void createDiretory(String userID) {
		path = directoryFile + "\\" + userID;
		
		if(!new File(path).exists()) {
			new File(path).mkdir();
		}
	}
}
