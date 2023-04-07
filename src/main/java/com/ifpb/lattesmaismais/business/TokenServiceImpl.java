package com.ifpb.lattesmaismais.business;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.business.interfaces.TokenService;
import com.ifpb.lattesmaismais.model.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl implements TokenService {

	public static final String CLAIM_USER_ID = "userid";
	public static final String CLAIM_USER_NAME = "username";
	
	@Value("${jwt.expiration}")
	private String expiration;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Override
	public String generate(User user) {
		long expiration = Long.valueOf(this.expiration);
		
		LocalDateTime expiLDT = LocalDateTime.now().plusMinutes(expiration);
		Instant expiInstant = expiLDT.atZone(ZoneId.systemDefault()).toInstant();
		Date expiDate = Date.from(expiInstant);
		
		String token = Jwts.builder()
						.setExpiration(expiDate)
						.setSubject(user.getId().toString())
						.claim(CLAIM_USER_ID, user.getId())
						.claim(CLAIM_USER_NAME, user.getName())
						.signWith(SignatureAlgorithm.HS512, secret)
						.compact();
		
		return token;
	}

	@Override
	public Claims getClaims(String token) throws ExpiredJwtException {
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public boolean isValid(String token) {
		if(token == null) {
			return false;
		}

		try {
			Claims claims = getClaims(token);
			
			LocalDateTime expirationLDT = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			
			return expirationLDT.isAfter(LocalDateTime.now());
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public String getUserName(String token) {
		return (String) getClaims(token).get(CLAIM_USER_NAME);
	}

	@Override
	public Integer getUserId(String token) {
		return Integer.parseInt(getClaims(token).getSubject());
	}

	@Override
	public String get(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if(authorization == null || !authorization.startsWith("Bearer")) {
			return null;
		}
		
		return authorization.split(" ")[1];
	}

}
