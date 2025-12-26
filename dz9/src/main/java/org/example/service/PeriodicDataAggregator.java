package org.example.service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeriodicDataAggregator {
    private final List<String> entities = List.of("BTC", "ETH", "SOL", "DOT");
    private final ExecutorService workerPool = Executors.newFixedThreadPool(4);
    private final Random random = new Random();

    public void aggregate() {
        System.out.println("--- Запуск агрегации данных ---");
        for (String entity : entities) {
            workerPool.submit(() -> {
                try {
                    String data = fetchData(entity);
                    System.out.println("Агрегатор сохранил: " + data);
                } catch (Exception e) {
                    System.err.println("Ошибка для " + entity + ": " + e.getMessage());
                }
            });
        }
    }

    private String fetchData(String id) throws Exception {
        Thread.sleep(500);
        if (random.nextInt(10) < 2) throw new Exception("Сбой сети");
        return id + ": " + (40000 + random.nextInt(5000));
    }
}