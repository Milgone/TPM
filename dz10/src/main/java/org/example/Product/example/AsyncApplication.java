package org.example;

import org.example.model.ProductResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class AsyncApplication {

    private final ExecutorService customExecutor = Executors.newFixedThreadPool(5, r -> {
        Thread t = new Thread(r);
        t.setName("Async-Worker-" + t.hashCode());
        t.setDaemon(true);
        return t;
    });

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            List<String> ids = List.of("P-101", "P-102", "P-103", "P-ERR", "P-105");

            long start = System.currentTimeMillis();
            List<ProductResult> results = fetchAllDataAsync(ids);
            long end = System.currentTimeMillis();

            System.out.println("--- ИТОГОВЫЕ РЕЗУЛЬТАТЫ ---");
            results.forEach(System.out::println);
            System.out.println("Время выполнения: " + (end - start) + " ms");

            customExecutor.shutdown();
        };
    }

    public List<ProductResult> fetchAllDataAsync(List<String> ids) {
        List<CompletableFuture<ProductResult>> futures = ids.stream()
                .map(id -> {
                    CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> fetchName(id), customExecutor);
                    CompletableFuture<Double> priceFuture = CompletableFuture.supplyAsync(() -> fetchPrice(id), customExecutor);

                    return nameFuture.thenCombineAsync(priceFuture, (name, price) -> {
                                System.out.println("[" + Thread.currentThread().getName() + "] Комбинирование для " + id);
                                return new ProductResult(id, name, price, "Validated");
                            }, customExecutor)
                            .thenApply(res -> new ProductResult(res.id(), res.name().toUpperCase(), res.price(), res.info()))
                            .handle((res, ex) -> {
                                if (ex != null) {
                                    System.err.println("Ошибка для " + id + ": " + ex.getMessage());
                                    return new ProductResult(id, "Error_Stub", 0.0, "Failed");
                                }
                                return res;
                            });
                })
                .collect(Collectors.toList());

        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private String fetchName(String id) {
        sleep(500);
        if (id.equals("P-ERR")) throw new RuntimeException("DB Crash");
        return "Product_" + id;
    }

    private double fetchPrice(String id) {
        sleep(300);
        return 100.0 + Math.random() * 500;
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}