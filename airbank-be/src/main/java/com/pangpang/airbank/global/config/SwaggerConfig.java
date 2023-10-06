package com.pangpang.airbank.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		Server server = new Server().url("/");

		Info info = new Info().title("AirBank")
			.version("v1")
			.description("AirBank API 명세서")
			.contact(new Contact().name("GitHub").url("#"));

		return new OpenAPI()
			.info(info)
			.addServersItem(server);
	}
}
