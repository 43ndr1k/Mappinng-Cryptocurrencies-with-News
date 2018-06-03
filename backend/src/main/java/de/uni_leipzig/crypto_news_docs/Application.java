package de.uni_leipzig.crypto_news_docs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = {"de.uni_leipzig.crypto_news_docs"})
@EntityScan("de.uni_leipzig.crypto_news_docs")
@EnableJpaRepositories("de.uni_leipzig.crypto_news_docs")
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@Bean
	public WebMvcConfigurerAdapter corsConfigurer() {
		
		// This allows all origins for CORS (i.e. disables CSRF protection) for development.
		// DO NOT USE THIS FOR PRODUCTION ENVIRONMENTS!
		return (WebMvcConfigurerAdapter) new CorsWebMvcConfigurerAdapter();
	}

}
