package ru.igrey.dev.handler.button.delete;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.service.TelegramUserService;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class DeleteCategoryHandler implements ButtonHandler {

    private CallbackQuery query;
    private CategoryRepository categoryRepository;
    private TelegramUser telegramUser;
    private TelegramUserService telegramUserService;

    public DeleteCategoryHandler(CallbackQuery query, CategoryRepository categoryRepository, TelegramUser telegramUser, TelegramUserService telegramUserService) {
        this.query = query;
        this.categoryRepository = categoryRepository;
        this.telegramUser = telegramUser;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public String onClick() {
        Long categoryId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        categoryRepository.deleteCategoryById(categoryId);
        backToViewCategories();
        return AnswerMessageText.CATEGORY_IS_DELETED.text() + "\n" + AnswerMessageText.BACK_TO_CATEGORY_VIEW.text();
    }

    private void backToViewCategories() {
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
            BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.NO_CATEGORIES_NO_NOTES.text(), null);
        } else {
            BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.PICK_CATEGORY_TO_VIEW_NOTES.text(), ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
        }
    }

}
