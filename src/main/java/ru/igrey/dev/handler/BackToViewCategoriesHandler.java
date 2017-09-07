package ru.igrey.dev.handler;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.service.TelegramUserService;

public class BackToViewCategoriesHandler implements ButtonHandler {

    private CallbackQuery query;
    private TelegramUserService telegramUserService;

    public BackToViewCategoriesHandler(CallbackQuery query, TelegramUserService telegramUserService) {
        this.query = query;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public String onClick() {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
            BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.NO_CATEGORIES_NO_NOTES.text(), null);
        } else {
            BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.IN_WHICH_CATEGORY.text(), ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
        }
        return AnswerMessageText.BACK_TO_CATEGORY_VIEW.text();
    }

}
