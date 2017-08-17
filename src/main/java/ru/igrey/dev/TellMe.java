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
import ru.igrey.dev.constant.ButtonName;
import ru.igrey.dev.constant.KeyboardCommand;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.service.TelegramUserService;

import java.util.List;

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
            sendTextMessage(chatId, AnswerMessageText.ADD_NOTE_AND_PICK_CATEGORY, ReplyKeyboard.getKeyboardOnUserStart());
        } else if (incomingMessageText.equals(KeyboardCommand.DELETE_NOTE) || incomingMessageText.equals(KeyboardCommand.COMMAND_DELETE_NOTE)) {
            sendButtonMessage(
                    chatId,
                    AnswerMessageText.IN_WHICH_CATEGORY_REMOVE_NOTES,
                    ReplyKeyboard.buttonsForPickingCategoryForDeleteNotes(categoryRepository.findCategoryByUserId(chatId))
            );
        } else if (incomingMessageText.equals(KeyboardCommand.SHOW_NOTES) || incomingMessageText.equals(KeyboardCommand.COMMAND_SHOW_NOTES)) {
            if (telegramUser.getCategories() == null || telegramUser.getCategories().isEmpty()) {
                sendButtonMessage(chatId, AnswerMessageText.NO_CATEGORIES_NO_NOTES, null);
                return;
            }
            sendButtonMessage(chatId, AnswerMessageText.IN_WHICH_CATEGORY, ReplyKeyboard.buttonsForPickingCategoryForViewNote(telegramUser.getCategories()));
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
                    ReplyKeyboard.buttonsForPickingCategoryForViewNote(categoryRepository.findCategoryByUserId(chatId))
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
        if (query.getData().equals(KeyboardCommand.CREATE_CATEGORY)) {
            telegramUser.setStatus(UserStatus.CREATE_CATEGORY);
            telegramUserService.save(telegramUser);
            answer.setText(AnswerMessageText.ADD_CATEGORY);
        } else if (query.getData().contains(ButtonName.CATEGORY_DELETE)) {
            Long categoryId = Long.valueOf(query.getData().split("#")[1]);
            List<Note> notes = noteRepository.findByCategoryId(categoryId);
            String answerMessage = (notes == null || notes.isEmpty()) ? AnswerMessageText.EMPTY : AnswerMessageText.PICK_NOTES_FOR_DELETE;
            sendButtonMessage(
                    chatId,
                    answerMessage,
                    ReplyKeyboard.buttonsForPickingNotesForDelete(noteRepository.findByCategoryId(categoryId), categoryId)
            );
        } else if (query.getData().contains(ButtonName.NOTE_DELETE)) {
            Long categoryId = Long.valueOf(query.getData().split("#")[1]);
            Long noteId = Long.valueOf(query.getData().split("#")[2]);
            deleteNote(chatId, query.getMessage().getMessageId(), noteId, categoryId);
            answer.setText(AnswerMessageText.NOTE_IS_DELETED);
        } else if (query.getData().contains(ButtonName.PICK_CATEGORY_FOR_NOTE)) {
            Long categoryId = Long.valueOf(query.getData().split("#")[1]);
            Long noteId = Long.valueOf(query.getData().split("#")[2]);
            Note note = noteRepository.findById(noteId);
            note.setCategoryId(categoryId);
            noteRepository.saveNote(note);
            answer.setText(AnswerMessageText.NOTE_IS_ADDED_IN_CATEGORY);
        } else {
            Category category = telegramUser.getCategories().stream()
                    .filter(c -> c.getId().equals(Long.valueOf(query.getData())))
                    .findFirst()
                    .orElse(Category.createNewCategory(query.getMessage().getChatId(), ""));
            sendTextMessage(query.getMessage().getChatId(),
                    category.toString(),
                    ReplyKeyboard.getKeyboardOnUserStart());
        }
        try {
            answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            log.error("Could not send answer on button click", e);
        }
    }


    private void deleteNote(Long chatId, Integer messageId, Long noteId, Long categoryId) {
        noteRepository.delete(noteId);
        editMessage(chatId,
                messageId,
                AnswerMessageText.PICK_NOTES_FOR_DELETE,
                ReplyKeyboard.buttonsForPickingNotesForDelete(noteRepository.findByCategoryId(categoryId), categoryId)
        );
    }


    private void sendTextMessage(Long chatId, String responseMessage, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId)
                .setReplyMarkup(keyboardMarkup)
                .enableMarkdown(false)
                .setText(responseMessage);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendButtonMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
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

