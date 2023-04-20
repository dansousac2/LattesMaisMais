package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.ifpb.lattesmaismais.business.UserConverterService;
import com.ifpb.lattesmaismais.business.interfaces.TokenService;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.presentation.dto.LoginDto;
import com.ifpb.lattesmaismais.presentation.dto.TokenDto;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoFront;

@RestController
@RequestMapping("api/login")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginController {

	// Precisa ser gerado nas propriedades como Bean
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserConverterService userConverter;
	
	@PostMapping
	public ResponseEntity loginLocal(@RequestBody LoginDto loginDto) {
		try {
			UsernamePasswordAuthenticationToken usernamePAToken = 
					new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
			
			Authentication authenticatedUser = authManager.authenticate(usernamePAToken);
			
			User user = (User) authenticatedUser.getPrincipal();
			
			String token = tokenService.generate(user);
			UserDtoFront dtoFront = userConverter.userToDtoFront(user);
			
			return new ResponseEntity(new TokenDto(token, dtoFront), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/verifytoken")
	public ResponseEntity isValidToken(@RequestBody TokenDto token) {
		try {
			boolean isValid = tokenService.isValid(token.getToken());
			
			return ResponseEntity.ok(isValid);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
