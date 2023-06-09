package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.CurriculumService;
import com.ifpb.lattesmaismais.business.GenericsCurriculumService;
import com.ifpb.lattesmaismais.model.entity.Curriculum;

@RestController
@RequestMapping("api/export")
public class ExportController {

	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private GenericsCurriculumService genCS;
	
	public ResponseEntity export(Integer curriculumId, Integer ownerId) {
		try {
			Curriculum curriculum = genCS.verifyCurriculumOwnerAndGetCurriculum(curriculumId, ownerId, curriculumService);
			
			//TODO gerar pdf e retornar para na requisição
			
			
			return null;
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
