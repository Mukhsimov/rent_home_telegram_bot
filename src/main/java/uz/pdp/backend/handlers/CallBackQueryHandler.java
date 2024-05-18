package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.Services.favorites.FavoritesService;
import uz.pdp.backend.models.MyUser;
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

        switch (data){
            case "rentHome" -> {
                curUser.setState(RentState.RENT_HOME.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                rentHomeState();
            }
            case "rentOut" -> {
                curUser.setState(RentOutState.RENT_OUT_HOME.name());
                curUser.setBaseState(BaseState.RENT_OUT_STATE.name());
                rentHomeOutState();
            }
        }

        switch (BaseState.valueOf(curUser.getBaseState())){
            case RENT_STATE -> rentState();
            case RENT_OUT_STATE -> rentOutState();
        }
    }

    private void rentState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        RentState rentState = RentState.valueOf(curUser.getState());
        switch (rentState ){
            case RENT_HOME -> {
                switch (data){
                    case "Search home"->{
                        searchHome();
                    }
                    case "show favourites"->{
                        showFavourites();
                    }
                    case "back"->{
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
        switch (rentOutState){
            case RENT_OUT_HOME -> {
                switch (data){
                    case "add home"->{
                        addHome();
                    }
                    case "show home"->{
                        showHomes();
                    }
                    case "deleate home"->{
                        deleteHome();
                    }
                    case "deleate account"->{
                        deleteAccount();
                    }
                    case "back"->{
                        rentOutBack(null);
                    }
                }
            }
        }
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
    private void rentOutBack(RentOutState rentOutState){
        switch (rentOutState){
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
                SendMessage sendMessage = messageMaker.mainMenyu(curUser);
                SendResponse execute = bot.execute(sendMessage);
                curUser.setBaseState(BaseState.MAIN_STATE.name());
                curUser.setState(MainStates.MENYU_STATE.name());
            }
        }
    }

    private void rentBackTo( RentState rentState){

        switch (rentState){
            case SEARCH_HOME -> {
                searchHome();
                curUser.setState(RentState.RENT_HOME.name());
            }
            case SHOW_FAVORITES -> {
                showFavourites();
                curUser.setState(RentState.SHOW_FAVORITES.name());
            }
            default -> {
                // send main menu message
                SendMessage sendMessage = messageMaker.mainMenyu(curUser);
                SendResponse execute = bot.execute(sendMessage);
                curUser.setBaseState(BaseState.MAIN_STATE.name());
                curUser.setState(MainStates.MENYU_STATE.name());
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
