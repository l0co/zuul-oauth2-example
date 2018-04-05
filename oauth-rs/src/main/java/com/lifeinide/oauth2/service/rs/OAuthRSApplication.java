package com.lifeinide.oauth2.service.rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Oauth2 resource server.
 *
 * @author Lukasz Frankowski (lifeinide.com)
 */
@SpringBootApplication
@EnableResourceServer
@EnableWebSecurity
public class OAuthRSApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuthRSApplication.class, args);
	}

}
