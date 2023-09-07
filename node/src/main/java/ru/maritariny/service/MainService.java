package ru.maritariny.service;

import org.telegram.telegrambots.meta.api.objects.Update;

// Сервис для обработки входящих сообщений. Дальше будут вызываться специфические серисы в зависимости от типа сообщения
public interface MainService {
    void processTextMessage(Update update);
    void processDocMessage(Update update);
    void processPhotoMessage(Update update);
}
