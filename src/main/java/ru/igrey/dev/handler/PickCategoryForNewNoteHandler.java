package ru.igrey.dev.handler;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;

import static ru.igrey.dev.constant.Delimiter.DELIMITER;

public class PickCategoryForNewNoteHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;

    public PickCategoryForNewNoteHandler(CallbackQuery query, NoteRepository noteRepository, CategoryRepository categoryRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String onClick() {
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        Long noteId = Long.valueOf(query.getData().split(DELIMITER)[2]);
        Note note = noteRepository.findById(noteId);
        note.setCategoryId(categoryId);
        noteRepository.saveNote(note);
        Category category = categoryRepository.findCategoryById(categoryId);
        BeanConfig.tellMeBot().editMessage(query.getMessage().getChatId(),
                query.getMessage().getMessageId(),
                category != null ? category.toString() : AnswerMessageText.CATEGORY_HAS_BEEN_DELETED.text(),
                ReplyKeyboard.buttonBackToCategoryView(categoryId));
        return AnswerMessageText.NOTE_IS_ADDED_IN_CATEGORY.text();
    }

}
