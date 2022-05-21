import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import lombok.SneakyThrows;


public class Main {

    public static void main(String[] args) throws TelegramApiException, FileNotFoundException {

        //MyBot.QuotesRandom();
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot());
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
