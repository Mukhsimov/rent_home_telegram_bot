package uz.pdp;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("6410731641:AAEx5I_5YQsIr_34CyMezeWn4HNzxQqdRtg");
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        bot.setUpdatesListener(new UpdatesListener() {

            @Override
            public int process(List<Update> list) {
                for (Update update : list) {
                    CompletableFuture.runAsync(()->{

                        Message message = update.message();
                        User from = message.from();

                        //  SendMessage sendMessage = new SendMessage(message.chat().id(), message.text());
                        //  bot.execute(sendMessage);

                            Thread thread = Thread.currentThread();

                            System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + message.text());


                    }, executorService);

                }

                return CONFIRMED_UPDATES_ALL;

            }
        });

    }
}
