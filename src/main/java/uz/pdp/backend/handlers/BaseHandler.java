package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.Services.favorites.FavoritesService;
import uz.pdp.backend.Services.homeService.HomeService;
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
    protected HomeService homeService;

    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
        this.userService = new UserService();
        this.favoritesService = new FavoritesService();
        this.buttonCreator = new ButtonCreator();
        this.messageMaker = new MessageMaker();
        this.homeService = new HomeService();

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

    protected void register(MyUser user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "enter contact");
        KeyboardButton[][] button = {
                {
                        new KeyboardButton("Phone number").requestContact(true)
                }
        };
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(button).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(markup);
        userService.update(user);
        bot.execute(sendMessage);
    }
}
