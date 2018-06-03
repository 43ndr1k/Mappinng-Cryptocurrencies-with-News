package de.uni_leipzig.crypto_news_docs.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Grundeinstellungen der Swagger Dokumentations-API.
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

  /**
   * Welche Controller sollen in die Dokumentation aufgenommen werden und von welcher URL-Path Ebende.
   * @return Docket.
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            //.apis(RequestHandlerSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("de.uni_leipzig.crypto_news_docs.controller"))

            //.paths(PathSelectors.regex("/user*|/register.*|/oauth/*"))
            .paths(PathSelectors.any()) //for all
            .build().pathMapping("/")
                    .apiInfo(apiInfo());

  }

  /**
   * Festlegen, welche Informationen für die Beschreibung der API angezeigt werden sollen.
   * @return ApiInfo.
   */
  private ApiInfo apiInfo() {
    ApiInfo apiInfo = new ApiInfo(
            "Crypto-News-Docs REST Service",
            "Dokumentation der REST API für die Crypto-News-Docs.",
            "1.0",
            "a url to terms and services",
            "IR",
            "a License of API",
            "a license URL");
    return apiInfo;
  }


}