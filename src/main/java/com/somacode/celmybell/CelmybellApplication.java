package com.somacode.celmybell;

import com.somacode.celmybell.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CelmybellApplication {
	@Autowired LogService logService;

	public static void main(String[] args) {
		SpringApplication.run(CelmybellApplication.class, args);
	}

}
