package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.TelegramUserDao;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.entity.TelegramUserEntity;

public class TelegramUserRepository {

    private TelegramUserDao userDao;
    private CategoryRepository categoryRepository;

    public TelegramUserRepository(TelegramUserDao userDao, CategoryRepository categoryRepository) {
        this.userDao = userDao;
        this.categoryRepository = categoryRepository;
    }

    public TelegramUser findById(Long userId) {
        TelegramUserEntity userEntity = userDao.findById(userId);
        if (userEntity == null) {
            return null;
        }
        return new TelegramUser(
                userDao.findById(userId),
                categoryRepository.findCategoryByUserId(userId)
        );
    }

    public void save(TelegramUser telegramUser) {
        userDao.save(telegramUser.toEntity());
    }
}
