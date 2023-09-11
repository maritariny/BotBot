package ru.maritariny.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.maritariny.service.UserActivationService;

@Log4j
@RequestMapping("/user")
@RestController // означает, что в ответ вернем роут дата
public class ActivationController {
    private final UserActivationService userActivationService;

    public ActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        var res = userActivationService.activation(id);
        if (res) {
            return ResponseEntity.ok().body("Регистрация успешно завершена");
        }
        return ResponseEntity.internalServerError().build();
    }
}
