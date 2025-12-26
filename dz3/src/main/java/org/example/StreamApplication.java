package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class StreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            List<Integer> numbers = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 1_000_000; i++) {
                numbers.add(random.nextInt(100));
            }

            long startStream = System.currentTimeMillis();
            long sumStream = numbers.stream()
                    .filter(n -> n % 2 == 0)
                    .map(n -> n * 2)
                    .mapToLong(Integer::longValue)
                    .sum();
            long endStream = System.currentTimeMillis();

            long startParallel = System.currentTimeMillis();
            long sumParallel = numbers.parallelStream()
                    .filter(n -> n % 2 == 0)
                    .map(n -> n * 2)
                    .mapToLong(Integer::longValue)
                    .sum();
            long endParallel = System.currentTimeMillis();

            System.out.println("Sequential Stream: " + (endStream - startStream) + " ms. Sum: " + sumStream);
            System.out.println("Parallel Stream: " + (endParallel - startParallel) + " ms. Sum: " + sumParallel);
        };
    }
}
