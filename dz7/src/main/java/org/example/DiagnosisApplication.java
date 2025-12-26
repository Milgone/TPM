package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DiagnosisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiagnosisApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            Object resA = "Resource A";
            Object resB = "Resource B";

            System.out.println("PID: " + ProcessHandle.current().pid());

            new Thread(() -> {
                synchronized (resA) {
                    try { Thread.sleep(100); } catch (Exception e) {}
                    synchronized (resB) { System.out.println("T1 got both"); }
                }
            }, "Deadlock-Thread-1").start();

            new Thread(() -> {
                synchronized (resB) {
                    try { Thread.sleep(100); } catch (Exception e) {}
                    synchronized (resA) { System.out.println("T2 got both"); }
                }
            }, "Deadlock-Thread-2").start();
        };
    }
}