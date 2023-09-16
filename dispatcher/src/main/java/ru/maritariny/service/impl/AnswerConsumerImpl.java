package ru.maritariny.service.impl;

// Для считывания из брокера ответов, которые были отправлены из ноды

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.maritariny.controller.UpdateProcessor;
import ru.maritariny.service.AnswerConsumer;

import static ru.maritariny.model.RabbitQueue.ANSWER_MESSAGE;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {

    // Для дальнейшей передачи ответов в UpdateController
    private final UpdateProcessor updateProcessor;

    public AnswerConsumerImpl(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
