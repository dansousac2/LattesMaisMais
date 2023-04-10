package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.ifpb.lattesmaismais.business.interfaces.TokenService;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.presentation.dto.LoginDto;
import com.ifpb.lattesmaismais.presentation.dto.TokenDto;

@RestController
@RequestMapping("api/login")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginController {

	// Precisa ser gerado nas propriedades como Bean
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity loginLocal(@RequestBody LoginDto loginDto) {
		try {
			UsernamePasswordAuthenticationToken usernamePAToken = 
					new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
			
			Authentication authenticatedUser = authManager.authenticate(usernamePAToken);
			
			String token = tokenService.generate((User) authenticatedUser.getPrincipal());
			
			return ResponseEntity.ok(token);  
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/verifytoken")
	public ResponseEntity isValidToken(@RequestBody TokenDto token) {
		try {
			boolean isValid = tokenService.isValid(token.getContent());
			
			return ResponseEntity.ok(isValid ? "Token válido" : "Token inválido");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
