package de.uni_leipzig.crypto_news_docs.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class config {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
