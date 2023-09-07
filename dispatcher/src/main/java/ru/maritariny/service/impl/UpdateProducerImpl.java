package ru.maritariny.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maritariny.service.UpdateProducer;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {

    // Внедряется бин RabbitTemplate, подтягивается изнутри зависимости (стартера spring-boot-starter-amqp)
    // Спринг при старте создаст этот бин
    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText()); // Заглушка
        rabbitTemplate.convertAndSend(rabbitQueue, update); // Имя очереди и update, который преобразуется в json
        // Если в конфигруационном файле (RabbitConfiguration) не описать json конвертер (jsonMessageConverter),
        // то была бы ошибка в этой строке
    }
}
