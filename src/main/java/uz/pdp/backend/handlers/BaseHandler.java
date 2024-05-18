package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.Services.favorites.FavoritesService;
import uz.pdp.backend.maker.MessageMaker;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.frontend.App;
import uz.pdp.backend.Services.userService.UserService;
import uz.pdp.backend.models.MyUser;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;
    protected ButtonCreator buttonCreator ;
    protected MessageMaker messageMaker;
    protected UserService userService ;
    protected FavoritesService favoritesService;

    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
        this.userService = new UserService();
        this.favoritesService = new FavoritesService();
        this.buttonCreator = new ButtonCreator();
        this.messageMaker = new MessageMaker();

    }

    public abstract void handle(Update update);

    protected MyUser getOrCreateUser(User from) {
        MyUser user = userService.get(from.id());
        if (user==null) {
            MyUser.builder().name(from.firstName()).userName(from.username()).id(from.id()).baseState(BaseState.MAIN_STATE).state(MainStates.MENU_STATE.name()).build();
            userService.create(user);
        }

        return user;
    }
}
