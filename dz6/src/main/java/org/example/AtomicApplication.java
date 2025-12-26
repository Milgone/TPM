package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
public class AtomicApplication {

    private volatile boolean running = true;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicReference<String> cache = new AtomicReference<>(null);

    public static void main(String[] args) {
        SpringApplication.run(AtomicApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            Thread worker = new Thread(() -> {
                while (running) {
                    counter.incrementAndGet();
                }
                System.out.println("Поток остановлен. Финальный счет: " + counter.get());
            });

            worker.start();

            Thread cacheThread1 = new Thread(() -> updateCache("Data-A"));
            Thread cacheThread2 = new Thread(() -> updateCache("Data-B"));

            cacheThread1.start();
            cacheThread2.start();

            Thread.sleep(1000);
            running = false;

            worker.join();
            cacheThread1.join();
            cacheThread2.join();

            System.out.println("Итоговое значение счетчика: " + counter.get());
            System.out.println("Значение в кэше: " + cache.get());
        };
    }

    private void updateCache(String newValue) {
        cache.compareAndSet(null, newValue);
    }
}