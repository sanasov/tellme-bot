package ru.igrey.dev;


import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
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

    public TellMe(TelegramUserService telegramUserService, NoteRepository noteRepository, CategoryRepository categoryRepository) {
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
        if (incomingMessageText.equals("/start")) {
            sendTextMessage(chatId, "Добавьте заметку и выберите для нее категорию", ReplyKeyboard.getKeyboardOnUserStart());
        } else if (telegramUser.getStatus().equals(UserStatus.CREATE_CATEGORY)) {
            Category category = categoryRepository.saveCategory(Category.createNewCategory(chatId, incomingMessageText));
            Note note = noteRepository.findLastInsertedNoteWithoutCategory(chatId);
            note.setCategoryId(category.getId());
            noteRepository.saveNote(note);
            telegramUser.setStatus(UserStatus.NEW);
            telegramUserService.save(telegramUser);
            sendButtonMessage(
                    chatId,
                    "Категория добавлена",
                    ReplyKeyboard.buttonsForPickingCategoryForViewNote(categoryRepository.findCategoryByUserId(chatId))
            );
        } else if (incomingMessageText.equals(KeyboardText.SHOW_NOTES) || incomingMessageText.equals("/shownotes")) {
            sendButtonMessage(chatId, "В какой категории?", ReplyKeyboard.buttonsForPickingCategoryForViewNote(telegramUser.getCategories()));
        } else {
            Note newNote = noteRepository.saveNote(Note.createNewNote(incomingMessageText, null, chatId));
            sendButtonMessage(chatId, "Выберите категорию для вашей заметки", ReplyKeyboard.buttonsForPickingCategoryAfterCreateNote(telegramUser.getCategories(), newNote.getId()));
        }

    }

    private void handleButtonClick(CallbackQuery query) {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserByUserId(query.getFrom());
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(query.getId());
        if (query.getData().equals(KeyboardText.CREATE_CATEGORY)) {
            telegramUser.setStatus(UserStatus.CREATE_CATEGORY);
            telegramUserService.save(telegramUser);
            answer.setText("Добавьте категорию");
        } else if (query.getData().contains("#")) {
            Long categoryId = Long.valueOf(query.getData().split("#")[0]);
            Long noteId = Long.valueOf(query.getData().split("#")[1]);
            Note note = noteRepository.findById(noteId);
            note.setCategoryId(categoryId);
            noteRepository.saveNote(note);
            answer.setText("Запись добавлена в категорию");
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

    private void sendTextMessage(Long chatId, String responseMessage, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId)
                .setReplyMarkup(keyboardMarkup)
                .enableMarkdown(true)
                .setText(responseMessage);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
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

