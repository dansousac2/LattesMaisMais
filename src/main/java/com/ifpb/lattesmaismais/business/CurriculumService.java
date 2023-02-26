package com.ifpb.lattesmaismais.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.repository.CurriculumRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

@Service
public class CurriculumService {
	
	@Autowired
	private CurriculumRepository curriculumRepository;
	
	public Curriculum findById(Integer id) throws ObjectNotFoundException {
		return curriculumRepository.findById(id).orElseThrow(
					() -> new ObjectNotFoundException(String.format("Currículo com id %d não encontrado!", id))
				);
	}
	
	public Curriculum save(Curriculum curriculum) {
		return curriculumRepository.save(curriculum);
	}
	
	public void deleteById(Integer id) {
		curriculumRepository.deleteById(id);
	}
}
