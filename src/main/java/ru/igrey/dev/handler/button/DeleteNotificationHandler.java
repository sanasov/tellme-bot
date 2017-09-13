package ru.igrey.dev.handler.button;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.Emoji;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.dao.repository.NotificationRepository;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

public class DeleteNotificationHandler implements ButtonHandler {

    private CallbackQuery query;
    private NotificationRepository notificationRepository;
    private NoteRepository noteRepository;


    public DeleteNotificationHandler(CallbackQuery query, NotificationRepository notificationRepository, NoteRepository noteRepository) {
        this.query = query;
        this.notificationRepository = notificationRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Long notificationId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[1]);
        Long noteId = Long.valueOf(query.getData().split(BUTTON_DELIMITER)[2]);
        deleteNotificationAndNote(notificationId, noteId);
        BeanConfig.tellMeBot().editMessage(chatId,
                query.getMessage().getMessageId(),
                Emoji.FIRE.toString(),
                null
        );
        return AnswerMessageText.NOTE_IS_DELETED.text();
    }

    private void deleteNotificationAndNote(Long notificationId, Long noteId) {
        notificationRepository.deleteById(notificationId);
        noteRepository.delete(noteId);
    }

}
