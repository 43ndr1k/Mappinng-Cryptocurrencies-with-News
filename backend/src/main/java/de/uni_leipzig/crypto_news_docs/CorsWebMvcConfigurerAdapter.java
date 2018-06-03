package de.uni_leipzig.crypto_news_docs;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class CorsWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowedOrigins("*")
			.allowedHeaders("*");
	}
}
