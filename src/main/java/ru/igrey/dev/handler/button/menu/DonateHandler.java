package ru.igrey.dev.handler.button.menu;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.keyboard.ReplyKeyboard;

@Slf4j
public class DonateHandler implements ButtonHandler {

    private CallbackQuery query;
    private static String catPhotoId = "AgADAgADNqgxG087AUoTQCqp0qNISJs2Sw0ABKqE_vjU0wMJ2dIPAAEC";

    public DonateHandler(CallbackQuery query) {
        this.query = query;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setPhoto(catPhotoId);
        try {
            BeanConfig.tellMeBot().sendPhoto(photo);
        } catch (TelegramApiException e) {
            log.error("Could not send DONATE cat photo", e);
        }
        BeanConfig.tellMeBot().editMessage(
                chatId,
                query.getMessage().getMessageId(),
                AnswerMessageText.DONATE.text(),
                ReplyKeyboard.backToMenuButton()
        );
        return "";
    }

}
