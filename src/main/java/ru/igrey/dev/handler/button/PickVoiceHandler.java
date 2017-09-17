package ru.igrey.dev.handler.button;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Note;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

@Slf4j
public class PickVoiceHandler implements ButtonHandler {

    private CallbackQuery query;
    private NoteRepository noteRepository;

    public PickVoiceHandler(CallbackQuery query, NoteRepository noteRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        Long noteId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[2]);
        Note note = noteRepository.findById(noteId);
        SendVoice voice = new SendVoice();
        voice.setVoice(note.getText());
        voice.setCaption(note.getCaption());
        voice.setChatId(chatId);
        voice.setReplyMarkup(ReplyKeyboard.buttonViewFileCategoryDeleteFile(categoryId, noteId));
        try {
            BeanConfig.tellMeBot().sendVoice(voice);
        } catch (TelegramApiException e) {
            log.error("Could not send document", e);
        }
        return "";
    }

}
