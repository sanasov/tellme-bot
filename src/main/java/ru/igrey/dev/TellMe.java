package ru.igrey.dev;


import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
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
import ru.igrey.dev.constant.Emoji;
import ru.igrey.dev.constant.KeyboardCommand;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.service.TelegramUserService;

/**
 * Created by sanasov on 01.04.2017.
 */
@Slf4j
public class TellMe extends TelegramLongPollingBot {

    private TelegramUserService telegramUserService;
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;

    public TellMe(DefaultBotOptions defaultBotOptions, TelegramUserService telegramUserService, NoteRepository noteRepository, CategoryRepository categoryRepository) {
        super(defaultBotOptions);
        this.telegramUserService = telegramUserService;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleIncomingMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleButtonClick(update.getCallbackQuery());
        }
    }

    private void handleIncomingMessage(Message message) {
        log.info("Incoming message: " + message.getText());
        log.info("From user: " + message.getFrom() + "; chatId: " + message.getChat().getId());

        if (message.getChat().isUserChat()) {
            handlePrivateIncomingMessage(message);
        }
    }

    private void handlePrivateIncomingMessage(Message incomingMessage) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(incomingMessage.getFrom());
        Long chatId = incomingMessage.getChatId();
        String incomingMessageText = incomingMessage.getText();
        if (incomingMessageText.equals(KeyboardCommand.COMMAND_START)) {
            sendTextMessage(chatId, AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY, null);
        } else if (incomingMessageText.equals(KeyboardCommand.SHOW_NOTES) || incomingMessageText.equals(KeyboardCommand.COMMAND_SHOW_NOTES)) {
            onShowCategories(telegramUser, chatId);
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
                    AnswerMessageText.CATEGORY_IS_ADDED,
                    ReplyKeyboard.buttonsForPickingCategoryForViewNotes(categoryRepository.findCategoryByUserId(chatId))
            );
        } else if (incomingMessageText.equals(KeyboardCommand.DELETE_NOTE) || incomingMessageText.equals(KeyboardCommand.COMMAND_DELETE_NOTE)) {
            sendButtonMessage(
                    chatId,
                    AnswerMessageText.IN_WHICH_CATEGORY_REMOVE_NOTES,
                    ReplyKeyboard.buttonsForPickingCategoryForDeleteNotes(categoryRepository.findCategoryByUserId(chatId))
            );
        } else {
            Note newNote = noteRepository.saveNote(Note.createNewNote(incomingMessageText, null, chatId));
            sendButtonMessage(chatId, AnswerMessageText.PICK_CATEGORY_FOR_YOUR_NOTE, ReplyKeyboard.buttonsForPickingCategoryAfterCreateNote(telegramUser.getCategories(), newNote.getId()));
        }

    }

    private void handleButtonClick(CallbackQuery query) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        Long chatId = query.getMessage().getChatId();
        answer.setCallbackQueryId(query.getId());
        String buttonCommand = query.getData().split("#")[0];
        log.info("Button command: " + buttonCommand);
        switch (buttonCommand) {
            case ButtonCommandName.CREATE_CATEGORY:
                onCreateCategory(query, telegramUser, chatId);
                break;
            case ButtonCommandName.CATEGORY_DELETE_NOTES:
                onPickCategoryToDeleteNotes(query);
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
                        AnswerMessageText.IN_WHICH_CATEGORY,
                        ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories())
                );
                break;
            case ButtonCommandName.CATEGORY_DELETE:
                answer.setText(onCategoryDelete(query));
                break;
            case ButtonCommandName.CANCEL:
                answer.setText(backToViewCategories(query, telegramUser));
                break;
            case ButtonCommandName.PICK_CATEGORY_FOR_VIEW_NOTES:
                onPickCategoryToViewNotes(query);
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
            sendButtonMessage(chatId, AnswerMessageText.NO_CATEGORIES_NO_NOTES, null);
            return;
        }
        sendButtonMessage(chatId, AnswerMessageText.IN_WHICH_CATEGORY, ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
    }

    private String backToViewCategories(CallbackQuery query, TelegramUser telegramUser) {
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
            editMessage(chatId, messageId, AnswerMessageText.NO_CATEGORIES_NO_NOTES, null);
        } else {
            editMessage(chatId, messageId, AnswerMessageText.IN_WHICH_CATEGORY, ReplyKeyboard.buttonsForPickingCategoryForViewNotes(telegramUser.getCategories()));
        }
        return AnswerMessageText.BACK_TO_CATEGORY_VIEW;
    }

    private void onCreateCategory(CallbackQuery query, TelegramUser telegramUser, Long chatId) {
        Integer messageId = query.getMessage().getMessageId();
        telegramUser.setStatus(UserStatus.CREATE_CATEGORY);
        telegramUserService.save(telegramUser);
        editMessage(chatId, messageId, AnswerMessageText.ADD_CATEGORY, null);
    }

    private void onPickCategoryToDeleteNotes(CallbackQuery query) {
        Long categoryId = Long.valueOf(query.getData().split("#")[1]);
        Long chatId = query.getMessage().getChatId();
        Category category = categoryRepository.findCategoryById(categoryId);
        String answerMessage = (category.getNotes() == null || category.getNotes().isEmpty()) ? AnswerMessageText.EMPTY : AnswerMessageText.PICK_NOTES_FOR_DELETE;
        editMessage(
                chatId,
                query.getMessage().getMessageId(),
                answerMessage,
                ReplyKeyboard.buttonsForPickingNotesForDelete(noteRepository.findByCategoryId(categoryId), categoryId, category.getTitle())
        );
    }

    private String onNoteDelete(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split("#")[1]);
        Long noteId = Long.valueOf(query.getData().split("#")[2]);
        deleteNote(chatId, query.getMessage().getMessageId(), noteId, categoryId);
        return AnswerMessageText.NOTE_IS_DELETED;
    }

    private String onPickCategoryForNewNote(CallbackQuery query) {
        Long categoryId = Long.valueOf(query.getData().split("#")[1]);
        Long noteId = Long.valueOf(query.getData().split("#")[2]);
        Note note = noteRepository.findById(noteId);
        note.setCategoryId(categoryId);
        noteRepository.saveNote(note);
        editMessage(query.getMessage().getChatId(),
                query.getMessage().getMessageId(),
                categoryRepository.findCategoryById(categoryId).toString(),
                ReplyKeyboard.buttonBackToCategoryView());
        return AnswerMessageText.NOTE_IS_ADDED_IN_CATEGORY;
    }

    private String onCategoryDelete(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split("#")[1]);
        categoryRepository.deleteCategoryById(categoryId);
        editMessage(
                chatId,
                query.getMessage().getMessageId(),
                Emoji.HEAVY_MULTIPLICATION_X.toString(),
                null
        );
        return AnswerMessageText.CATEGORY_IS_DELETED;
    }

    private void onPickCategoryToViewNotes(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        Long categoryId = Long.valueOf(query.getData().split("#")[1]);
        editMessage(chatId,
                query.getMessage().getMessageId(),
                categoryRepository.findCategoryById(categoryId).toString(),
                ReplyKeyboard.buttonBackToCategoryView());
    }

    private void deleteNote(Long chatId, Integer messageId, Long noteId, Long categoryId) {
        noteRepository.delete(noteId);
        Category category = categoryRepository.findCategoryById(categoryId);
        editMessage(chatId,
                messageId,
                AnswerMessageText.PICK_NOTES_FOR_DELETE,
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
        editMessageText.enableMarkdown(true);
        editMessageText.enableHtml(true);
        try {
            editMessageText(editMessageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
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

