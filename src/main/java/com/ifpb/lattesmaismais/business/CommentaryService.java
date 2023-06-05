package com.ifpb.lattesmaismais.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;
import com.ifpb.lattesmaismais.model.repository.CommentaryRepository;

@Service
public class CommentaryService {
	
	@Autowired
	private CommentaryRepository repository;

	public ValidatorCommentary save(ValidatorCommentary entity) {

		return repository.save(entity);
	}

	public ValidatorCommentary findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado para id: " + id));
	}
	
}
