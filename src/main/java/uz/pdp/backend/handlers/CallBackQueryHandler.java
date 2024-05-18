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
        switch (BaseState.valueOf(curUser.getBaseState())){
            case MAIN_STATE -> mainState() ;
            case RENT_STATE -> rentState();
            case RENT_OUT_STATE -> mainState();
        }
        switch (data) {

            case "rentHome" -> {
                curUser.setState(RentState.RENT_HOME.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                rentHomeState();
            }
            case "addFavorites" -> {
                curUser.setState(RentState.ADD_FAVOURITES.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                addFavorites();
            }
            case "showFavourites" ->{
                curUser.setState(RentState.SHOW_FAVORITES.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                showFavourites();
            }
            case "SearchHome" -> {
                curUser.setState(RentState.SEARCH_HOME.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                searchHome();
            }
            case "rentOut" -> {
                curUser.setState(RentOutState.RENT_OUT_HOME.name());
                curUser.setBaseState(BaseState.RENT_OUT_STATE.name());
                rentOutState();
            }
            case "addHome" -> {
                curUser.setState(RentOutState.ADD_HOME.name());
                curUser.setBaseState(BaseState.RENT_OUT_STATE.name());
                addHome();
            }
            case "showHomes" -> {
                curUser.setState(RentOutState.SHOW_HOME.name());
                curUser.setBaseState(BaseState.RENT_OUT_STATE.name());
                showHomes();

            }
            case "deleteHome" -> {
                curUser.setState(RentOutState.DELETE_HOME.name());
                curUser.setBaseState(BaseState.RENT_OUT_STATE.name());
                deleteHome();
            }
            case "deleteAccount" -> {
                curUser.setState(RentOutState.DELETE_ACCOUNT.name());
                curUser.setBaseState(BaseState.RENT_OUT_STATE.name());
                deleteAccount();
            }
            case "back" ->{
                switch (curUser.getBaseState()){
                    case "MAIN_STATE" ->{
                        curUser.setState(MainStates.MENYU_STATE.name());
                        //AAAAAAAAAAA
                    }
                    case "RENT_OUT_STATE" ->{
                        curUser.setState(RentOutState.RENT_OUT_HOME.name());
                        rentOutState();
                    }
                    case "RENT_STATE" ->{
                        curUser.setState(RentState.RENT_HOME.name());
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
            case MENYU_STATE -> {
                switch (data){
                    case "Search home"->{

                    }
                }
            }
        }
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

    private void addFavorites() {

    }


    private void searchHome() {
    }

    private void showFavourites(){

    }

    private void addHome() {
        String info = "enter information about home\n1 price\n2 square\n3 room\nFOR EXAMPLE : 100_000. 15. 12\n note there MUST be dots between them";
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
            case SHOW_FAVORITES -> {

            }
            default -> {
                // send main menu message
                SendMessage sendMessage = messageMaker.mainMenu(curUser);
                SendResponse execute = bot.execute(sendMessage);
                curUser.setBaseState(BaseState.MAIN_STATE.name());
                curUser.setState(MainStates.MENYU_STATE.name());
            }
        }
        userService.update(curUser);

    }

}
