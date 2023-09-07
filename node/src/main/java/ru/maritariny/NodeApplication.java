package ru.maritariny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Нода не взаимодействует напрямую с телеграмом

@SpringBootApplication
public class NodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NodeApplication.class);
    }
}
