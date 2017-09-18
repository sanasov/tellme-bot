package ru.igrey.dev.handler.button.menu;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.keyboard.ReplyKeyboard;

public class HelpHandler implements ButtonHandler {

    private CallbackQuery query;

    public HelpHandler(CallbackQuery query) {
        this.query = query;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        BeanConfig.tellMeBot().editMessage(
                chatId,
                query.getMessage().getMessageId(),
                AnswerMessageText.MENU.text(),
                ReplyKeyboard.menuButtons()
        );
        return "";
    }

}
