package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.frontend.enums.states.BaseState;
import uz.pdp.frontend.enums.states.childsStates.RentOutState;
import uz.pdp.frontend.enums.states.childsStates.RentState;
import uz.pdp.frontend.models.Favourite;
import uz.pdp.frontend.utills.ButtonCreator;

public class CallBackQueryHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        String data = callbackQuery.data();

        switch (data) {

            case "rentHome" -> {
                curUser.setState(RentState.RENT_HOME.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                rentHomeState();
            }
            case "favorites" -> {
                curUser.setState(RentState.FAVORITES.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                favorites();
            }
            case "SearchHome" -> {
                curUser.setState(RentState.SEARCH_HOME.name());
                curUser.setBaseState(BaseState.RENT_STATE.name());
                searchHome();
            }
            case "rentOut" -> {
                curUser.setState(RentOutState.RENT_OUT.name());
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


        }
    }

    private void rentHomeState() {
        ButtonCreator creator = new ButtonCreator();
        // search homes by filter
        // Favorites
        String txt = "rent home";
        String[][] callBackData = {{"SearchHome", "Favorites"}};

        String[][] names = {{"Search home", "Favorites"}};

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData, txt);

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

        InlineKeyboardMarkup inlineKeyboardMarkup = creator.inlineKeyboardMarkup(names, callBackData, txt);

        SendMessage sendMessage = new SendMessage(curUser.getId(), txt);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);

    }

    private void favorites() {
    }


    private void searchHome() {
    }

    private void addHome() {
    }

    private void showHomes() {
    }

    private void deleteHome() {
    }

    private void deleteAccount() {

    }


}
