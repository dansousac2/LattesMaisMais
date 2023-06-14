package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.CurriculumService;
import com.ifpb.lattesmaismais.business.ExportService;
import com.ifpb.lattesmaismais.business.GenericsCurriculumService;
import com.ifpb.lattesmaismais.model.entity.Curriculum;

@RestController
@RequestMapping("api/export")
public class ExportController {

	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private ExportService service;
	
	@Autowired
	private GenericsCurriculumService genCS;
	
	@GetMapping
	public ResponseEntity export(@RequestParam Integer curriculumId, @RequestParam Integer ownerId) {
		try {
			Curriculum curriculum = genCS.verifyCurriculumOwnerAndGetCurriculum(curriculumId, ownerId, curriculumService);
			
			String pathCurriculumPdf = service.generatePdf(curriculum, ownerId);
			
			return ResponseEntity.ok(pathCurriculumPdf);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
