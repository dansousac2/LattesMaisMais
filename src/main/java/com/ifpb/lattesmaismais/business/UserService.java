package com.ifpb.lattesmaismais.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.repository.UserRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

@Service
public class UserService implements UserDetailsService {

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
	
	public boolean existsEmail(String email) {
		return repository.existsByEmail(email);
	}
	
	public User save(User user) {
		return repository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			
			return findByEmail(username);
			
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	public List<User> findByRolesName(String name) {
		return repository.findByRolesNameIgnoreCase(name);
	}
}
