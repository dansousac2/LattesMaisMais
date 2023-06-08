package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ifpb.lattesmaismais.business.CurriculumConverterService;
import com.ifpb.lattesmaismais.business.CurriculumXmlParseService;
import com.ifpb.lattesmaismais.business.FileUploadService;
import com.ifpb.lattesmaismais.business.HashService;
import com.ifpb.lattesmaismais.presentation.dto.FileUploadDto;

import jakarta.validation.Valid;

@RestController
public class FileUploadController {

	@Autowired
	private FileUploadService uploadService;

	@Autowired
	private HashService hashService;

	@Autowired
	private CurriculumXmlParseService cXmlParseService;
	
	@Autowired
	private CurriculumConverterService curriculumConverterService;

	@PostMapping("api/fileupload")
	public ResponseEntity uploadFile(@RequestParam MultipartFile file,@Valid FileUploadDto dto) {
		try {
			// Criando hash:
			String hashUserId = hashService.hashingSHA256(String.valueOf(dto.getUserId()));

			Integer receiptId = uploadService.uploadFile(file, hashUserId, dto.getUserCommentary(), dto.getUrl());

			return new ResponseEntity(receiptId, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("api/onlyuploadfile")
	public ResponseEntity onlyUploadFile(@RequestParam MultipartFile file,@Valid FileUploadDto dto) {
		try {
			// Criando hash:
			String hashUserId = hashService.hashingSHA256(String.valueOf(dto.getUserId()));
			
			// enviando arquivos para pasta do usuário sem criar novas classes no DB
			uploadService.uploadOnlyFiles(file, hashUserId, dto.getNameOnDB());
			
			return new ResponseEntity(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("api/uploadcurriculumxml")
	public ResponseEntity uploadCurriculum(@RequestParam MultipartFile file, String userId) {
		try {
			// Criando hash:
			String hashUserId = hashService.hashingSHA256(userId);

			uploadService.uploadCurriculum(file, hashUserId);
			
			Integer curriculumId = cXmlParseService.xmlToCurriculum(userId);
			
			return new ResponseEntity(curriculumId, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("api/readfile")
	public ResponseEntity readFile(@RequestParam String fileName, String userId) {
		try {
			String hashUserId = hashService.hashingSHA256(userId);

			String filePathInHttpServer = uploadService.readFile(fileName, hashUserId);

			return ResponseEntity.ok(filePathInHttpServer);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
