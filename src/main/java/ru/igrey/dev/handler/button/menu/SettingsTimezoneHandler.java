package ru.igrey.dev.handler.button.menu;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.service.TelegramUserService;

public class SettingsTimezoneHandler implements ButtonHandler {

    private CallbackQuery query;
    private TelegramUser telegramUser;
    private TelegramUserService telegramUserService;

    public SettingsTimezoneHandler(CallbackQuery query, TelegramUser telegramUser, TelegramUserService telegramUserService) {
        this.query = query;
        this.telegramUser = telegramUser;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        telegramUser.setStatus(UserStatus.SET_TIMEZONE);
        telegramUserService.save(telegramUser);
        BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.SET_TIMEZONE.text(), null);
        return "";
    }

}
