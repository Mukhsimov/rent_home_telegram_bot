package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.Services.homeService.HomeService;
import uz.pdp.backend.models.Favourite;
import uz.pdp.backend.models.Home;
import uz.pdp.backend.models.MyUser;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.states.childsStates.RentOutState;
import uz.pdp.backend.states.childsStates.RentState;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;

import java.util.List;

public class CallBackQueryHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        curUser = getOrCreateUser(from);
        super.update = update;
        String data = callbackQuery.data();

        switch (data) {
            case "RENT_HOME" -> {
                curUser.setState(RentState.RENT_HOME.name());
                curUser.setBaseState(BaseState.valueOf(BaseState.RENT_STATE.name()));
                rentHomeState();
            }
            case "RENT_OUT_HOME" -> {
                curUser.setState(RentOutState.RENT_OUT_HOME.name());
                curUser.setBaseState(BaseState.valueOf(BaseState.RENT_OUT_STATE.name()));
                rentHomeOutState();
            }
        }

        switch (BaseState.valueOf(String.valueOf(curUser.getBaseState()))) {
            case RENT_STATE -> rentState();
            case RENT_OUT_STATE -> rentOutState();
        }
    }

    private void rentState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        RentState rentState = RentState.valueOf(curUser.getState());
        switch (rentState) {
            case RENT_HOME -> {
                switch (data) {
                    case "SEARCH_HOME" -> {
                        searchHome();
                    }
                    case "SHOW_FAVOURITES" -> {
                        showFavourites();
                    }
                    case "BACK" -> {
                        rentBackTo(null);
                    }
                }
            }
        }
    }

    /*
    RENT_OUT_HOME,
    ADD_HOME,
    SHOW_HOME,
    DELETE_HOME,
    DELETE_ACCOUNT
     */
    private void rentOutState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        RentOutState rentOutState = RentOutState.valueOf(curUser.getState());
        if (curUser.getContact() == null || curUser.getContact().isEmpty()) {
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.REGISTER_STATE));
            register(curUser);
        }
        switch (rentOutState) {
            case RENT_OUT_HOME -> {
                switch (data) {
                    case "ADD_HOME" -> {
                        addHome();
                    }
                    case "SHOW_HOME" -> {
                        showHomes();
                    }
                    case "DELEATE_HOME" -> {
                        deleteHome();
                    }
                    case "DELEATE_ACCOUNT" -> {
                        deleteAccount();
                    }
                    case "BACK" -> {
                        rentOutBack(null);
                    }
                }
            }
        }
    }

    private void searchHome() {
        curUser.setState(RentState.SEARCH_HOME.name());
        String[][] strings = new String[4][1];
        strings = new String[][]{
                {"search by room count"},
                {"search by square"},
                {"search by price"},
                {"search by location"}
        };
        String[][] strings1 = {
                {"search by room count", "search by square", "search by price", "search by location"}
        };
        InlineKeyboardMarkup markup = buttonCreator.inlineKeyboardMarkup(strings, strings1);
        SendMessage sendMessage = new SendMessage(curUser.getId(), "choose menyu").replyMarkup(markup);
        bot.execute(sendMessage);
    }

    private void showFavourites() {
        List<Favourite> byUser = favoritesService.getByUser(curUser.getId());
        List<Home> homes = homeService.showFavourites(byUser);
        int i = 0;
        for (Home home : homes) {
            i++;
            System.out.println(i + " " + home);
        }
    }

    private void addHome() {
        String info = "enter information about home\n1 price\n2 square\n3 room\nFOR EXAMPLE : 100_000. 15. 12\n note there MUST be dots between them";
        curUser.setState(RentOutState.ADD_HOME.name());
        SendMessage sendMessage = new SendMessage(curUser.getId(), info);
        bot.execute(sendMessage);
    }

    private void showHomes() {
        curUser.setState(RentOutState.SHOW_HOME.name());
        List<Home> homes = homeService.showMy(curUser.getId());
        int i = 0;
        for (Home home : homes) {
            i++;
            System.out.println(i + " " + home);
        }
    }

    private void deleteHome() {
        showHomes();
        String text = "CHOOSE_HOME";
        curUser.setState(RentOutState.DELETE_HOME.name());
        SendMessage sendMessage = new SendMessage(curUser.getId(), text);
        bot.execute(sendMessage);
    }

    private void deleteAccount() {
        userService.delete(curUser.getId());
        bot.execute(new SendMessage(curUser.getId(), "account deleated successefully nigga"));
    }

    private void rentOutBack(RentOutState rentOutState) {
        switch (rentOutState) {
            case ADD_HOME -> {
                addHome();
            }
            case SHOW_HOME -> {
                showHomes();
            }
            case DELETE_HOME -> {
                deleteHome();
            }
            case DELETE_ACCOUNT -> {
                deleteAccount();
            }
            default -> {
                SendMessage sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                curUser.setBaseState(BaseState.valueOf(BaseState.MAIN_STATE.name()));
                curUser.setState(MainStates.MENU_STATE.name());
            }
        }
    }

    private void rentBackTo(RentState rentState) {

        switch (rentState) {
            case SEARCH_HOME -> {
                searchHome();
                curUser.setState(RentState.RENT_HOME.name());
            }
            case SHOW_FAVOURITES -> {
                showFavourites();
                curUser.setState(RentState.SHOW_FAVOURITES.name());
            }
            default -> {
                // send main menu message
                SendMessage sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                curUser.setBaseState(BaseState.valueOf(BaseState.MAIN_STATE.name()));
                curUser.setState(MainStates.MENU_STATE.name());
            }
        }
        userService.update(curUser);
    }

    private void rentHomeState() {
        ButtonCreator creator = new ButtonCreator();
        // search homes by filter
        // Favorites
        String txt = "rent home";
        String[][] callBackData = {{"SearchHome", "Favorites"}};

        String[][] names = {{"Search home", "Favorites"}};

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

    private void rentHomeOutState() {
        /*
         * add home
         * show homes
         * delete home
         * delete account
         * */

        ButtonCreator creator = new ButtonCreator();
        // search homes by filter
        // Favorites
        String txt = "rent out home";
        String[][] callBackData = {{"addHome", "showHomes"},
                {"deleteHome", "deleteAccount"}};

        String[][] names = {{"add home", "show homes"},
                {"delete Home", "delete account"}};

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

}
