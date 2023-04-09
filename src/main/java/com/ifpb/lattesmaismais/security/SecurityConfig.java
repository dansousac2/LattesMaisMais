package com.ifpb.lattesmaismais.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.ifpb.lattesmaismais.business.interfaces.RoleService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers("/api/curriculum").permitAll();
				auth.requestMatchers("/logout").permitAll();
				auth.requestMatchers("/api/curriculum/validator").hasRole(RoleService.AVALIABLE_ROLES.VALIDATOR.name());
				auth.anyRequest().authenticated();
			})
			.formLogin(Customizer.withDefaults());
			
		
		return http.build();
	}
}
