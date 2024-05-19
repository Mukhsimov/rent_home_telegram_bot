package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.Services.ButtonCreator;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.models.MyUser;
import uz.pdp.backend.states.childsStates.RentOutState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {

        Thread thread = Thread.currentThread();
        Message message = update.message();
        User from = message.from();

        String text = message.text();
        Contact contact = message.contact();
        Location location = message.location();

        super.curUser = getOrCreateUser(from);
        super.update = update;

        if (contact != null) {
            curUser.setContact(contact.phoneNumber());
            userService.update(curUser);
            bot.execute(new SendMessage(from.id(), "you are registred successefully"));
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.MENU_STATE));
            mainMenyu();

        } else if (curUser.getContact() == null) {
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.REGISTER_STATE));
            SendMessage register = messageMaker.register(curUser);
            bot.execute(register);
        }
        if (Objects.equals(text, "/start") && curUser.getContact() != null) {
            curUser.setState(String.valueOf(BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.MENU_STATE));
            mainMenyu();
        } else if (curUser.getState().equals(RentOutState.ADD_HOME.name())) {

        }
        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);
    }


    private void mainMenyu() {

        SendMessage sendMessage = messageMaker.mainMenu(curUser);
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