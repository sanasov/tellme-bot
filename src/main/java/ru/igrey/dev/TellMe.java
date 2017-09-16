package ru.igrey.dev;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import ru.igrey.dev.constant.KeyboardCommand;
import ru.igrey.dev.constant.NamedCategory;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.handler.button.ButtonHandlerFactory;
import ru.igrey.dev.parse.ParsedTimeZone;
import ru.igrey.dev.scheduler.JobFactory;
import ru.igrey.dev.scheduler.TriggerFactory;
import ru.igrey.dev.service.TelegramUserService;

import java.util.Arrays;

/**
 * Created by sanasov on 01.04.2017.
 */
@Slf4j
public class TellMe extends TelegramLongPollingBot {

    private TelegramUserService telegramUserService;
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;
    private ButtonHandlerFactory buttonHandlerFactory;

    public TellMe(DefaultBotOptions defaultBotOptions, TelegramUserService telegramUserService, NoteRepository noteRepository, CategoryRepository categoryRepository, Scheduler scheduler, ButtonHandlerFactory buttonHandlerFactory) {
        super(defaultBotOptions);
        this.telegramUserService = telegramUserService;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.buttonHandlerFactory = buttonHandlerFactory;

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
        Category photoCategory = categoryRepository.createCategoryIfNotExist(chatId, NamedCategory.PHOTO);
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getPhoto().get(0).getFileId(),
                        message.getPhoto().get(0).hasFilePath() ? message.getPhoto().get(0).getFilePath() : NamedCategory.PHOTO + message.getPhoto().get(0).getFileSize(),
                        message.getCaption(),
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
        Category photoCategory = categoryRepository.createCategoryIfNotExist(chatId, NamedCategory.VOICE);
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getVoice().getFileId(),
                        message.getVoice().getMimeType() + ", " + message.getVoice().getDuration() + ", " + message.getVoice().getFileSize(),
                        message.getCaption(),
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
        Category videoCategory = categoryRepository.createCategoryIfNotExist(chatId, NamedCategory.VIDEO);
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getVideo().getFileId(),
                        message.getVideo().getMimeType() + ", " + message.getVideo().getDuration() + ", " + message.getVideo().getThumb(),
                        message.getCaption(),
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
        Category photoCategory = categoryRepository.createCategoryIfNotExist(chatId, NamedCategory.FILE_DOCUMENT);
        noteRepository.saveNote(
                Note.createNewNote(
                        message.getDocument().getFileId(),
                        message.getDocument().getFileName(),
                        message.getCaption(),
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

        if (incomingMessageText.toLowerCase().trim().equals("admin66")) {
            sendTextMessage(chatId, telegramUserService.usersStatistic(), null);
            return;
        }

        if (incomingMessageText.equals(KeyboardCommand.COMMAND_START)) {
            sendInstruction(chatId);
            sendTextMessage(chatId, AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY.text(), null);
            if (telegramUser.getCategories().isEmpty()) {
                telegramUser.setCategories(Arrays.asList(categoryRepository.createCategoryIfNotExist(chatId, NamedCategory.COMMON)));
            }
            if (StringUtils.isBlank(telegramUser.getLanguage())) {
                sendButtonMessage(chatId, AnswerMessageText.PICK_LANGUAGE.text(), ReplyKeyboard.buttonsForPickingLanguage());
                return;
            }
            onShowCategories(telegramUser, chatId);
        } else if (incomingMessageText.equals(KeyboardCommand.HELP)) {
            sendTextMessage(chatId, AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY.text(), null);
        } else if (incomingMessageText.equals(KeyboardCommand.VIEW)) {
            onShowCategories(telegramUser, chatId);
        } else if (incomingMessageText.equals(KeyboardCommand.SETTINGS)) {
            onShowCategories(telegramUser, chatId);
        } else if (incomingMessageText.equals(KeyboardCommand.RATE)) {
            sendTextMessage(chatId, AnswerMessageText.RATE.text(), null);
        } else if (telegramUser.getStatus().equals(UserStatus.CREATE_CATEGORY)) {
            Category category = categoryRepository.saveCategory(Category.createNewCategory(chatId, incomingMessageText));
            telegramUser.setStatus(UserStatus.NEW);
            telegramUserService.save(telegramUser);
            noteRepository.saveNewNotesInCategory(chatId, category.getId());

            sendButtonMessage(
                    chatId,
                    AnswerMessageText.CATEGORY_IS_ADDED.text(),
                    ReplyKeyboard.buttonsForPickingCategoryForViewNotes(categoryRepository.findCategoryByUserId(chatId))
            );
        } else if (telegramUser.getStatus().equals(UserStatus.SET_TIMEZONE)) {
            telegramUser.setStatus(UserStatus.NEW);
            Integer timeZone = 0;
            try {
                timeZone = new ParsedTimeZone(incomingMessageText).totalDiffInMinutes();
                telegramUser.setTimezone(timeZone);
                sendTextMessage(
                        chatId,
                        "Timezone is setted",
                        null
                );
                telegramUserService.save(telegramUser);
            } catch (Exception e) {
                log.info("Wrong number format");
                sendTextMessage(
                        chatId,
                        AnswerMessageText.WRONG_FORMAT.text(),
                        null
                );
            }

        } else {// create new note
            Note newNote = noteRepository.saveNote(Note.createNewNote(incomingMessageText, null, null, null, chatId));
            sendButtonMessage(chatId, AnswerMessageText.PICK_CATEGORY_FOR_YOUR_NOTE.text(), ReplyKeyboard.buttonsForPickingCategoryAfterCreateNote(telegramUser.getCategories(), newNote.getId()));
        }

    }

    private void sendInstruction(Long chatId) {
        SendDocument instruction = new SendDocument();
        instruction.setChatId(chatId);
        instruction.setCaption("Download video instruction");
        instruction.setDocument("BQADAgAD_gADDiVoScMwzxMcfVuQAg");
        try {
            sendDocument(instruction);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleButtonClick(CallbackQuery query) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(query.getId());
        ButtonHandler buttonHandler = buttonHandlerFactory.create(telegramUser, query);
        answer.setText(
                buttonHandler.onClick()
        );
        try {
            answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            log.error("Could not send answer on button click", e);
        }
    }

    public void onShowCategories(TelegramUser telegramUser, Long chatId) {
        if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
            sendButtonMessage(chatId, AnswerMessageText.NO_CATEGORIES_NO_NOTES.text(), null);
            return;
        }
        sendButtonMessage(chatId, AnswerMessageText.PICK_CATEGORY_TO_VIEW_NOTES.text(), ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
    }

    public void sendTextMessage(Long chatId, String responseMessage, ReplyKeyboardMarkup keyboardMarkup) {
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

    public void sendButtonMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markup);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Message text: " + text + "\n" + e.getMessage(), e);
        }
    }

    public void editMessage(Long chatId, Integer messageId, String responseText, InlineKeyboardMarkup markup) {
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
            log.error("Message text: " + responseText + "\n" + e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return "@sergiseyBot";
    }

    @Override
    public String getBotToken() {
        return "298962706:AAFJMqitHXvDNWT1Bw4N-ebU2u0Ny9GBSZU";
    }

}

