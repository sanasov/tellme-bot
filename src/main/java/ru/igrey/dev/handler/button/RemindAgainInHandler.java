package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.TimeRemindAgain;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Note;

import java.time.format.DateTimeFormatter;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class RemindAgainInHandler implements ButtonHandler {

    CallbackQuery query;
    NoteRepository noteRepository;

    public RemindAgainInHandler(CallbackQuery query, NoteRepository noteRepository) {
        this.query = query;
        this.noteRepository = noteRepository;
    }

    @Override
    public String onClick() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
        String noteId = query.getData().split(BUTTON_DELIMITER)[1];
        Note note = noteRepository.findById(Long.valueOf(noteId));
        note.setNotifyRule(timeRemindAgain().notificationDate(note.getTimezoneInMinutes()).format(formatter));
        noteRepository.updateNote(note);
        BeanConfig.tellMeBot().editMessage(
                query.getMessage().getChatId(),
                query.getMessage().getMessageId(),
                AnswerMessageText.NOTIFY_AGAIN.text() + "\n" + note.createNotifications().get(0).toView(note.getTimezoneInMinutes()),
                null
        );
        return AnswerMessageText.NOTIFICATION_IS_POSTPONED.text();
    }

    private TimeRemindAgain timeRemindAgain() {
        String commandRemindAgainIn = query.getData().split(BUTTON_DELIMITER)[0];
        return TimeRemindAgain.valueOf(commandRemindAgainIn);
    }
}
