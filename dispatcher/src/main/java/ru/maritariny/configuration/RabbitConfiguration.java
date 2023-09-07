package ru.maritariny.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.maritariny.model.RabbitQueue.*;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        // преобразовывает апдейты (Update) в json для передачи в рэббит
        // при получении апдейтов обратно в приложение будет преобразовывать в java-объект
        // Руками метод не вызывается, но если его не объявить, то Spring-приложение либо упадет, либо не стартанет
        return new Jackson2JsonMessageConverter();
    }

    // очереди создадутся автоматически
    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue docMessageQueue() {
        return new Queue(DOC_MESSAGE_UPDATE);
    }

    @Bean
    public Queue photoMessageQueue() {
        return new Queue(PHOTO_MESSAGE_UPDATE);
    }

    @Bean
    public Queue answerMessageQueue() {
        // для ответов от нод, которые адресованы диспатчеру и далее конечному пользователю
        return new Queue(ANSWER_MESSAGE);
    }


}
