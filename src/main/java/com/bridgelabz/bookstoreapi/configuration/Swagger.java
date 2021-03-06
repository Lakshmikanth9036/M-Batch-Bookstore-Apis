package com.bridgelabz.bookstoreapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.bridgelabz.bookstoreapi.controller"))
				.paths(PathSelectors.any()).build();
	}
	
	private ApiInfo metaData() {
		  Contact contact=new Contact("Amruth Sagar",
		           "https://github.com/ChandrakalaKirasur/BookStoreApis.git","amrutha.sagar@bridgelabz.com");

 		   return new ApiInfoBuilder()
		           .title("Book Store Application")
		           .description("Spring boot application for Book store application")
		           .contact(contact)          
		           .build();
		   }
	
//	private ApiInfo metaData() {
//        ApiInfo apiInfo = new ApiInfo(
//                "Spring Boot REST API",
//                "Spring Boot REST API for Online Store",
//                new Contact("John Thompson", "https://springframework.guru/about/", "john@springfrmework.guru"));
//        return apiInfo;
//    }

}
