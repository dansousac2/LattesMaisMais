package com.ifpb.lattesmaismais.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpb.lattesmaismais.model.entity.Curriculum;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer>{

	List<Curriculum> findAllByOwnerId(Integer userId);

	List<Curriculum> findByOwnerIdAndVersionAllIgnoreCase(Integer ownerId, String version);

}
