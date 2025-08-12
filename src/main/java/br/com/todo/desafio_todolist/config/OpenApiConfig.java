package br.com.todo.desafio_todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo List API")
                        .version("1.0.0")
                        .description("API RESTful para gerenciamento de tarefas (To-Do List)")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@exemplo.com")
                                .url("https://github.com/seuusuario/desafio-todolist"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}