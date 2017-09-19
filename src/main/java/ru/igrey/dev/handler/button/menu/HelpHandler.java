package ru.igrey.dev.handler.button.menu;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.handler.button.ButtonHandler;

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
                AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY.text() + "\n" + AnswerMessageText.NOTIFICATION_INSTRUCTION.text() + "\n\n" + AnswerMessageText.FORMAT.text(),
                null
        );
        BeanConfig.tellMeBot().sendInstruction(chatId);
        return "";
    }

}
