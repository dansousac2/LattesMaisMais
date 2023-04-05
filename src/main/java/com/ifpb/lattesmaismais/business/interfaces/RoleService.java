package com.ifpb.lattesmaismais.business.interfaces;

import com.ifpb.lattesmaismais.model.entity.Role;

public interface RoleService {

	enum AVALIABLE_ROLES {
		USER,
		VALIDATOR
	}
	
	void createDefaultValues();
	Role findDefault();
	Role findByName(String name);
}
