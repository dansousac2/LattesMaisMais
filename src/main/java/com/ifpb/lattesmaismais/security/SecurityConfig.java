package com.ifpb.lattesmaismais.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception{
		return authConf.getAuthenticationManager();
	}
	
	// Ao retirar esse Bean o erro "There is no PasswordEncoder mapped for the id 'null'" foi resolvido (???)
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/user").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/login").permitAll();
				auth.requestMatchers("/api/logout").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/login/verifytoken").permitAll();
				
				auth.requestMatchers("/api/validatorcommentary").hasRole(AVALIABLE_ROLES.VALIDATOR.name());
				auth.requestMatchers("/api/curriculum/ownerandversion").hasRole(AVALIABLE_ROLES.VALIDATOR.name());
				
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
	
	// CORS - resolve problema de policiamento de rotas credenciadas no navegador
	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {
		List<String> all = Arrays.asList("*");
		
		CorsConfiguration corsConf = new CorsConfiguration();
		corsConf.setAllowedMethods(all);
		corsConf.setAllowedOriginPatterns(all);
		corsConf.setAllowedHeaders(all);
		corsConf.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConf);
		
		CorsFilter corsFilter = new CorsFilter(source);
		
		FilterRegistrationBean<CorsFilter> filter =	new FilterRegistrationBean<CorsFilter>(corsFilter);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return filter;
	}
}
