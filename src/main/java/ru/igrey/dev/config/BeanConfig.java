package ru.igrey.dev.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.igrey.dev.TellMe;
import ru.igrey.dev.dao.CategoryDao;
import ru.igrey.dev.dao.JdbcTemplateFactory;
import ru.igrey.dev.dao.NoteDao;
import ru.igrey.dev.dao.TelegramUserDao;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.dao.repository.TelegramUserRepository;
import ru.igrey.dev.service.TelegramUserService;

@Slf4j
public class BeanConfig {

    private static TellMe tellMeBot;
    private static TelegramUserService telegramUserService;
    private static TelegramUserRepository telegramUserRepository;
    private static CategoryRepository categoryRepository;
    private static NoteRepository noteRepository;
    private static TelegramUserDao telegramUserDao;
    private static JdbcTemplate jdbcTemplate;
    private static Scheduler scheduler;


    public static TellMe tellMeBot() {
        if (tellMeBot == null) {
            DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
            options.setMaxThreads(10);
            tellMeBot = new TellMe(
                    options,
                    telegramUserService(),
                    noteRepository(),
                    categoryRepository(),
                    scheduler()
            );
        }
        return tellMeBot;
    }


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


    public static Scheduler scheduler() {
        if (scheduler == null) {
            try {
                scheduler = new StdSchedulerFactory().getScheduler();
            } catch (SchedulerException e) {
                log.error(e.getMessage(), e);
            }
        }
        return scheduler;
    }


}
