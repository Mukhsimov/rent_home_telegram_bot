package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.filter.Filter;
import uz.pdp.backend.models.Favourite;
import uz.pdp.backend.models.Home;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.states.childsStates.RentOutState;
import uz.pdp.backend.states.childsStates.RentState;
import uz.pdp.backend.Services.ButtonCreator;

import java.util.List;

public class CallBackQueryHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getOrCreateUser(from);
        super.update = update;
        String data = callbackQuery.data();
        checkContact();
        setState();
        System.out.println("base"+curUser.getBaseState());
        System.out.println("state"+curUser.getState());
        if (curUser.getBaseState().equals(BaseState.MAIN_STATE)) {
            messageMaker.mainMenu(curUser);
        } else if (curUser.getBaseState().equals(BaseState.RENT_STATE)) {
            switch (data) {
                case "RENT_HOME"->{
                    rentHomeState();
                }
                case "SEARCH_HOME" -> {
                    searchHome();
                }
                case "SHOW_FAVOURITES" -> {
                    showFavourites();
                }
                case "ADD_FAVOURITES"->{
                    searchHome();
                }
                case "DELETE_FAVOURITE"->{
                    deleteFavourites();
                }
            }
        } else if (curUser.getBaseState().equals(BaseState.RENT_OUT_STATE)) {
            switch (data) {
                case "RENT_OUT_HOME"->{
                    rentHomeOutState();
                }
                case "ADD_HOME" -> {
                    addHome();
                }
                case "SHOW_HOME" -> {
                    showHomes();
                }
                case "DELETE_HOME" -> {
                    deleteHome();
                }
                case "DELETE_ACCOUNT" -> {
                    deleteAccount();
                }
            }
        }

    }


    private void searchHome() {
        String txt ="search home\n" +
                "1. search by room count\n" +
                "2. search by square\n" +
                "3. search by price\n" +
                "For example: 1. 4";

        userService.update(curUser);
        SendMessage send = new SendMessage(curUser.getId(), txt);

        bot.execute(send);
    }

    private void SearchHomeByFilter(int roomCount, double square, double price){
        Filter<Home> filter = (home) ->{
            return true;
        };
        homeService.getHomesByFilter(filter);
    }

    private void deleteFavourites(){
        List<Favourite> byUser = favoritesService.getByUser(curUser.getId());
        List<Home> homes = homeService.showFavourites(byUser);
        int i=0;
        StringBuilder stringBuilder = new StringBuilder();
        for (Home home : homes) {
            i++;
            stringBuilder.append(i)
                    .append(". ")
                    .append(home);
        }
        stringBuilder.append("\nchoose home : for example 2");
        bot.execute(new SendMessage(curUser.getId(), stringBuilder.toString()));
    }

    private void showFavourites() {
        List<Favourite> byUser = favoritesService.getByUser(curUser.getId());
        List<Home> homes = homeService.showFavourites(byUser);
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Home home : homes) {
            i++;
            stringBuilder.append(String.valueOf(i))
                    .append(". ")
                    .append(home.toString())
                    .append("\n");
        }
        bot.execute(new SendMessage(curUser.getId(), stringBuilder.toString()) );
    }

    private void addHome() {
        String info = "enter information about home\n1 price\n2 square\n3 room\nfor example : 100_000-15-12\n note there MUST be [-] between them";
        SendMessage sendMessage = new SendMessage(curUser.getId(), info);
        bot.execute(sendMessage);
    }

    private List<Home> showHomes() {
        curUser.setState(RentOutState.SHOW_HOME.name());
        List<Home> homes = homeService.showMy(curUser.getId());
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Home home : homes) {
            i++;
            stringBuilder.append(String.valueOf(i))
                    .append(". ")
                    .append(home.toString())
                    .append("\n");
        }
        bot.execute(new SendMessage(curUser.getId(), stringBuilder.toString()));
        return homes;
    }

    private void deleteHome() {
        showHomes();
        String text = "choose home \nfor example 1";
        curUser.setState(RentOutState.DELETE_HOME.name());
        SendMessage sendMessage = new SendMessage(curUser.getId(), text);
        bot.execute(sendMessage);
    }

    private void deleteAccount() {
        userService.delete(curUser.getId());
        homeService.deleateAccount(curUser.getId());
        favoritesService.deleateAccount(curUser.getId());
        bot.execute(new SendMessage(curUser.getId(), "account deleated successefully nigga"));
        curUser=null;
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
                SendMessage sendMessage = messageMaker.
                        mainMenu(curUser);
                bot.execute(sendMessage);
                curUser.setBaseState(BaseState.valueOf(BaseState.MAIN_STATE.name()));
                curUser.setState(MainStates.MENU_STATE.name());
            }
        }
        userService.update(curUser);
    }

    private void setState(){
        String data = update.callbackQuery().data();
        if(data.equals(RentOutState.RENT_OUT_HOME.name())){
            curUser.setBaseState(BaseState.RENT_OUT_STATE);
            curUser.setState(RentOutState.RENT_OUT_HOME.name());
            userService.update(curUser);
        }else if(data.equals(RentState.RENT_HOME.name())){
            curUser.setBaseState(BaseState.RENT_STATE);
            curUser.setState(RentState.RENT_HOME.name());
            userService.update(curUser);
        }else if(data.equals(RentState.SEARCH_HOME.name())){
            curUser.setState(RentState.SEARCH_HOME.name());
            curUser.setBaseState(BaseState.RENT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentState.SHOW_FAVOURITES.name())){
            curUser.setState(RentState.SHOW_FAVOURITES.name());
            curUser.setBaseState(BaseState.RENT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentState.ADD_FAVOURITES.name())){
            curUser.setState(RentState.ADD_FAVOURITES.name());
            curUser.setBaseState(BaseState.RENT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentState.DELETE_FAVOURITE.name())){
            curUser.setState(RentState.DELETE_FAVOURITE.name());
            curUser.setBaseState(BaseState.RENT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentOutState.ADD_HOME.name())){
            curUser.setState(RentOutState.ADD_HOME.name());
            curUser.setBaseState(BaseState.RENT_OUT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentOutState.SHOW_HOME.name())){
            curUser.setState(RentOutState.SHOW_HOME.name());
            curUser.setBaseState(BaseState.RENT_OUT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentOutState.DELETE_HOME.name())){
            curUser.setState(RentOutState.DELETE_HOME.name());
            curUser.setBaseState(BaseState.RENT_OUT_STATE);
            userService.update(curUser);
        }else if(data.equals(RentOutState.DELETE_ACCOUNT.name())){
            curUser.setState(RentOutState.DELETE_ACCOUNT.name());
            curUser.setBaseState(BaseState.RENT_OUT_STATE);
            userService.update(curUser);
        }
    }

    private void rentHomeState() {
        ButtonCreator creator = new ButtonCreator();
        // search homes by filter
        // Favorites
        String txt = "rent home";
        String[][] callBackData = {{RentState.SEARCH_HOME.name(), RentState.SHOW_FAVOURITES.name()}, {RentState.ADD_FAVOURITES.name(), RentState.DELETE_FAVOURITE.name()}};

        String[][] names = { {"search home", "show favourites"},
                {"add favourites", "deleate favourites"} };

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

    private void rentHomeOutState() {

        ButtonCreator creator = new ButtonCreator();
        // search homes by filter
        // Favorites
        String txt = "rent out home";
        String[][] callBackData = {{RentOutState.ADD_HOME.name(), RentOutState.SHOW_HOME.name()},
                {RentOutState.DELETE_HOME.name(), RentOutState.DELETE_ACCOUNT.name()}};

        String[][] names = {{"add home", "show homes"},
                {"delete Home", "delete account"}};

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

    private void checkContact() {
        if (curUser.getContact() == null || curUser.getContact().isEmpty()) {
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.REGISTER_STATE));
            SendMessage register = messageMaker.register(curUser);
            bot.execute(register);
        }
    }

}
