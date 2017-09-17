package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.domain.Category;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class PickCategoryToViewFileHandler implements ButtonHandler {

    private CallbackQuery query;
    private CategoryRepository categoryRepository;

    public PickCategoryToViewFileHandler(CallbackQuery query, CategoryRepository categoryRepository) {
        this.query = query;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        Category category = categoryRepository.findCategoryById(categoryId);
        BeanConfig.tellMeBot().editMessage(chatId,
                query.getMessage().getMessageId(),
                category.getTitle(),
                ReplyKeyboard.buttonsForPickingFileForView(category));
        return "";
    }

}
