package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
public class PoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoolApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            List<String> urls = List.of(
                    "https://google.com", "https://github.com", "https://yandex.ru",
                    "https://bing.com", "https://apple.com", "https://microsoft.com",
                    "https://amazon.com", "https://wikipedia.org", "https://reddit.com",
                    "https://cloudflare.com", "https://stackoverflow.com", "https://python.org"
            );

            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    4,
                    8,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(50),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

            System.out.println("--- ЗАПУСК ПРОВЕРКИ API ---");

            for (String url : urls) {
                executor.execute(() -> {
                    long start = System.currentTimeMillis();
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(3000);
                        int code = connection.getResponseCode();
                        long duration = System.currentTimeMillis() - start;
                        System.out.printf("URL: %-25s | Status: %d | Time: %dms | Thread: %s%n",
                                url, code, duration, Thread.currentThread().getName());
                    } catch (Exception e) {
                        System.err.println("URL: " + url + " | Error: " + e.getMessage());
                    }
                });
            }

            executor.shutdown();
            if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("--- ВСЕ ЗАДАЧИ ВЫПОЛНЕНЫ ---");
            }
        };
    }
}