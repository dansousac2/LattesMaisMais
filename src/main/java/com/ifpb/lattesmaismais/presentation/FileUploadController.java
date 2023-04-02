package com.ifpb.lattesmaismais.presentation;

import com.ifpb.lattesmaismais.business.*;
import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity uploadFile(@RequestParam MultipartFile file, String userId) {
		try {
			// Criando hash:
			String hashUserId = hashService.hashingSHA256(userId);

			uploadService.uploadFile(file, hashUserId);

			return new ResponseEntity(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("api/uploadcurriculumxml")
	public ResponseEntity uploadCurriculum(@RequestParam MultipartFile file, Integer userId) {
		try {
			// Criando hash:
			String hashUserId = hashService.hashingSHA256(userId.toString());

			uploadService.uploadCurriculum(file, hashUserId);
			
			Curriculum curriculum = cXmlParseService.xmlToCurriculum(userId);
			
			CurriculumDto curriculumDto = curriculumConverterService.curriculumToDto(curriculum);
			
			return new ResponseEntity(curriculumDto, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("api/readfile")
	public ResponseEntity readFile(@RequestParam String fileName, String userId) {
		try {
			String hashUserId = hashService.hashingSHA256(userId);

			uploadService.readFile(fileName, hashUserId);

			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
