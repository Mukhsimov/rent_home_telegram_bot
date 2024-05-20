package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.Services.favorites.FavoritesService;
import uz.pdp.backend.Services.homeService.HomeService;
import uz.pdp.backend.Services.photo.PhotoService;
import uz.pdp.backend.maker.MessageMaker;
import uz.pdp.backend.models.Favourite;
import uz.pdp.backend.models.Home;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.frontend.App;
import uz.pdp.backend.Services.userService.UserService;
import uz.pdp.backend.models.MyUser;

import java.util.List;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;
    protected ButtonCreator buttonCreator ;
    protected MessageMaker messageMaker;
    protected UserService userService ;
    protected FavoritesService favoritesService;
    protected HomeService homeService;
    protected PhotoService photoService;
    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
        this.userService = new UserService();
        this.favoritesService = new FavoritesService();
        this.buttonCreator = new ButtonCreator();
        this.messageMaker = new MessageMaker();
        this.homeService = new HomeService();
        this.photoService = new PhotoService();
    }

    public abstract void handle(Update update);

    protected MyUser getOrCreateUser(User from) {
        MyUser user = userService.get(from.id());
        if (user==null) {
            user = MyUser.builder().name(from.firstName()).userName(from.username()).id(from.id()).baseState(BaseState.MAIN_STATE).state(MainStates.REGISTER_STATE.name()).build();
            userService.create(user);
        }

        return user;
    }



}
