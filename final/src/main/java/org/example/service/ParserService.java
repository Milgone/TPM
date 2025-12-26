package org.example.service;

import org.example.model.CurrencyRate;
import org.example.repository.CurrencyRepository; // ВОТ ЭТА СТРОКА РЕШАЕТ ВСЁ
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ParserService {
    private final CurrencyRepository repository;
    private final WebClient webClient = WebClient.create();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final List<String> symbols = List.of("BTC", "ETH", "SOL", "BNB", "ADA");

    public ParserService(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleParsing() {
        symbols.forEach(this::fetchAndSaveAsync);
    }

    public void fetchAndSaveAsync(String symbol) {
        CompletableFuture.runAsync(() -> {
            try {
                Double mockPrice = 20000.0 + (ThreadLocalRandom.current().nextDouble() * 5000);
                CurrencyRate rate = new CurrencyRate(symbol, mockPrice, "+1.5%", LocalDateTime.now());
                repository.save(rate);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }, executor);
    }

    public List<CurrencyRate> getFilteredRates(String name) {
        return repository.findAll().parallelStream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .toList();
    }
}