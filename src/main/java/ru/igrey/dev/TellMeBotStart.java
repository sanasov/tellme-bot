package ru.igrey.dev;


import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.igrey.dev.config.BeanConfig;

/**
 * Created by sanasov on 01.04.2017.
 */
public class TellMeBotStart {

    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {

            botsApi.registerBot(createTellMeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static TellMe createTellMeBot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setMaxThreads(10);
        return new TellMe(
                options,
                BeanConfig.telegramUserService(),
                BeanConfig.noteRepository(),
                BeanConfig.categoryRepository()
        );
    }

}
