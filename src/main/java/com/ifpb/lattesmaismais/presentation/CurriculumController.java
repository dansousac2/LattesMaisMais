package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.CurriculumConverterService;
import com.ifpb.lattesmaismais.business.CurriculumService;
import com.ifpb.lattesmaismais.business.GenericsCurriculumService;
import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/curriculum")
public class CurriculumController {

	@Autowired
	private CurriculumService service;
	
	@Autowired
	private CurriculumConverterService converterService;
	
	@Autowired
	private GenericsCurriculumService genCurriculumService;

	@GetMapping
	public ResponseEntity findAll() {
		List<Curriculum> entityList = service.findAll();

		List<CurriculumDto> dtoList = converterService.curriculumToDto(entityList, this.genCurriculumService);

		return ResponseEntity.ok().body(dtoList);
	}
	
	@GetMapping("/findall/{userId}")
	public ResponseEntity findAllByUserId(@PathVariable Integer userId) {
		try {
			List<Curriculum> entityList = service.findAllByUserId(userId);
			
			List<CurriculumDto> dtoList = converterService.curriculumToDto(entityList, this.genCurriculumService);
			
			return ResponseEntity.ok().body(dtoList);	
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		try {
			Curriculum entity = service.findById(id);
			CurriculumDto dto = converterService.curriculumToDto(entity, this.genCurriculumService);
			
			return ResponseEntity.ok(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody @Valid CurriculumDtoBack dto) {
		try {
			// lança exceção em caso de incompatiblidade de informações
			User owner = genCurriculumService.verifyCurriculumDto(dto, this.service);
			// remove os ids das entidades Curriculum, Entry e Receipt
			Curriculum entity = converterService.dtoBackToCurriculum(dto, owner, this.genCurriculumService, false);
			
			entity = service.save(entity);
			
			CurriculumDto dtoToReturn = converterService.curriculumToDto(entity, this.genCurriculumService);
			
			return new ResponseEntity(dtoToReturn, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity update(@RequestBody @Valid CurriculumDtoBack dto) {
		try {
			// lança exceção em caso de incompatiblidade de informações
			User owner = genCurriculumService.verifyCurriculumDto(dto, this.service);
			
			Curriculum entity = converterService.dtoBackToCurriculum(dto, owner, this.genCurriculumService, true);
			
			entity = service.save(entity);
			
			CurriculumDto dtoToReturn = converterService.curriculumToDto(entity, this.genCurriculumService);
			
			return new ResponseEntity(dtoToReturn, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		try {
			service.deleteById(id);
			
			return ResponseEntity.ok("Id do currículo deletado: " + id);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
