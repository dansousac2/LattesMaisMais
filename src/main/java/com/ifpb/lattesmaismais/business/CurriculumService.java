package com.ifpb.lattesmaismais.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.repository.CurriculumRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

import java.util.List;

@Service
public class CurriculumService {
	
	@Autowired
	private CurriculumRepository curriculumRepository;

	public List<Curriculum> findAll() {
		return curriculumRepository.findAll();
	}

	public Curriculum findById(Integer id) throws ObjectNotFoundException {
		if (!curriculumRepository.existsById(id)) {
			throw new ObjectNotFoundException(String.format("Currículo com id %d não encontrado!", id));
		}

		return curriculumRepository.findById(id).get();
	}
	
	public Curriculum save(Curriculum curriculum) {
		return curriculumRepository.save(curriculum);
	}
	
	public void deleteById(Integer id) throws ObjectNotFoundException {
		if (!curriculumRepository.existsById(id)) {
			throw new ObjectNotFoundException(String.format("Currículo com id %d não encontrado!", id));
		}

		curriculumRepository.deleteById(id);
	}
}
