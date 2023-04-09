package com.ifpb.lattesmaismais.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.repository.UserRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public User findById(Integer id) throws ObjectNotFoundException {
		return repository.findById(id).orElseThrow(
					() -> new ObjectNotFoundException("Usuário com não encontrado/ id: " + id)
				);
	}
	
	public User findByEmail(String email) throws ObjectNotFoundException {
		return repository.findByEmail(email).orElseThrow(
				() -> new ObjectNotFoundException("Usuário com não encontrado/ email: " + email)
			);
	}
}
