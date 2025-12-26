package org.example.collector;

import java.util.HashSet;
import java.util.Set;

public class DataCollector {
    private int processedCount = 0;
    private final Set<String> processedKeys = new HashSet<>();
    private final int capacity = 10;

    public synchronized void collectItem(String key) throws InterruptedException {
        while (processedKeys.size() >= capacity) {
            wait();
        }
        if (!isAlreadyProcessed(key)) {
            processedKeys.add(key);
            incrementProcessed();
            System.out.println(Thread.currentThread().getName() + " добавил: " + key + ". Всего: " + processedCount);
            notifyAll();
        }
    }

    public synchronized void processBatch() throws InterruptedException {
        while (processedKeys.isEmpty()) {
            wait();
        }
        processedKeys.clear();
        System.out.println(Thread.currentThread().getName() + " очистил батч.");
        notifyAll();
    }

    private synchronized void incrementProcessed() {
        processedCount++;
    }

    private synchronized boolean isAlreadyProcessed(String key) {
        return processedKeys.contains(key);
    }
}