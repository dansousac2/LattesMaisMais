package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.CurriculumConverterService;
import com.ifpb.lattesmaismais.business.CurriculumService;
import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;

@RestController
@RequestMapping("api/curriculum")
public class CurriculumController {

	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private CurriculumConverterService converterService;
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		try {
			Curriculum entity = curriculumService.findById(id);
			CurriculumDto dto = converterService.curriculumToDto(entity);
			
			return ResponseEntity.ok(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	//TODO remover endpiont de teste 
	@GetMapping
	public String testePublico() {
		return "<h1>Testando acesso PÚBLICO ao endpoint!!!</h1>";
	}
	
	//TODO remover endpiont de teste 
	@GetMapping("/private")
	public String testePrivado() {
		return "<h1>Testando acesso PRIVADO ao endpoint!!!</h1>";
	}
	//TODO criar demais métodos de CurriculumController
}
