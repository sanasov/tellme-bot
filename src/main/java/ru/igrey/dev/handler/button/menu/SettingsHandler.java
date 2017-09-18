package ru.igrey.dev.handler.button.menu;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.handler.button.ButtonHandler;
import ru.igrey.dev.keyboard.ReplyKeyboard;

public class SettingsHandler implements ButtonHandler {

    private CallbackQuery query;
    private TelegramUser telegramUser;

    public SettingsHandler(CallbackQuery query, TelegramUser telegramUser) {
        this.query = query;
        this.telegramUser = telegramUser;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        BeanConfig.tellMeBot().editMessage(
                chatId,
                query.getMessage().getMessageId(),
                telegramUser.settings(),
                ReplyKeyboard.settingsButtons()
        );
        return "";
    }

}
