package ru.maritariny.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.maritariny.service.ProducerService;

import static ru.maritariny.model.RabbitQueue.ANSWER_MESSAGE;

@Service
public class ProducerServiceImpl implements ProducerService {
    // Отличие: очередь захардкожена в срвисе, т.к. очередь всего одна и нет смысла передавать ее в качестве аргумента

    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void producerAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}
