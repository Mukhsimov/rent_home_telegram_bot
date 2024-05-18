package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.states.childsStates.RentOutState;
import uz.pdp.backend.states.childsStates.RentState;
import uz.pdp.backend.Services.ButtonCreator;

public class CallBackQueryHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        curUser = getOrCreateUser(from);
        super.update = update;
        String data = callbackQuery.data();
        switch (curUser.getBaseState()){
            case MAIN_STATE -> mainState() ;
            case RENT_STATE -> rentState();
            case RENT_OUT_STATE -> rentOutState();
        }
        switch (data) {

            case "RENT_HOME" -> {
                curUser.setState(RentState.RENT_HOME.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                rentHomeState();
            }
            case "ADD_FAVOURITES" -> {
                curUser.setState(RentState.ADD_FAVOURITES.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                addFavorites();
            }
            case "SHOW_FAVORITES" ->{
                curUser.setState(RentState.SHOW_FAVOURITES.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                showFavourites();
            }
            case "SEARCH_HOME" -> {
                curUser.setState(RentState.SEARCH_HOME.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                searchHome();
            }
            case "RENT_OUT_HOME" -> {
                curUser.setState(RentOutState.RENT_OUT_HOME.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                rentOutState();
            }
            case "ADD_HOME" -> {
                curUser.setState(RentOutState.ADD_HOME.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                addHome();
            }
            case "SHOW_HOME" -> {
                curUser.setState(RentOutState.SHOW_HOME.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                showHomes();

            }
            case "DELETE_HOME" -> {
                curUser.setState(RentOutState.DELETE_HOME.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                deleteHome();
            }
            case "DELETE_ACCOUNT" -> {
                curUser.setState(RentOutState.DELETE_ACCOUNT.toString());
                curUser.setBaseState(BaseState.RENT_STATE);
                deleteAccount();
            }
            case "BACK" ->{
                switch (curUser.getBaseState()){
                    case MAIN_STATE ->{
                        curUser.setState(MainStates.MENU_STATE.toString());
                        mainState();
                    }
                    case RENT_OUT_STATE ->{
                        curUser.setState(RentOutState.RENT_OUT_HOME.toString());
                        rentOutState();
                    }
                    case RENT_STATE ->{
                        curUser.setState(RentState.RENT_HOME.toString());
                        rentHomeState();
                    }
                }
            }
        }
    }

    private void rentState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        RentState rentState = RentState.valueOf(curUser.getState());
        switch (rentState ){
            case SEARCH_HOME -> {
                switch (data){
                    case "Search home"->{

                    }
                    case "back"->{
                        rentBackTo(null);
                    }
                }
            }
        }
    }

    private void mainState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        MainStates mainStates = MainStates.valueOf(curUser.getState());
        switch (mainStates){
            case MENU_STATE -> {
                switch (data){
                    case "Search home"->{

                    }
                }
            }
        }
    }

    private void rentHomeState() {
        ButtonCreator creator = new ButtonCreator();
        /*
         rent home
         search home
         add favorites
         show favorites
        */
        String txt = "rent home";
        String[][] callBackData = {
                {"RENT_HOME", "SEARCH_HOME"},
                {"ADD_FAVOURITES", "SHOW_FAVOURITES"}
        };

        String[][] names = {
                {"Rent home", "Search home"},
                {"Add favourites", "Show favourites"}
        };

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

    private void rentOutState() {
        /*
         * add home
         * show homes
         * delete home
         * delete account
         * */

        ButtonCreator creator = new ButtonCreator();
        // search homes by filter
        // Favorites
        String txt = "Rent out home";
        String[][] callBackData = {
                {"ADD_HOME", "SHOW_HOME"},
                {"DELETE_HOME", "DELETE_ACCOUNT"}
        };

        String[][] names = {
                {"Add home", "Show homes"},
                {"Delete Home", "Delete account"}
        };

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

    private void addFavorites() {

    }


    private void searchHome() {
    }

    private void showFavourites(){

    }

    private void addHome() {
        String info = "enter information about home\n" +
                "1 price\n" +
                "2 square\n" +
                "3 room\n" +
                "FOR EXAMPLE : 100_000. 15. 12\n" +
                " note there MUST be dots between them";
        SendMessage sendMessage = new SendMessage(curUser.getId(), info);
        bot.execute(sendMessage);
    }

    private void showHomes() {

    }

    private void deleteHome() {
    }

    private void deleteAccount() {

    }

    private void rentBackTo( RentState rentState){

        switch (rentState){
            case RENT_HOME -> {
                // send message rent home
                curUser.setState(RentState.RENT_HOME.name());
            }
            case SEARCH_HOME -> {

            }
            case ADD_FAVOURITES -> {

            }
            case SHOW_FAVOURITES -> {

            }
            default -> {
                // send main menu message
                SendMessage sendMessage = messageMaker.mainMenu(curUser);
                SendResponse execute = bot.execute(sendMessage);
                curUser.setBaseState(BaseState.MAIN_STATE);
                curUser.setState(MainStates.MENU_STATE.name());
            }
        }
        userService.update(curUser);

    }

}
