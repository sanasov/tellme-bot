package ru.igrey.dev.handler;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;
import ru.igrey.dev.service.TelegramUserService;

public class CreateCategoryHandler implements ButtonHandler {

    private CallbackQuery query;
    private TelegramUserService telegramUserService;
    private TelegramUser telegramUser;

    public CreateCategoryHandler(CallbackQuery query, TelegramUserService service, TelegramUser user) {
        this.query = query;
        this.telegramUserService = service;
        this.telegramUser = user;
    }

    @Override
    public String onClick() {
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        telegramUser.setStatus(UserStatus.CREATE_CATEGORY);
        telegramUserService.save(telegramUser);
        BeanConfig.tellMeBot().editMessage(chatId, messageId, AnswerMessageText.ADD_CATEGORY.text(), null);
        return "";
    }


}
