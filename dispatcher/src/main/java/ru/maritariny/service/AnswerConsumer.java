package ru.maritariny.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

// Для передачи ответа от rabbit mq в UpdateController
public interface AnswerConsumer {

    // В отличие от UpdateProducer Имя очереди будет указываться в самом сервисе с помощью аннотации
    void consume(SendMessage sendMessage);
}
