package com.lifeinide.oauth2.service.internal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * External oauth2 client example.
 *
 * @author Lukasz Frankowski (lifeinide.com)
 */
@SpringBootApplication
public class InternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternalApplication.class, args);
	}

}
