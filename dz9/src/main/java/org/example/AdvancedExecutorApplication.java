package org.example;

import org.example.service.PeriodicDataAggregator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
public class AdvancedExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedExecutorApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            ExecutorService executor = Executors.newFixedThreadPool(5);

            // 1. Future и отмена задачи
            Future<?> longTask = executor.submit(() -> {
                try { Thread.sleep(8000); } catch (InterruptedException e) {
                    System.out.println("Задача была прервана!");
                }
            });
            Thread.sleep(2000);
            boolean cancelled = longTask.cancel(true);
            System.out.println("Задача отменена: " + cancelled);

            // 2. Список задач и invokeAll
            List<Callable<Boolean>> tasks = List.of(
                    () -> { Thread.sleep(1500); return true; },
                    () -> { Thread.sleep(2000); return false; },
                    () -> { Thread.sleep(1000); return true; }
            );
            List<Future<Boolean>> results = executor.invokeAll(tasks);
            for (Future<Boolean> f : results) {
                System.out.println("Результат из invokeAll: " + f.get());
            }

            // 3. ScheduledExecutorService и Агрегатор
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            PeriodicDataAggregator aggregator = new PeriodicDataAggregator();

            scheduler.scheduleAtFixedRate(aggregator::aggregate, 0, 5, TimeUnit.SECONDS);

            scheduler.schedule(() -> System.out.println("Мониторинг очереди: OK"), 1, TimeUnit.SECONDS);
        };
    }
}