package com.abm.mainet.web.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Generates Swagger 2/OpenAPI-compatible JSON for the existing Accounts REST controllers.
 *
 * Registered only with the REST DispatcherServlet via spring-webmvc-rest.xml.
 * The existing dispatcher is mapped to /rest/* in web.xml.
 */
@Configuration
@EnableSwagger2
public class AccountsSwaggerConfig {

    @Bean
    public Docket accountsRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("accounts-rest")
                .select()
                .apis(RequestHandlerSelectors.basePackage(
                        "com.abm.mainet.account.rest.ui.controller"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/rest")
                .apiInfo(new ApiInfoBuilder()
                        .title("MainetService Accounts REST API")
                        .description("Receipt Entry, Vendor Bill Approval and Voucher Posting REST operations.")
                        .version("1.0.0")
                        .termsOfServiceUrl("")
                        .contact(new Contact("", "", ""))
                        .license("")
                        .licenseUrl("")
                        .build());
    }
}
