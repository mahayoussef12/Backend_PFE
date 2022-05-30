package com.isima.projet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.security.SecureRandom;

@EntityScan(
		basePackageClasses = { Test.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@SecurityScheme(name = "pfe", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "API", version = "2.0", description = "Projet Fin D'etude"))
public class Test {
	public static String uploadDirectory =
			System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	public static void main(String[] args) {
		new File(uploadDirectory).mkdir();
		SpringApplication.run(Test.class, args);
	}
	@Bean
	public PasswordEncoder encoder() {
		int strength=10;
		return new BCryptPasswordEncoder(strength,new SecureRandom());

	}
	/*@Bean
	PasswordEncoder passwordEncoder() {
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
		return new DelegatingPasswordEncoder("bcrypt", encoders);
	}*/


}
