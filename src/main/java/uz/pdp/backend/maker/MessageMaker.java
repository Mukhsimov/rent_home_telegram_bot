package uz.pdp.backend.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.models.MyUser;

public class MessageMaker {
    protected ButtonCreator buttonCreator ;

    public SendMessage mainMenyu(MyUser myUser) {

        String[][] massage = {new String[]{"rent out home"}, new String[]{"rent home"}};
        String[][] callBack = {new String[]{"rentOutHome"}, new String[]{"rentHome"}};
        InlineKeyboardMarkup markup = buttonCreator.inlineKeyboardMarkup(massage, callBack);
        SendMessage sendMessage = new SendMessage(myUser.getId(), "Main menyu").replyMarkup(markup);
        return sendMessage;
    }

}
