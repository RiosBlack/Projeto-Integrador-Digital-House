package com.grupo7.renthotels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RenthotelsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenthotelsApplication.class, args);
	}

}
