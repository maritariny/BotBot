package ru.maritariny.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

// рест-контроллер, который принимает запросы от телеграма
@RestController
public class WebHookController {
    private final UpdateProcessor updateProcessor;

    public WebHookController(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @RequestMapping(value = "/callback/update", method = RequestMethod.POST)
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        updateProcessor.processUpdate(update);
        return ResponseEntity.ok().build(); // ошибки не обрабатываются, т.к. спринг сам ее отловит и отправит ответ 500
        // либо можно реализовать контроллер адвайс для формирования обработки ошибок (фича спринга)
    }
}
