package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ifpb.lattesmaismais.business.FileUploadService;

@RestController
public class FileUploadController {

	@Autowired
	private FileUploadService uploadService;
	
	@PostMapping("api/fileupload")
	public ResponseEntity uploadFile(@RequestParam MultipartFile file, Integer userId) {

		try {
			uploadService.uploadFile(file, userId);
			return new ResponseEntity(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
