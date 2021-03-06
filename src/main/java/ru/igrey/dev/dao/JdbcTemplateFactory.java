package ru.igrey.dev.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.dao.repository.TelegramUserRepository;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.domain.UserStatus;

/**
 * Created by sanasov on 26.04.2017.
 */
public class JdbcTemplateFactory {

    private static JdbcTemplate jdbcTemplate;

    public JdbcTemplateFactory() {

    }


    public JdbcTemplate create() {
        String url = "jdbc:sqlite:/" + DirectoryInfo.getDirectoryTargetScripts() + "botdb.s3db";
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl(url);
        sqLiteDataSource.setDatabaseName("botdb");
        return new JdbcTemplate(sqLiteDataSource);
    }

    public JdbcTemplate create2() {
        String url = "jdbc:sqlite:C:/trainings/tellme-bot/botdb.s3db";
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl(url);
        sqLiteDataSource.setDatabaseName("botdb");
        return new JdbcTemplate(sqLiteDataSource);
    }


    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplateFactory().create2();
        TelegramUserDao telegramUserDao = new TelegramUserDao(jdbcTemplate);
        CategoryRepository categoryRepository = categoryRepository(jdbcTemplate);
        TelegramUserRepository repository = new TelegramUserRepository(telegramUserDao, categoryRepository);
        TelegramUser telegramUser = createTelegramUser();
        telegramUser.setLanguageCode("BB");
        repository.save(telegramUser);
    }

    private static TelegramUser createTelegramUser() {
        TelegramUser telegramUser = new TelegramUser(
                11L,
                "Sergey",
                "Anasov",
                "Saidovich",
                null,
                UserStatus.NEW,
                null,
                null,
                "ru-RU");
        return telegramUser;
    }

    private static Category createCategory() {
        return new Category(
                null,
                11L,
                null,
                "Movie",
                null);
    }

    private static CategoryRepository categoryRepository(JdbcTemplate jdbcTemplate) {
        CategoryDao categoryDao = new CategoryDao(jdbcTemplate);
        NoteDao noteDao = new NoteDao(jdbcTemplate);
        return new CategoryRepository(new NoteRepository(noteDao), categoryDao);
    }

}
