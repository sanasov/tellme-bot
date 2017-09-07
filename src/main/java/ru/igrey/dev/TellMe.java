package ru.igrey.dev;


import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.ButtonCommandName;
import ru.igrey.dev.constant.KeyboardCommand;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.scheduler.JobFactory;
import ru.igrey.dev.scheduler.TriggerFactory;
import ru.igrey.dev.service.TelegramUserService;

import static ru.igrey.dev.constant.Delimiter.DELIMITER;

/**
 * Created by sanasov on 01.04.2017.
 */
@Slf4j
public class TellMe extends TelegramLongPollingBot {

    private TelegramUserService telegramUserService;
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;

    public TellMe(DefaultBotOptions defaultBotOptions, TelegramUserService telegramUserService, NoteRepository noteRepository, CategoryRepository categoryRepository, Scheduler scheduler) {
        super(defaultBotOptions);
        this.telegramUserService = telegramUserService;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;

        try {
            scheduler.start();
            scheduler.scheduleJob(JobFactory.createNotificationJob(), TriggerFactory.createTrigger());
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleDocumentMessage(update.getMessage());
            handlePhotoMessage(update.getMessage());
            handleVoiceMessage(update.getMessage());
            handleVideoMessage(update.getMessage());
            handleIncomingMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleButtonClick(update.getCallbackQuery());
        }
    }

    private void handlePhotoMessage(Message message) {
        if (message.getPhoto() == null) {
            return;
        }
        Long chatId = message.getChatId();
        Category photoCategory = categoryRepository.createCategoryIfNotExist(chatId, "Photo");
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getPhoto().get(0).getFileId(),
                        photoCategory.getId(),
                        chatId)
        );
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(message.getFrom());
        onShowCategories(telegramUser, chatId);
    }

    private void handleVoiceMessage(Message message) {
        if (message.getVoice() == null) {
            return;
        }
        Long chatId = message.getChatId();
        Category photoCategory = categoryRepository.createCategoryIfNotExist(chatId, "Voice");
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getVoice().getFileId(),
                        photoCategory.getId(),
                        chatId)
        );
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(message.getFrom());
        onShowCategories(telegramUser, chatId);
    }

    private void handleVideoMessage(Message message) {
        if (message.getVideo() == null) {
            return;
        }
        Long chatId = message.getChatId();
        Category videoCategory = categoryRepository.createCategoryIfNotExist(chatId, "Video");
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getVideo().getFileId(),
                        videoCategory.getId(),
                        chatId)
        );
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(message.getFrom());
        onShowCategories(telegramUser, chatId);
    }

    private void handleDocumentMessage(Message message) {
        if (message.getDocument() == null) {
            return;
        }
        Long chatId = message.getChatId();
        Category photoCategory = categoryRepository.createCategoryIfNotExist(chatId, "File document");
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getDocument().getFileId(),
                        photoCategory.getId(),
                        chatId)
        );
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(message.getFrom());
        onShowCategories(telegramUser, chatId);
    }

    private void handleIncomingMessage(Message message) {
        log.info("Incoming message: " + message.getText());
        log.info("From user: " + message.getFrom() + "; chatId: " + message.getChat().getId());

        if (message.getChat().isUserChat() && message.getText() != null) {
            handlePrivateIncomingMessage(message);
        }
    }

    private void handlePrivateIncomingMessage(Message incomingMessage) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(incomingMessage.getFrom());
        Long chatId = incomingMessage.getChatId();
        String incomingMessageText = incomingMessage.getText();

        if (incomingMessageText.equals("admin66")) {
            sendTextMessage(chatId, telegramUserService.usersStatistic(), null);
            return;
        }

        if (incomingMessageText.equals(KeyboardCommand.COMMAND_START)) {
            sendInstruction(chatId);
            sendTextMessage(chatId, AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY.text(), null);
            onShowCategories(telegramUser, chatId);
        } else if (incomingMessageText.equals(KeyboardCommand.HELP)) {
            sendTextMessage(chatId, AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY.text(), null);
        } else if (telegramUser.getStatus().equals(UserStatus.CREATE_CATEGORY)) {
            Category category = categoryRepository.saveCategory(Category.createNewCategory(chatId, incomingMessageText));
            telegramUser.setStatus(UserStatus.NEW);
            telegramUserService.save(telegramUser);
            Note note = noteRepository.findLastInsertedNoteWithoutCategory(chatId);
            if (note != null) {
                note.setCategoryId(category.getId());
                noteRepository.saveNote(note);
            }
            sendButtonMessage(
                    chatId,
                    AnswerMessageText.CATEGORY_IS_ADDED.text(),
                    ReplyKeyboard.buttonsForPickingCategoryForViewNotes(categoryRepository.findCategoryByUserId(chatId))
            );
        } else {
            Note newNote = noteRepository.saveNote(Note.createNewNote(incomingMessageText, null, chatId));
            sendButtonMessage(chatId, AnswerMessageText.PICK_CATEGORY_FOR_YOUR_NOTE.text(), ReplyKeyboard.buttonsForPickingCategoryAfterCreateNote(telegramUser.getCategories(), newNote.getId()));
        }

    }

    private void sendInstruction(Long chatId) {
        SendDocument instucion = new SendDocument();
        instucion.setChatId(chatId);
        instucion.setCaption("Download video instruction");
        instucion.setDocument("BQADAgAD_gADDiVoScMwzxMcfVuQAg");
        try {
            sendDocument(instucion);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleButtonClick(CallbackQuery query) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        Long chatId = query.getMessage().getChatId();
        answer.setCallbackQueryId(query.getId());
        String buttonCommand = query.getData().split(DELIMITER)[0];
        log.info("Button command: " + buttonCommand);
        switch (buttonCommand) {
            case ButtonCommandName.CREATE_CATEGORY:
                onCreateCategory(query, telegramUser, chatId);
                break;
            case ButtonCommandName.NOTE_DELETE:
                answer.setText(onNoteDelete(query));
                break;
            case ButtonCommandName.PICK_CATEGORY_FOR_ADDED_NOTE:
                answer.setText(onPickCategoryForNewNote(query));
                break;
            case ButtonCommandName.BACK_TO_CATEGORY_VIEW:
                editMessage(
                        chatId,
                        query.getMessage().getMessageId(),
                        AnswerMessageText.IN_WHICH_CATEGORY.text(),
                        ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories())
                );
                break;
            case ButtonCommandName.CATEGORY_DELETE:
                answer.setText(onCategoryDelete(query));
                break;
            case ButtonCommandName.CANCEL:
                answer.setText(backToViewCategories(query));
                break;
            case ButtonCommandName.PICK_CATEGORY_TO_VIEW_NOTES:
                onPickCategoryToViewNotes(query);
                break;
            case ButtonCommandName.REMOVE_MODE:
                onPickCategoryToDeleteNotes(query);
                break;
        }

        try {
            answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            log.error("Could not send answer on button click", e);
        }
    }

    private void onShowCategories(TelegramUser telegramUser, Long chatId) {
        if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
            sendButtonMessage(chatId, AnswerMessageText.NO_CATEGORIES_NO_NOTES.text(), null);
            return;
        }
        sendButtonMessage(chatId, AnswerMessageText.IN_WHICH_CATEGORY.text(), ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
    }

    private String backToViewCategories(CallbackQuery query) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
            editMessage(chatId, messageId, AnswerMessageText.NO_CATEGORIES_NO_NOTES.text(), null);
        } else {
            editMessage(chatId, messageId, AnswerMessageText.IN_WHICH_CATEGORY.text(), ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
        }
        return AnswerMessageText.BACK_TO_CATEGORY_VIEW.text();
    }

    private void onCreateCategory(CallbackQuery query, TelegramUser telegramUser, Long chatId) {
        Integer messageId = query.getMessage().getMessageId();
        telegramUser.setStatus(UserStatus.CREATE_CATEGORY);
        telegramUserService.save(telegramUser);
        editMessage(chatId, messageId, AnswerMessageText.ADD_CATEGORY.text(), null);
    }

    private void onPickCategoryToDeleteNotes(CallbackQuery query) {
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        Long chatId = query.getMessage().getChatId();
        Category category = categoryRepository.findCategoryById(categoryId);
        if (category == null) {
            return;
        }
        String answerMessage = (category.getNotes() == null || category.getNotes().isEmpty()) ? AnswerMessageText.EMPTY.text() : AnswerMessageText.PICK_NOTES_FOR_DELETE.text();
        editMessage(
                chatId,
                query.getMessage().getMessageId(),
                answerMessage,
                ReplyKeyboard.buttonsForPickingNotesForDelete(noteRepository.findByCategoryId(categoryId), categoryId, category.getTitle())
        );

    }


    private String onNoteDelete(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        Long noteId = Long.valueOf(query.getData().split(DELIMITER)[2]);
        deleteNote(chatId, query.getMessage().getMessageId(), noteId, categoryId);
        return AnswerMessageText.NOTE_IS_DELETED.text();
    }

    private String onPickCategoryForNewNote(CallbackQuery query) {
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        Long noteId = Long.valueOf(query.getData().split(DELIMITER)[2]);
        Note note = noteRepository.findById(noteId);
        note.setCategoryId(categoryId);
        noteRepository.saveNote(note);
        Category category = categoryRepository.findCategoryById(categoryId);
        editMessage(query.getMessage().getChatId(),
                query.getMessage().getMessageId(),
                category != null ? category.toString() : AnswerMessageText.CATEGORY_HAS_BEEN_DELETED.text(),
                ReplyKeyboard.buttonBackToCategoryView(categoryId));
        return AnswerMessageText.NOTE_IS_ADDED_IN_CATEGORY.text();
    }

    private String onCategoryDelete(CallbackQuery query) {
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        categoryRepository.deleteCategoryById(categoryId);
        backToViewCategories(query);
        return AnswerMessageText.CATEGORY_IS_DELETED + "\n" + AnswerMessageText.BACK_TO_CATEGORY_VIEW;
    }

    private void onPickCategoryToViewNotes(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split(DELIMITER)[1]);
        Category category = categoryRepository.findCategoryById(categoryId);
        editMessage(chatId,
                query.getMessage().getMessageId(),
                category == null ? AnswerMessageText.CATEGORY_HAS_BEEN_DELETED.text() : category.toString(),
                ReplyKeyboard.buttonBackToCategoryView(categoryId));
    }

    private void deleteNote(Long chatId, Integer messageId, Long noteId, Long categoryId) {
        noteRepository.delete(noteId);
        Category category = categoryRepository.findCategoryById(categoryId);
        editMessage(chatId,
                messageId,
                AnswerMessageText.PICK_NOTES_FOR_DELETE.text(),
                ReplyKeyboard.buttonsForPickingNotesForDelete(category.getNotes(), categoryId, category.getTitle())
        );
    }


    private void sendTextMessage(Long chatId, String responseMessage, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId)
                .setReplyMarkup(keyboardMarkup)
                .enableMarkdown(false)
                .enableHtml(true)
                .setText(responseMessage);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendButtonMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markup);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void editMessage(Long chatId, Integer messageId, String responseText, InlineKeyboardMarkup markup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setText(responseText);
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(messageId);
        editMessageText.enableMarkdown(false);
        editMessageText.enableHtml(true);
        try {
            editMessageText(editMessageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendNotification() {
        sendTextMessage(154090812L, "notification", null);
    }

    @Override
    public String getBotUsername() {
        return "@MindkeeperBot";
    }

    @Override
    public String getBotToken() {
        return "254626232:AAEO0aaj6ddVIfrPsOCEIkDa8i0y0rcc3k0";
    }

}

