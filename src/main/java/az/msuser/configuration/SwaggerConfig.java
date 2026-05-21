package az.msuser.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-USER")
                        .version("1.0")
                        .description("This project provides user registration, login, and user management operations." +
                                " It is protected by JWT authentication and is a REST API " +
                                "developed with Spring Boot."));
    }
}
