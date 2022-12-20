package com.ifpb.lattesmaismais.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.User;
import com.ifpb.lattesmaismais.model.UserRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User findById(Integer id) throws ObjectNotFoundException {
		return userRepository.findById(id).orElseThrow(
					() -> new ObjectNotFoundException(String.format("Usuário com id %d não encontrado!", id))
				);
	}
}
