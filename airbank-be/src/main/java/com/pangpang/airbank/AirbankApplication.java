package com.pangpang.airbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AirbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirbankApplication.class, args);
	}

}
