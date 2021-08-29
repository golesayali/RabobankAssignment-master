package nl.rabobank.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

/**
 * This configuration class is use to configure SwaggerUI api document to run
 * internal application controllers.
 *
 * @author Sayali G
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "swagger")
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String title;
    private String description;
    private String version;
    private String name;
    private String url;
    private String email;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("nl.rabobank"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }


    private ApiInfo getApiInfo() {

        return new ApiInfoBuilder()
                .title(getTitle())
                .description(getDescription())
                .contact(craterSwaggerApiDocContact())
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }

    private Contact craterSwaggerApiDocContact() {
        return new Contact(getName(), getUrl(), getEmail());
    }
}
