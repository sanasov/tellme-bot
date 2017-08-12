package ru.igrey.dev;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by sanasov on 01.04.2017.
 */
public class TellMe extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TellMe.class);

    public TellMe() {

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            sendTextMessage(message.getChatId(), "Hello");
        }
    }

    private void sendTextMessage(Long chatId, String responseMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId)
                .setText(responseMessage);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return "@MindkeeperBot";
    }

    @Override
    public String getBotToken() {
        return "254626232:AAEO0aaj6ddVIfrPsOCEIkDa8i0y0rcc3k0";
    }

}

