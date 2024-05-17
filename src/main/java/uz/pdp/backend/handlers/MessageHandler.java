package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.models.MyUser;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Thread thread = Thread.currentThread();
        Message message = update.message();
        User from = message.from();
        String text = message.text();
        super.curUser = getOrCreateUser(from);
        super.update = update;
        if(curUser.getContact()==null || curUser.getContact().isEmpty()){
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.REGISTER_STATE));
            register(curUser);
        }
        if (text.equals("/start")) {
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.MENYU_STATE));
            mainMenyu();
        }else {

        }
        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);
    }



    private void mainMenyu() {
        ButtonCreator buttonCreator = new ButtonCreator();
        String[][] massage = {new String[]{"rent out home"}, new String[]{"rent home"}};
        String[][] callBack = {new String[]{"rentOutHome"}, new String[]{"rentHome"}};
        InlineKeyboardMarkup markup = buttonCreator.inlineKeyboardMarkup(massage, callBack);
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Main menyu").replyMarkup(markup);
        bot.execute(sendMessage);
    }

    private void register(MyUser user) {
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



/*
}else {
            String baseState = curUser.getBaseState();
            switch (BaseState.valueOf(baseState)){
                case MAIN_STATE -> registerState();
                case RENT_STATE -> registerState();
                case RENT_OUT_STATE -> registerState();

            }
        }
        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);
    }

    private void registerState() {
        String state = curUser.getState();
        Message message = update.message();
        switch (MainStates.valueOf(state)){
            case REGISTER_STATE ->{
                Contact contact = message.contact();
                if (contact!=null) {
                    String phoneNumber = contact.phoneNumber();
                    curUser.setContact(phoneNumber);
                    userService.update(curUser);
                    Boolean b = mainMenyu();
                    if (b){
                        curUser.setState(MainStates.MAIN_MENYU.toString());
                        userService.update(curUser);
                    }
                }else {

                }
            }

        }
    }
 */