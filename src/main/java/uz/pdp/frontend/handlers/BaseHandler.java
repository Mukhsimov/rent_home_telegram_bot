package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.frontend.App;

public abstract class BaseHandler {
    protected TelegramBot bot;

    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
    }

    public abstract void handle(Update update);

   // public abstract void execute();
}
