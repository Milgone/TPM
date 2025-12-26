package org.example.parser;

import org.example.parser.service.ParserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(ParserService parserService) {
        return args -> {
            parserService.runLogic();
            Thread.sleep(1000); // Чтобы консоль не перемешалась
            parserService.showThreads();
        };
    }
}