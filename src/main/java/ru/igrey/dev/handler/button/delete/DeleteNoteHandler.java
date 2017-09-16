package ru.igrey.dev.handler.button.delete;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.handler.button.ButtonHandler;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class DeleteNoteHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;

    public DeleteNoteHandler(CallbackQuery query, NoteRepository noteRepository, CategoryRepository categoryRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        Long noteId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[2]);
        deleteNote(chatId, query.getMessage().getMessageId(), noteId, categoryId);
        return AnswerMessageText.NOTE_IS_DELETED.text();
    }

    private void deleteNote(Long chatId, Integer messageId, Long noteId, Long categoryId) {
        noteRepository.delete(noteId);
        Category category = categoryRepository.findCategoryById(categoryId);
        BeanConfig.tellMeBot().editMessage(chatId,
                messageId,
                AnswerMessageText.PICK_NOTES_FOR_DELETE.text(),
                ReplyKeyboard.buttonsForPickingNotesForDelete(category.getNotes(), categoryId, category.getTitle())
        );
    }

}
