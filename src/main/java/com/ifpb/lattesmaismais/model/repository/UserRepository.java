package com.ifpb.lattesmaismais.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpb.lattesmaismais.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	List<User> findByRolesNameIgnoreCase(String name);
}
