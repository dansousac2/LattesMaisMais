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
	private CurriculumRepository repository;

	public List<Curriculum> findAll() {
		return repository.findAll();
	}

	public Curriculum findById(Integer id) throws ObjectNotFoundException {
		if (!repository.existsById(id)) {
			throw new ObjectNotFoundException(String.format("Currículo com id %d não encontrado!", id));
		}

		return repository.findById(id).get();
	}
	
	public Curriculum save(Curriculum curriculum) {
		return repository.save(curriculum);
	}
	
	public void deleteById(Integer id) throws ObjectNotFoundException {
		if (!repository.existsById(id)) {
			throw new ObjectNotFoundException(String.format("Currículo com id %d não encontrado!", id));
		}

		repository.deleteById(id);
	}

	public List<Curriculum> findAllByUserId(Integer userId) {
		if(userId == null || userId <= 0) {
			throw new IllegalArgumentException("Id de usuário não pode ser nulo!");
		}
		
		return repository.findAllByOwnerId(userId);
	}

	public List<Curriculum> findByOwnerIdAndVersion(Integer ownerId, String version) {
		
		return repository.findByOwnerIdAndVersionAllIgnoreCase(ownerId, version);
	}
}
