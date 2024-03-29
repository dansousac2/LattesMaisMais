package com.ifpb.lattesmaismais.business;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.User;

@Service
public class PasswordEncoderService extends BCryptPasswordEncoder {

	public void encryptPassword(User user) {
		if(user.getId() == null) {
			String passEncrypted = encode(user.getPassword());
			user.setPassword(passEncrypted);
		}
		
	}

}
