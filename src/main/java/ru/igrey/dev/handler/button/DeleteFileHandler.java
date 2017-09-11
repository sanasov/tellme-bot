package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.dao.repository.NoteRepository;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class DeleteFileHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;

    public DeleteFileHandler(CallbackQuery query, NoteRepository noteRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
    }

    @Override
    public String onClick() {
        Long noteId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        noteRepository.delete(noteId);
        return AnswerMessageText.NOTE_IS_DELETED.text();
    }

}
