package com.ifpb.lattesmaismais.business;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ifpb.lattesmaismais.business.interfaces.PasswordEncoderService;
import com.ifpb.lattesmaismais.model.entity.User;

public class PasswordEncoderServiceImpl extends BCryptPasswordEncoder implements PasswordEncoderService{

	@Override
	public void encryptPassword(User user) {
		if(user.getId() != null) {
			String passEncrypted = encode(user.getPassword());
			user.setPassword(passEncrypted);
		}
		
	}

}
