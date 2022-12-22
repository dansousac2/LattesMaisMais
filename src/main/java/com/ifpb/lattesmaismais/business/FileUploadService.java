package com.ifpb.lattesmaismais.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import com.ifpb.lattesmaismais.presentation.exception.HashException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	@Value("${directory.file}")
	private String directoryFile;

	@Autowired
	private FileConverterService fileConverterService;

	@Autowired
	private FileEncryptionService fileEncryptionService;

	public void uploadFile(MultipartFile file, String userID) throws IllegalStateException, IOException, EncryptionException, FileConversionException, HashException {
		//TODO adicionar restrições

		// concatena com a pasta destino
		String path = directoryFile + "\\" + userID;
		
		if(!new File(path).exists()) {
			new File(path).mkdir();
		}

		// Convertendo file para array
		byte[] fileData = file.getBytes();

//		// Gerando hash com conteúdo do arquivo:
//		String hashData = hashService.hashingSHA256(fileData);

		// Criptografar dados e gerar novo file para upload
		byte[] encryptedData = fileEncryptionService.encryptData(fileData);

//		// Separando a extensão do arquivo
//		int extensionIndex = file.getOriginalFilename().indexOf(".");
//		String fileExtension = file.getOriginalFilename().substring(extensionIndex);

		fileConverterService.writeFile(path  + "\\" + file.getOriginalFilename(), encryptedData);

//		file.transferTo(new File(path + "\\" + file.getOriginalFilename()));
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
}
