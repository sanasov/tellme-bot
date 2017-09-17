package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.Localization;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.Language;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.service.TelegramUserService;

import java.util.List;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class LanguageHandler implements ButtonHandler {

    private CallbackQuery query;
    private TelegramUser telegramUser;
    private TelegramUserService telegramUserService;

    public LanguageHandler(CallbackQuery query, TelegramUserService telegramUserService, TelegramUser telegramUser) {
        this.query = query;
        this.telegramUserService = telegramUserService;
        this.telegramUser = telegramUser;
    }

    @Override
    public String onClick() {
        Integer messageId = query.getMessage().getMessageId();
        Long chatId = query.getMessage().getChatId();
        Language language = Language.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        telegramUser.setLanguage(language.name());
        Localization.set(language);
        if (telegramUser.getTimezone() == null) {
            telegramUser.setStatus(UserStatus.SET_TIMEZONE);
            BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.SET_TIMEZONE.text(), null);
        } else {
            onShowCategories(telegramUser.getCategories(), chatId, messageId);
        }
        telegramUserService.save(telegramUser);
        return language.title();
    }

    public void onShowCategories(List<Category> categories, Long chatId, Integer messageId) {
        if (categories == null || categories.isEmpty()) {
            BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.NO_CATEGORIES_NO_NOTES.text(), null);
            return;
        }
        BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.PICK_CATEGORY_TO_VIEW_NOTES.text(), ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
    }

}
