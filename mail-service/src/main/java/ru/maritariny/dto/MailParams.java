package ru.maritariny.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// Класс нужен для того, чтобы при получении запроса в контроллере спринг сразу смог смапить входящий json в наш java-класс
// В итоге в коде получим объект, у которого поля будут содержать значения полей из входящего запроса
// Заполнение этих полей возьмет на себя спринг
// Далее этот объект мы передадим из контроллера в сервис для дальнейшей обработки запроса
@Getter
@Setter
@RequiredArgsConstructor
public class MailParams {

    private String id;
    private String emailTo;
}
