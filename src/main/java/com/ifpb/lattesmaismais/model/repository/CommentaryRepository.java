package com.ifpb.lattesmaismais.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;

public interface CommentaryRepository extends JpaRepository<ValidatorCommentary, Integer>{

	
}
