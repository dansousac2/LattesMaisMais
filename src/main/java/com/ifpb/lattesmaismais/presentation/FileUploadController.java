package com.ifpb.lattesmaismais.presentation;

import com.ifpb.lattesmaismais.business.HashService;
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

	@Autowired
	private HashService hashService;

	@PostMapping("api/fileupload")
	public ResponseEntity uploadFile(@RequestParam MultipartFile file, String userId) {
		try {
			String hashUserId = hashService.hashingSHA256(userId);

			uploadService.uploadFile(file, hashUserId);
			return new ResponseEntity(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
