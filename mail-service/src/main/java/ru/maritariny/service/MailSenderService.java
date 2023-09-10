package ru.maritariny.service;

import ru.maritariny.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
