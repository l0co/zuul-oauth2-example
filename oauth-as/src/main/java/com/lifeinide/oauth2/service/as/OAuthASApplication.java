package com.lifeinide.oauth2.service.as;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Oauth2 authorization server.
 *
 * @author Lukasz Frankowski (lifeinide.com)
 */
@SpringBootApplication
public class OAuthASApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuthASApplication.class, args);
	}

}
