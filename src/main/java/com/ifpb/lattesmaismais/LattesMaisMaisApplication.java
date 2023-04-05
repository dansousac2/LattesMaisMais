package com.ifpb.lattesmaismais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ifpb.lattesmaismais.business.interfaces.RoleService;

@SpringBootApplication
@EnableWebMvc
public class LattesMaisMaisApplication implements WebMvcConfigurer, CommandLineRunner {

	@Autowired
	private RoleService roleService;
	
	public static void main(String[] args) {
		SpringApplication.run(LattesMaisMaisApplication.class, args);
	}

	// WebMvc
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/**")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
	}

	// CommandLineRunner
	@Override
	public void run(String... args) throws Exception {
		roleService.createDefaultValues();
	}

}
