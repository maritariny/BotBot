package ru.maritariny.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maritariny.service.FileService;

@Log4j
@RequestMapping("/file") // подойдет для ссылок http://localhost:8086/file/get-doc, http://localhost:8086/file/get-photo
@RestController // означает, что в ответ вернем роут дата
public class FileController {
    private final FileService fileServive;

    public FileController(FileService fileServive) {
        this.fileServive = fileServive;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        // возвращаемое значение ResponseEntity - это спринговый класс-билдер, к-ый помогает удобно собрать http-ответ
        // @RequestParam() - описывает параметры, которые могут прийти в гет-запросе
        //TODO для формирования badRequest добавить ControllerAdvice (для формирования детальных ошибок и детализированных ответов пользователю)
        var doc = fileServive.getDocument(id);
        if (doc == null) {
            return ResponseEntity.badRequest().build(); // 400
        }
        var binaryContent = doc.getBinaryContent();
        var fileSystemResource = fileServive.getFileSystemResource(binaryContent);

        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build(); // 500 документ найден в базе, но почему-то не смогли его вернуть. Ошибка на стороне сервера
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType())) // добавление хедера с типом содержимого
                .header("Content-disposition", "attachment; filename=" + doc.getDocName())// указывает клиенту (барузеру) как именно воспринимать информацию (attachment = скачать файл
                // если его не добавить, то изображение или документ сразу откроются в окне браузера)
                .body(fileSystemResource);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        //TODO для формирования badRequest добавить ControllerAdvice
        var photo = fileServive.getPhoto(id);
        if (photo == null) {
            return ResponseEntity.badRequest().build(); // 400
        }
        var binaryContent = photo.getBinaryContent();
        var fileSystemResource = fileServive.getFileSystemResource(binaryContent);

        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build(); // 500 документ найден в базе, но почему-то не смогли его вернуть. Ошибка на стороне сервера
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // добавление хедера с типом содержимого
                .header("Content-disposition", "attachment;")// указывает клиенту (барузеру) как именно воспринимать информацию (attachment = скачать файл
                // если его не добавить, то изображение или документ сразу откроются в окне браузера)
                .body(fileSystemResource);
    }
}
