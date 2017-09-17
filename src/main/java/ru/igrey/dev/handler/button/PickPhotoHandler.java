package ru.igrey.dev.handler.button;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Note;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;
@Slf4j
public class PickPhotoHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;

    public PickPhotoHandler(CallbackQuery query, NoteRepository noteRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Long noteId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[2]);
        Long categoryId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        Note note = noteRepository.findById(noteId);
        SendPhoto photo = new SendPhoto();
        photo.setPhoto(note.getText());
        photo.setCaption(note.getCaption());
        photo.setChatId(chatId);
        photo.setReplyMarkup(ReplyKeyboard.buttonViewFileCategoryDeleteFile(categoryId, noteId));
        try {
            BeanConfig.tellMeBot().sendPhoto(photo);
        } catch (TelegramApiException e) {
            log.error("Could not send photo", e);
        }
        return "";
    }

}
