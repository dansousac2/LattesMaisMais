package com.ifpb.lattesmaismais.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	public void uploadCurriculum(MultipartFile file, String hashUserId) throws IOException, FileConversionException {
		
		createDiretory(hashUserId);
		byte[] xml = file.getBytes();
		fileConverterService.writeFile(path  + "\\curriculum.xml", xml);
	}

	public void readFile(String fileName, String userID) throws FileNotFoundException, FileConversionException, DecryptionException {
		String path = directoryFile + "\\" + userID;

		if(!new File(path + "\\" + fileName).exists()) {
			throw new FileNotFoundException();
		}

		// Lendo os dados criptografados:
		byte[] encryptedData = fileConverterService.readFile(path + "\\" + fileName);

		// Descriptografando dados:
		byte[] decryptedData = fileEncryptionService.decryptData(encryptedData);

		// Criando novo diretório de teste pra salvar:
		String newPath = path + "_decrypted";

		if(!new File(newPath).exists()) {
			new File(newPath).mkdir();
		}

		fileConverterService.writeFile(newPath + "\\" + fileName, decryptedData);
	}

	// Deixei o método público para utilizar nos testes
	public void createDiretory(String userID) {
		path = directoryFile + "\\" + userID;
		
		if(!new File(path).exists()) {
			new File(path).mkdir();
		}
	}
}
