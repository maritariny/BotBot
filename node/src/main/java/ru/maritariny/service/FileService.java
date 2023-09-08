package ru.maritariny.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.maritariny.entity.AppDocument;
//import ru.maritariny.entity.AppPhoto;
//import ru.maritariny.service.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    //AppPhoto processPhoto(Message telegramMessage);
    //String generateLink(Long docId, LinkType linkType);
}
