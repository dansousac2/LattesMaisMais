package com.ifpb.lattesmaismais.business;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	public void uploadFile(MultipartFile file, String userID) throws IllegalStateException, IOException {
		//TODO adicionar restrições
		String projectDirectory = new File("").getAbsolutePath();
		// vai até a pasta do projeto
		String path = projectDirectory + "\\src\\main\\resources\\receipts\\user_" + userID;

		// concatena com a pasta destino
		if(!new File(path).exists()) {
			new File(path).mkdir();
		}
		
		file.transferTo(new File(path + "\\" + file.getOriginalFilename()));
	}
}
