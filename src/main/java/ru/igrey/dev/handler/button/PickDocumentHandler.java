package ru.igrey.dev.handler.button;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Note;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

@Slf4j
public class PickDocumentHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;

    public PickDocumentHandler(CallbackQuery query, NoteRepository noteRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Long noteId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[2]);
        Long categoryId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        Note note = noteRepository.findById(noteId);
        SendDocument document = new SendDocument();
        document.setDocument(note.getText());
        document.setCaption(note.getCaption());
        document.setChatId(chatId);
        document.setReplyMarkup(ReplyKeyboard.buttonViewFileCategoryDeleteFile(categoryId, noteId));
        try {
            BeanConfig.tellMeBot().sendDocument(document);
        } catch (TelegramApiException e) {
            log.error("Could not send document", e);
        }
        return "";
    }

}
