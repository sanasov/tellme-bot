package ru.igrey.dev.handler.button.menu;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.Emoji;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.keyboard.ReplyKeyboard;

@Slf4j
public class DonateHandler implements ButtonHandler {

    private CallbackQuery query;
    private static String catPhotoId = "AgADAgADDagxG38LEEr98DmE7kONXJsSSw0ABIaQrqaaH1BDQsMPAAEC";

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
                Emoji.DONATE.toString(),
                null
        );
        BeanConfig.tellMeBot().sendButtonMessage(
                chatId,
                AnswerMessageText.DONATE.text(),
                ReplyKeyboard.donateLinkAndBackToMenuButtons()
        );
        return "";
    }

}
