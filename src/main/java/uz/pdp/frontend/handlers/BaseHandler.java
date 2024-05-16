package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.frontend.App;
import uz.pdp.frontend.Services.userService.UserService;
import uz.pdp.frontend.enums.states.BaseState;
import uz.pdp.frontend.enums.states.childsStates.RegisterStates;
import uz.pdp.frontend.models.MyUser;

import java.nio.file.Path;
import java.util.List;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;

    protected UserService userService ;

    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
        this.userService = new UserService();
    }

    public abstract void handle(Update update);

    protected MyUser getOrCreateUser(User from) {
        MyUser user = userService.get(from.id());
        if (user==null) {
            user = new MyUser(from.id(), from.firstName(), from.username(), BaseState.REGISTER_STATE.toString(), RegisterStates.REGISTER_STATE.toString(), null);
            userService.create(user);
        }

        return user;
    }

    // public abstract void execute();
}
