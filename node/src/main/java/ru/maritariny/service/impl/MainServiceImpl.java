package ru.maritariny.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.maritariny.dao.AppUserDAO;
import ru.maritariny.dao.RawDataDAO;

import ru.maritariny.entity.AppDocument;
import ru.maritariny.entity.AppPhoto;
import ru.maritariny.entity.AppUser;
import ru.maritariny.entity.RawData;
import ru.maritariny.exceptions.UploadFileException;
import ru.maritariny.service.AppUserService;
import ru.maritariny.service.FileService;
import ru.maritariny.service.MainService;
import ru.maritariny.service.ProducerService;
import ru.maritariny.service.enums.LinkType;
import ru.maritariny.service.enums.ServiceCommand;

import static ru.maritariny.entity.enums.UserState.BASIC_STATE;
import static ru.maritariny.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static ru.maritariny.service.enums.ServiceCommand.*;


@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;
    private final FileService fileService;
    private final AppUserService appUserService;

    public MainServiceImpl(RawDataDAO rawDataDAO,
                           ProducerService producerService,
                           AppUserDAO appUserDAO,
                           FileService fileService, AppUserService appUserService) {

        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.fileService = fileService;
        this.appUserService = appUserService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        var serviceCommand = ServiceCommand.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            // Отменяет текущую команду и сбрасывает state пользователя к базовому состоянию
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            // Ожидание ввода сервисных команд
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            output = appUserService.setEmail(appUser, text);
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);

//        var message = update.getMessage();
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId().toString());
//        sendMessage.setText("Hello from NODE");
//        producerService.producerAnswer(sendMessage);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
       // var answer = "Документ успешно загружен! Ссылка для скачивания";
       // sendAnswer(answer, chatId);
        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            // Генерация ссылки для скачивания документа
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
            //String link = "ololo";
            var answer = "Документ успешно загружен! "
                                + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        //var answer = "Фото успешно загружено! Ссылка для скачивания";
        //sendAnswer(answer, chatId);
        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            // Генерация ссылки для скачивания фото
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
            //String link = "ololoshka";
            var answer = "Фото успешно загружено! "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        var serviceCommand = ServiceCommand.fromValue(cmd);
        if (REGISTRATION.equals(serviceCommand)) {
            return appUserService.registerUser(appUser);
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
            return "Приветствую! Чтобы посмотреть список доступных команд, введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд, введите /help";
        }
    }

    private String help() {
        return """
                Список доступных команд:
                /cancel - отмена выполнения текущей команды;
                /registration - регистрация пользователя.""";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";
    }

    private AppUser findOrSaveAppUser(Update update) {
        // persistent = Объект есть в БД, имеет заполненный первичный ключ и связан с сессией Hibernate
        // transient = Объекта еще нет в БД, необходимо его сохранить
        User telegramUser = update.getMessage().getFrom();
        //AppUser persistentAppUser = appUserDAO.findByTelegramUserId(telegramUser.getId());
        var optional = appUserDAO.findByTelegramUserId(telegramUser.getId());
        if (optional.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser); // Записывает в базу, заполняет ключ, привязывает объект к сессии Hibernate
        }
        return optional.get();
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update) // Вызывается сеттер
                .build();
        rawDataDAO.save(rawData);
    }
}
