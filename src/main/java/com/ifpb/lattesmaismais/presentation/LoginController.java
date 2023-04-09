package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.interfaces.TokenService;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.presentation.dto.LoginDto;

@RestController
@RequestMapping("api/login")
public class LoginController {

	// Precisa ser gerado nas propriedades como Bean
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public String loginLocal(@RequestBody LoginDto loginDto) {
		
		UsernamePasswordAuthenticationToken usernamePAToken = 
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
		
		Authentication authenticatedUser = authManager.authenticate(usernamePAToken);
		
		return tokenService.generate((User) authenticatedUser.getPrincipal());
	}
}
