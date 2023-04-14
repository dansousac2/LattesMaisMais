package com.ifpb.lattesmaismais.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.ifpb.lattesmaismais.business.interfaces.RoleService.AVALIABLE_ROLES;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private TokenFilter tokenFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers("/api/curriculum").permitAll();
				auth.requestMatchers("/logout").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/login").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/login/verifytoken").permitAll();
				//TODO remover abaixo - teste
				auth.requestMatchers(HttpMethod.POST, "/api/uploadcurriculumxml").permitAll();
				//TODO remover abaixo - teste
				auth.requestMatchers("/api/curriculum/{id}").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/curriculum/validator").hasRole(AVALIABLE_ROLES.VALIDATOR.name());
				auth.anyRequest().authenticated();
			})
			.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		http
			.logout(logout -> {
				logout
					.clearAuthentication(true)
					.invalidateHttpSession(true)
					.logoutUrl("/api/logout")
					.logoutSuccessHandler(new LogoutSuccessHandler() {
						@Override
						public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
								throws IOException, ServletException {
							// não faz nada no logout
						}
					});
			});
		
		return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception{
		return authConf.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
