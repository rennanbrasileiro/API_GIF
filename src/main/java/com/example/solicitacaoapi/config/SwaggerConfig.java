/*package com.example.solicitacaoapi.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springdoc.core.models.SwaggerUiConfigParameters;
import org.springdoc.webmvc.ui.SwaggerUiWebMvcConfigurer;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SwaggerConfig {

    // Define o grupo de APIs que será documentado
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public") // Nome do grupo de APIs
                .pathsToMatch("/**") // Todos os caminhos de endpoint serão incluídos
                .build();
    }

    // Configuração adicional do Swagger UI
    @Bean
    public SwaggerUiConfigParameters swaggerUiConfigParameters() {
        return new SwaggerUiConfigParameters();
        // Configure parâmetros adicionais para o Swagger UI se necessário
        // swaggerUiConfigParameters.setUrl("http://localhost:8080/v3/api-docs");
    }

    // Configuração de WebMvc para Swagger UI
    @Bean
    public WebMvcConfigurer swaggerUiWebMvcConfigurer() {
        return new SwaggerUiWebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Configure os recursos estáticos para o Swagger UI se necessário
                // registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/swagger-ui/");
            }
        };
    }

    // Configuração da documentação OpenAPI
    @Bean
    public OpenApiResource openApiResource() {
        return new OpenApiResource();
        // Configure o recurso OpenAPI se necessário
        // openApiResource.setApplicationContext(applicationContext);
    }
    
    // Adicione configurações adicionais para Swagger, se necessário
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("API Documentation")
                    .version("1.0")
                    .description("This is a sample API documentation")
                    .termsOfService("http://swagger.io/terms/")
                    .contact(new Contact()
                        .name("Support Team")
                        .url("http://www.example.com/support")
                        .email("support@example.com"))
                    .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org")));
    }
    
}*/
