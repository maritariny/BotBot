package ru.maritariny.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maritariny.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
@RequestMapping("/file") // подойдет для ссылок http://localhost:8086/file/get-doc, http://localhost:8086/file/get-photo
@RestController // означает, что в ответ вернем роут дата
public class FileController {
    private final FileService fileServive;

    public FileController(FileService fileServive) {
        this.fileServive = fileServive;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public void getDoc(@RequestParam("id") String id, HttpServletResponse response) { // второй парамет добавили для перехвата респонса (доработка по удалению временных файлов, точнее они больше не используются)
        // возвращаемое значение ResponseEntity - это спринговый класс-билдер, к-ый помогает удобно собрать http-ответ
        // @RequestParam() - описывает параметры, которые могут прийти в гет-запросе
        //TODO для формирования badRequest добавить ControllerAdvice (для формирования детальных ошибок и детализированных ответов пользователю)
        var doc = fileServive.getDocument(id);
        if (doc == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //return ResponseEntity.badRequest().build(); // 400 - Закомментировано, т.е. респонс перехвачен выше
            return;
        }
        response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString()); // добавление хедера с типом содержимого (по-новому для респонса)
        response.setHeader("Content-disposition", "attachment; filename=" + doc.getDocName());// указывает клиенту (барузеру) как именно воспринимать информацию (attachment = скачать файл
        // если его не добавить, то изображение или документ сразу откроются в окне браузера)
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = doc.getBinaryContent();

        try {
            var out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
        }

        // Старый вариант с временными файлами
//        var fileSystemResource = fileServive.getFileSystemResource(binaryContent);
//
//        if (fileSystemResource == null) {
//            return ResponseEntity.internalServerError().build(); // 500 документ найден в базе, но почему-то не смогли его вернуть. Ошибка на стороне сервера
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(doc.getMimeType())) // добавление хедера с типом содержимого
//                .header("Content-disposition", "attachment; filename=" + doc.getDocName())// указывает клиенту (барузеру) как именно воспринимать информацию (attachment = скачать файл
//                // если его не добавить, то изображение или документ сразу откроются в окне браузера)
//                .body(fileSystemResource);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        var photo = fileServive.getPhoto(id);
        if (photo == null) {
            //return ResponseEntity.badRequest().build(); // 400
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.IMAGE_JPEG.toString()); // добавление хедера с типом содержимого (по-новому для респонса)
        response.setHeader("Content-disposition", "attachment;");// указывает клиенту (барузеру) как именно воспринимать информацию (attachment = скачать файл
        // если его не добавить, то изображение или документ сразу откроются в окне браузера)
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = photo.getBinaryContent();

        try {
            var out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
        }


//        var fileSystemResource = fileServive.getFileSystemResource(binaryContent);
//
//        if (fileSystemResource == null) {
//            return ResponseEntity.internalServerError().build(); // 500 документ найден в базе, но почему-то не смогли его вернуть. Ошибка на стороне сервера
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG) // добавление хедера с типом содержимого
//                .header("Content-disposition", "attachment;")// указывает клиенту (барузеру) как именно воспринимать информацию (attachment = скачать файл
//                // если его не добавить, то изображение или документ сразу откроются в окне браузера)
//                .body(fileSystemResource);
    }
}
