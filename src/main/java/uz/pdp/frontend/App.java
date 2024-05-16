package uz.pdp.frontend;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.frontend.enums.states.BaseState;
import uz.pdp.frontend.manager.UpdateManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static final String BOT_TOKEN = "6410731641:AAEx5I_5YQsIr_34CyMezeWn4HNzxQqdRtg";
    private static final ExecutorService POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final ThreadLocal<UpdateManager> updateHandlerThreadLocal = ThreadLocal.withInitial(UpdateManager::new);

    public static void main(String[] args) {

        TelegramBot bot = new TelegramBot(BOT_TOKEN);

        bot.setUpdatesListener((list) -> {

            for (Update update : list) {

                CompletableFuture.runAsync(() -> {

                    try {

                        updateHandlerThreadLocal.get().manage(update);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }, POOL);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        }, Exception::printStackTrace);


    }
}
