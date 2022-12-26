package com.ifpb.lattesmaismais.model.repository;

import com.ifpb.lattesmaismais.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
