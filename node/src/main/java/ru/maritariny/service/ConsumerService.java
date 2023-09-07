package ru.maritariny.service;

import org.telegram.telegrambots.meta.api.objects.Update;

// Для считывания сообщений от брокера
public interface ConsumerService {
    void consumeTextMessageUpdates(Update update);
    void consumeDocMessageUpdates(Update update);
    void consumePhotoMessageUpdates(Update update);
}
