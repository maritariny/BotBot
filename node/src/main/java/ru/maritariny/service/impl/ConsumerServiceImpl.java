package ru.maritariny.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maritariny.service.ConsumerService;
import ru.maritariny.service.ProducerService;
import ru.maritariny.service.MainService;

import static ru.maritariny.model.RabbitQueue.*;

// RabbitMQ работает по принципу Push message - брокер сам сообщения проталкивает нужным слушателям в нашем приложении
// В кафке PULL message - сами запрашивают у брокера новые сообщения
@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {

    //private final ProducerService producerService;
    private final MainService mainService;


    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processTextMessage(update);

        // Код ниже перенесен в mainService. Вместо него строка что выше
//        var message = update.getMessage();
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId().toString());
//        sendMessage.setText("Hello from NODE");
//        producerService.producerAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");
      //  mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");
      //  mainService.processPhotoMessage(update);
    }
}
