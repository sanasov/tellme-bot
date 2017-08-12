package ru.igrey.dev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.User;
import ru.igrey.dev.dao.repository.TelegramUserRepository;
import ru.igrey.dev.domain.TelegramUser;

/**
 * Created by sanasov on 10.04.2017.
 */
public class TelegramUserService {

    private TelegramUserRepository telegramUserRepository;
    private static final Logger logger = LoggerFactory.getLogger(TelegramUserService.class);

    public TelegramUserService(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    public TelegramUser getOrCreateTelegramUserByUserId(User user) {
        TelegramUser result = telegramUserRepository.findById(user.getId().longValue());
        if (result == null) {
            result = TelegramUser.createNewUser(user);
            telegramUserRepository.save(result);
            logger.info("created user " + result);
        }
        return result;
    }

}
