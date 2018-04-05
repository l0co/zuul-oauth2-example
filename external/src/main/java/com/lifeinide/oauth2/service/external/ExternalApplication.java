package com.lifeinide.oauth2.service.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * External oauth2 client example.
 *
 * @author Lukasz Frankowski (lifeinide.com)
 */
@SpringBootApplication
@EnableResourceServer
@EnableWebSecurity
public class ExternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalApplication.class, args);
	}

}
