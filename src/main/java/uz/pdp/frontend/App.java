package uz.pdp.frontend;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InputMedia;
import com.pengrad.telegrambot.model.request.InputMediaPhoto;
import com.pengrad.telegrambot.request.SendMediaGroup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import uz.pdp.backend.manager.UpdateManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
