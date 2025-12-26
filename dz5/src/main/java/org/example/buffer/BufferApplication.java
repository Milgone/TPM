package org.example;

import org.example.buffer.BoundedBuffer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BufferApplication {

    public static void main(String[] args) {
        SpringApplication.run(BufferApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);

            Runnable producer = () -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        buffer.put(i);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };

            Runnable consumer = () -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        buffer.take();
                        Thread.sleep(150);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };

            new Thread(producer, "Producer-1").start();
            new Thread(producer, "Producer-2").start();
            new Thread(consumer, "Consumer-1").start();
            new Thread(consumer, "Consumer-2").start();
        };
    }
}