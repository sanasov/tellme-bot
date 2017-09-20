package ru.igrey.dev.handler.button.menu;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.keyboard.ReplyKeyboard;

public class SettingsLanguageHandler implements ButtonHandler {

    private CallbackQuery query;

    public SettingsLanguageHandler(CallbackQuery query) {
        this.query = query;
    }

    @Override
    public String onClick() {
        BeanConfig.tellMeBot().editMessage(query.getMessage().getChatId(), query.getMessage().getMessageId(), AnswerMessageText.PICK_LANGUAGE.text(), ReplyKeyboard.buttonsForPickingLanguage());
        return "";
    }

}
