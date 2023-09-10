package ru.maritariny.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.maritariny.utils.CryptoTool;

// Класс нужен, чтобы подключить наш CryptoTool из common модуля в качестве спрингового бина
@Configuration
public class RestServiceConfiguration {
    @Value("${salt}") // достается значение из конфигурационного файла
    private String salt;

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }
}
