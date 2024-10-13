package br.com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "RETRO JIRA", version = "1.0.0"), servers = {
                @Server(url = "") // http://localhost:8080/swagger-ui/index.html -- http://localhost:8080/v3/api-docs
})
@SpringBootApplication
public class MainServer {

        public static void main(String[] args) {
                SpringApplication.run(MainServer.class, args);
        }

}
