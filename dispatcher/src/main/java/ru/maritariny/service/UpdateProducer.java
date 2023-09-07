package ru.maritariny.service;

import org.telegram.telegrambots.meta.api.objects.Update;

// Для передачи update в rabbit mq
public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}
