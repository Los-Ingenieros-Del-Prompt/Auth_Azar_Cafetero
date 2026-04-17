package com.aguardiantes.azarcafetero.auth_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		// En Lambda las variables vienen del entorno, no de .env
		if (System.getenv("AWS_LAMBDA_FUNCTION_NAME") == null) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dotenv.entries().forEach(e ->
					System.setProperty(e.getKey(), e.getValue())
			);
		}
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}