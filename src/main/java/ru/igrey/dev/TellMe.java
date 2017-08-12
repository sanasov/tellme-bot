package ru.igrey.dev;


import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.service.TelegramUserService;

/**
 * Created by sanasov on 01.04.2017.
 */
@Slf4j
public class TellMe extends TelegramLongPollingBot {

    private TelegramUserService telegramUserService;
    private NoteRepository noteRepository;

    public TellMe(TelegramUserService telegramUserService, NoteRepository noteRepository) {
        this.telegramUserService = telegramUserService;
        this.noteRepository = noteRepository;
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

        if (incomingMessageText.equals(KeyboardText.CREATE_CATEGORY)) {


        } else if (incomingMessageText.equals(KeyboardText.SHOW_NOTES)) {


        } else {

        }

    }

    private void handleButtonClick(CallbackQuery query) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(query.getId());
        Message message = query.getMessage();

        try {
            answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTextMessage(Long chatId, String responseMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId)
                .setText(responseMessage);
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

