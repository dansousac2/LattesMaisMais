package com.ifpb.lattesmaismais.business.interfaces;

import com.ifpb.lattesmaismais.model.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

	String generate(User user);
	Claims getClaims(String token) throws ExpiredJwtException;
	boolean isValid(String token);
	String getUserName(String token);
	Integer getUserId(String token);
	String get(HttpServletRequest request);
}
