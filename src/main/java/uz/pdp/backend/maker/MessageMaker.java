package uz.pdp.backend.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.models.MyUser;
import uz.pdp.backend.states.childsStates.RentOutState;
import uz.pdp.backend.states.childsStates.RentState;

public class MessageMaker {
    protected ButtonCreator buttonCreator;

    public MessageMaker() {
        this.buttonCreator = new ButtonCreator();
    }

    public SendMessage mainMenu(MyUser myUser) {


        String[][] massage = {
                {"rent home", "rent out home"}
        };

        String[][] callBack = {
                 {RentState.RENT_HOME.toString(), RentOutState.RENT_OUT_HOME.toString()}
        };

        InlineKeyboardMarkup markup = buttonCreator.inlineKeyboardMarkup(massage, callBack);
        return new SendMessage(myUser.getId(), "Main menu").replyMarkup(markup);
    }


    public SendMessage register(MyUser user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "enter contact");

        KeyboardButton[][] button = {
                {new KeyboardButton("Phone number").requestContact(true)}
        };

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(button)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);


        return sendMessage.replyMarkup(markup);

    }



}
