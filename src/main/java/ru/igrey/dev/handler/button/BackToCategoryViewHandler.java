package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.domain.TelegramUser;

public class BackToCategoryViewHandler implements ButtonHandler {

    private CallbackQuery query;
    private TelegramUser telegramUser;

    public BackToCategoryViewHandler(CallbackQuery query, TelegramUser telegramUser) {
        this.query = query;
        this.telegramUser = telegramUser;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        BeanConfig.tellMeBot().editMessage(
                chatId,
                query.getMessage().getMessageId(),
                AnswerMessageText.PICK_CATEGORY_TO_VIEW_NOTES.text(),
                ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories())
        );
        return "";
    }

}
