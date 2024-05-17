package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.backend.Services.favorites.FavoritesService;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.frontend.App;
import uz.pdp.backend.Services.userService.UserService;
import uz.pdp.backend.models.MyUser;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;

    protected UserService userService ;
    protected FavoritesService favoritesService;

    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
        this.userService = new UserService();
        this.favoritesService = new FavoritesService();
    }

    public abstract void handle(Update update);

    protected MyUser getOrCreateUser(User from) {
        MyUser user = userService.get(from.id());
        if (user==null) {
            user = new MyUser(from.id(), from.firstName(), from.username(), BaseState.MAIN_STATE.toString(), MainStates.REGISTER_STATE.toString(), null);
            userService.create(user);
        }

        return user;
    }
}
