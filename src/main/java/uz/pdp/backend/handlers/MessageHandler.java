package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.*;
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

        super.curUser = getOrCreateUser(from);
        super.update = update;

        Contact contact = message.contact();
        Location location = message.location();
        String text = message.text();

        if (contact != null){
            // set contact and send menu to head
            String phoneNumber = contact.phoneNumber();
            curUser.setContact(phoneNumber);
            userService.update(curUser);
            mainMenyu();

        } else if (location != null) {
            // set home location
        } else if (text != null) {
            // menage txt

            if (text.equals("/start")) {
                String state = curUser.getState();

                if (state.equals(MainStates.REGISTER_STATE.toString())){

                        curUser.setBaseState(BaseState.MAIN_STATE);
                        curUser.setState(String.valueOf(MainStates.MENU_STATE));
                        userService.update(curUser);
                        SendMessage register = messageMaker.register(curUser);
                        bot.execute(register);
                } else mainMenyu();
            }


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