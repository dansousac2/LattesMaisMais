package com.ifpb.lattesmaismais.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.business.interfaces.RoleService;
import com.ifpb.lattesmaismais.model.entity.Role;
import com.ifpb.lattesmaismais.model.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository repository;
	
	@Override
	public void createDefaultValues() {
		
		for(AVALIABLE_ROLES role : AVALIABLE_ROLES.values()) {
			Role roleInDb = findByName("ROLE_" + role.name());
			
			if(roleInDb == null) {
				Role newRole = new Role();
				newRole.setName("ROLE_" + role.name());
				repository.save(newRole);
			}
		}
	}

	@Override
	public Role findDefault() {
		return findByName("ROLE_" + AVALIABLE_ROLES.USER.name());
	}

	@Override
	public Role findByName(String name) {
		Optional<Role> roleOp = repository.findByName(name);
		
		return roleOp.orElse(null);
	}

	
}
