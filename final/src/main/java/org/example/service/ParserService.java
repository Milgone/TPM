package org.example.service;

import org.example.dto.CurrencyRateDto;
import org.example.model.CurrencyRate;
import org.example.repository.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ParserService {

    private static final Logger log = LoggerFactory.getLogger(ParserService.class);

    private final CurrencyRepository repository;
    private final WebClient webClient = WebClient.create();
    private final ExecutorService executor;
    private final List<String> symbols = List.of("BTC", "ETH", "SOL", "BNB", "ADA");

    public ParserService(CurrencyRepository repository) {
        this.repository = repository;
        this.executor = Executors.newFixedThreadPool(5, daemonThreadFactory());
    }

    private ThreadFactory daemonThreadFactory() {
        return runnable -> {
            Thread t = new Thread(runnable);
            t.setName("currency-parser-" + t.getId());
            t.setDaemon(true);
            return t;
        };
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleParsing() {
        log.info("Starting scheduled parsing for symbols: {}", symbols);
        symbols.forEach(this::fetchAndSaveAsync);
    }

    public void fetchAndSaveAsync(String symbol) {
        CompletableFuture.runAsync(() -> {
            try {
                // Пример обращения к стороннему сервису (можно заменить на реальный URL)
                // Здесь мы просто демонстрируем вызов, результат не используем, чтобы не зависеть от сети на защите
                webClient.get()
                        .uri("https://postman-echo.com/get?symbol={symbol}", symbol)
                        .retrieve()
                        .bodyToMono(String.class)
                        .doOnError(error -> log.warn("External service call failed for {}: {}", symbol, error.getMessage()))
                        .subscribe(body -> log.debug("External service response for {}: {}", symbol, body));

                Double mockPrice = 20000.0 + (ThreadLocalRandom.current().nextDouble() * 5000);
                CurrencyRate rate = new CurrencyRate(symbol, mockPrice, "+1.5%", LocalDateTime.now());
                repository.save(rate);
                log.info("Saved currency rate: {} = {}", symbol, mockPrice);
            } catch (Exception e) {
                log.error("Error during parsing for {}: {}", symbol, e.getMessage(), e);
            }
        }, executor);
    }

    public List<CurrencyRate> getFilteredRates(String name) {
        return repository.findAll().parallelStream()
                .filter(r -> name == null || name.isBlank() || r.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<CurrencyRateDto> getFilteredRateDtos(String name) {
        return getFilteredRates(name).stream()
                .map(ParserService::toDto)
                .toList();
    }

    public static CurrencyRateDto toDto(CurrencyRate rate) {
        return new CurrencyRateDto(
                rate.getName(),
                rate.getPrice(),
                rate.getChangeDay(),
                rate.getCaptureDate()
        );
    }
}
