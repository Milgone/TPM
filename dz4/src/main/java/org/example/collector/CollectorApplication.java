package org.example;

import org.example.collector.DataCollector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            DataCollector collector = new DataCollector();

            Runnable producer = () -> {
                try {
                    for (int i = 0; i < 20; i++) {
                        collector.collectItem("Item-" + i);
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };

            Runnable consumer = () -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(500);
                        collector.processBatch();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };

            Thread t1 = new Thread(producer, "Producer-1");
            Thread t2 = new Thread(producer, "Producer-2");
            Thread t3 = new Thread(consumer, "Consumer");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
        };
    }
}