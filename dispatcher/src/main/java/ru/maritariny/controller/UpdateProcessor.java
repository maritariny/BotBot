package ru.maritariny.controller;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maritariny.service.UpdateProducer;
import ru.maritariny.utils.MessageUtils;

import static ru.maritariny.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateProcessor {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateProcessor(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }
    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    // Первичная валидация входящих данных
    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        // Может быть несколько видов сообщений:
        // - сообщения из приватных чатов
        // - отредактированные сообщения их приватных чатов
        // - сообщения из каналов/групп
        // - др.

        // Будут обрабатываться только первичные (неотредактированные) сообщения из приватных чатов
        if (update.hasMessage()) {
            distributeMessageByType(update);
        } else {
            log.error("Unsupported message type is received" + update);
            return;
        }
    }

    // Вернуть ответ: сгенерировать (создат объект класса SendMessage, поместить в него данные), передать его в тг-бот

    private void distributeMessageByType(Update update) {
        // Документ, фотография, текст - распределение брокером в зависимости от сообщения
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()){
            processDocumentMessage(update);
        } else if (message.hasPhoto()){
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения!");
        // прокси-метод, т.к. не всегда можно будет напрямую передать сообщение боту (например, из сервисов)
        setView(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается...");
        // прокси-метод, т.к. не всегда можно будет напрямую передать сообщение боту (например, из сервисов)
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

}
