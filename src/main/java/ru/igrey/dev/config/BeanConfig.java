package ru.igrey.dev.config;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.igrey.dev.dao.CategoryDao;
import ru.igrey.dev.dao.JdbcTemplateFactory;
import ru.igrey.dev.dao.NoteDao;
import ru.igrey.dev.dao.TelegramUserDao;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.dao.repository.TelegramUserRepository;
import ru.igrey.dev.service.TelegramUserService;

public class BeanConfig {

    private static TelegramUserService telegramUserService;
    private static TelegramUserRepository telegramUserRepository;
    private static CategoryRepository categoryRepository;
    private static NoteRepository noteRepository;
    private static TelegramUserDao telegramUserDao;
    private static JdbcTemplate jdbcTemplate;

    public static TelegramUserService telegramUserService() {
        if (telegramUserService == null) {
            telegramUserService = new TelegramUserService(telegramUserRepository());
        }
        return telegramUserService;
    }

    public static TelegramUserRepository telegramUserRepository() {
        if (telegramUserRepository == null) {
            telegramUserRepository = new TelegramUserRepository(userDao(), categoryRepository());
        }
        return telegramUserRepository;
    }

    public static CategoryRepository categoryRepository() {
        if (categoryRepository == null) {
            CategoryDao categoryDao = new CategoryDao(jdbcTemplate());
            NoteDao noteDao = new NoteDao(jdbcTemplate());
            categoryRepository = new CategoryRepository(new NoteRepository(noteDao), categoryDao);
        }
        return categoryRepository;
    }

    public static NoteRepository noteRepository() {
        if (noteRepository == null) {
            noteRepository = new NoteRepository(new NoteDao(jdbcTemplate()));
        }
        return noteRepository;
    }

    public static TelegramUserDao userDao() {
        if (telegramUserDao == null) {
            telegramUserDao = new TelegramUserDao(jdbcTemplate());
        }
        return telegramUserDao;
    }

    public static JdbcTemplate jdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplateFactory().create2();
        }
        return jdbcTemplate;
    }

}
