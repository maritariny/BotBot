package ru.maritariny.service;

import org.springframework.core.io.FileSystemResource;
import ru.maritariny.entity.AppDocument;
import ru.maritariny.entity.AppPhoto;
import ru.maritariny.entity.BinaryContent;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);

    // преобразование массива байт в объект FileSystemResource, который необходим для передачи контента в теле http-ответа
    //FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
