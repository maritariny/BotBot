package ru.maritariny.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maritariny.dao.RawDataDAO;
import ru.maritariny.entity.RawData;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class MainServiceImplTest {
    @Autowired
    private RawDataDAO rawDataDAO;

    @Test
    public void testSaveRawData() {
        // проверка, что id поменялся за время жизни, т.е. у класса RawData неправильно работает хеш-функция, т.к. она зависит
        // от изменяемого в течение жизни объекта поля id
        Update update = new Update();
        Message msg = new Message();
        msg.setText("ololo");
        update.setMessage(msg);

        RawData rawData = RawData.builder()
                .event(update)
                .build();
        Set<RawData> testData = new HashSet<>();

        testData.add(rawData); // помещение объекта класса с хешем, сформированным на основе пустого id, т.к. в конструктор
        // передавалось только значение поля update
        rawDataDAO.save(rawData); // тут будет установлено значение id средствами бд

        Assert.isTrue(testData.contains(rawData), "Entity not found in the set");

    }

}