package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;

import static ru.igrey.dev.constant.Delimiter.DELIMITER;

public class PickRemoveModeHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;


    public PickRemoveModeHandler(CallbackQuery query, NoteRepository noteRepository, CategoryRepository categoryRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String onClick() {
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        Long chatId = query.getMessage().getChatId();
        Category category = categoryRepository.findCategoryById(categoryId);
        if (category == null) {
            return "";
        }
        String answerMessage = (category.getNotes() == null || category.getNotes().isEmpty()) ? AnswerMessageText.EMPTY.text() : AnswerMessageText.PICK_NOTES_FOR_DELETE.text();
        BeanConfig.tellMeBot().editMessage(
                chatId,
                query.getMessage().getMessageId(),
                answerMessage,
                ReplyKeyboard.buttonsForPickingNotesForDelete(noteRepository.findByCategoryId(categoryId), categoryId, category.getTitle())
        );
        return "";
    }

}
