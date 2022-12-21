package com.ifpb.lattesmaismais.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	@Autowired
	private FileConverterService fileConverterService;

	@Autowired
	private FileEncryptionService fileEncryptionService;

	public void uploadFile(MultipartFile file, String userID) throws IllegalStateException, IOException, EncryptionException, FileConversionException {
		//TODO adicionar restrições
		// vai até a pasta do projeto
		String projectDirectory = new File("").getAbsolutePath();
		
		// concatena com a pasta destino
		String path = projectDirectory + "\\src\\main\\resources\\receipts\\user_" + userID;
		
		if(!new File(path).exists()) {
			new File(path).mkdir();
		}

		// Convertendo file para array
		byte[] fileData = file.getBytes();

		// Criptografar dados e gerar novo file para upload
		byte[] encryptedData = fileEncryptionService.encryptData(fileData);

		fileConverterService.writeFile(path  + "\\" + file.getOriginalFilename(), encryptedData);

//		file.transferTo(new File(path + "\\" + file.getOriginalFilename()));
	}

	public void readFile(String fileName, String userID) throws FileNotFoundException, FileConversionException, DecryptionException {
		String projectDirectory = new File("").getAbsolutePath();
		String path = projectDirectory + "\\src\\main\\resources\\receipts\\user_" + userID;

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
}
