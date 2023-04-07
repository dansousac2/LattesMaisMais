package com.ifpb.lattesmaismais.business.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ifpb.lattesmaismais.model.entity.User;

public interface PasswordEncoderService extends PasswordEncoder {
	
	void encryptPassword(User user);

}
