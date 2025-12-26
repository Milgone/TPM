package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LifecycleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifecycleApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            Object lock = new Object();

            Thread mainWorker = new Thread(() -> {
                try {
                    Thread.sleep(200);
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "MainWorker");

            Thread blocker = new Thread(() -> {
                synchronized (lock) {
                    System.out.println("Blocker захватил монитор");
                }
            }, "Blocker-Thread");

            System.out.println("Состояние NEW: " + mainWorker.getState());

            mainWorker.start();
            System.out.println("Состояние RUNNABLE: " + mainWorker.getState());

            Thread.sleep(100);
            System.out.println("Состояние TIMED_WAITING: " + mainWorker.getState());

            synchronized (lock) {
                blocker.start();
                Thread.sleep(100);
                System.out.println("Состояние BLOCKED: " + blocker.getState());

                lock.notify();
            }

            Thread.sleep(100);
            System.out.println("Состояние WAITING (до notify): " + mainWorker.getState());

            mainWorker.join();
            blocker.join();
            System.out.println("Состояние TERMINATED: " + mainWorker.getState());
        };
    }
}
