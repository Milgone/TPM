package org.example;

import org.example.task.SumTask;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

@SpringBootApplication
public class ForkJoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForkJoinApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            int size = 1_000_000;
            long[] numbers = new long[size];
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                numbers[i] = random.nextInt(100);
            }

            ForkJoinPool pool = new ForkJoinPool();

            long start = System.currentTimeMillis();
            long totalSum = pool.invoke(new SumTask(numbers, 0, numbers.length));
            long end = System.currentTimeMillis();

            System.out.println("--- РЕЗУЛЬТАТЫ FORK/JOIN ---");
            System.out.println("Количество элементов: " + size);
            System.out.println("Сумма: " + totalSum);
            System.out.println("Время выполнения: " + (end - start) + " ms");
            System.out.println("Активных потоков в пуле: " + pool.getParallelism());

            pool.shutdown();
        };
    }
}