package ru.maritariny.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Log4j
//public class TelegramBot extends TelegramLongPollingBot {
public class TelegramBot extends TelegramWebhookBot {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.uri}")
    private String botUri;

    //private static final Logger log = Logger.getLogger(TelegramBot.class);
    private UpdateProcessor updateProcessor;

    public TelegramBot(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() {
        updateProcessor.registerBot(this);
        try {
            var setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            this.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        // Итоговый адрес будет такой: <bot.uri> + "/callback" + "/update"
        return "/update";
    }

//    @Override
//    public void onUpdateReceived(Update update) {
//        //var originalMessage = update.getMessage();
//        //System.out.println(originalMessage.getText());
//        //log.debug(originalMessage.getText());
//
////        var response = new SendMessage();
////        response.setChatId(originalMessage.getChatId().toString());
////        response.setText("Hello from bot");
////        sendAnswerMessage(response);
//        updateProcessor.processUpdate(update);
//    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e){
                log.error(e);
            }
        }
    }



}
