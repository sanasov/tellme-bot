package ru.igrey.dev;


import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
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

            botsApi.registerBot(BeanConfig.tellMeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
