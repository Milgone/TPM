package org.example.parser.service;

import org.example.parser.model.CurrencyTask;
import org.springframework.stereotype.Service;

@Service
public class ParserService {

    public void runLogic() {
        // Создаем и запускаем поток через Thread
        Thread loggerThread = new Thread(() -> {
            System.out.println("Инфо: " + Thread.currentThread().getName() + " мониторит сеть...");
        }, "LoggerThread");

        // Создаем и запускаем поток через Runnable
        Thread counterWorker = new Thread(new CurrencyTask("BTC/USD", 1), "CounterWorker");

        loggerThread.start();
        counterWorker.start();
    }

    public void showThreads() {
        System.out.println("\n--- Список активных потоков ---");
        Thread.getAllStackTraces().keySet().forEach(t ->
                System.out.printf("Name: %-25s | State: %s%n", t.getName(), t.getState())
        );
    }
}