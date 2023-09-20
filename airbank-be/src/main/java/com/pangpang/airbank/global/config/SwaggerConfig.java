package com.pangpang.airbank.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		Info info = new Info().title("AirBank")
			.version("v1")
			.description("AirBank API 명세서")
			.contact(new Contact().name("GitHub").url("#"));

		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
