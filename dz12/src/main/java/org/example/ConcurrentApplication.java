package org.example;

import org.example.service.EventSystem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ConcurrentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            EventSystem eventSystem = new EventSystem();
            eventSystem.subscribe("EmailService");
            eventSystem.subscribe("PushNotificationService");
            eventSystem.subscribe("SmsGateway");

            ExecutorService executor = Executors.newFixedThreadPool(10);

            System.out.println("--- ЗАПУСК СИСТЕМЫ СОБЫТИЙ ---");

            for (int i = 0; i < 50; i++) {
                final int id = i;
                executor.execute(() -> {
                    String userId = "User-" + (id % 5);
                    eventSystem.processEvent(userId, "LOGIN_ATTEMPT");
                });
            }

            executor.shutdown();
            if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("--- ТЕСТ ЗАВЕРШЕН ---");
                System.out.println("Кэш последнего события User-1: " + eventSystem.getSessionInfo("User-1"));
            }
        };
    }
}