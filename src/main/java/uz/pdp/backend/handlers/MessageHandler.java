package uz.pdp.backend.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.models.MyUser;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {

        Thread thread = Thread.currentThread();

        Message message = update.message();
        User from = message.from();
        super.curUser = getOrCreateUser(from);
        super.update = update;

        Contact contact = message.contact();
        PhotoSize[] photo = message.photo();
        Location location = message.location();
        String text = message.text();

        if (contact != null) {
            curUser.setContact(contact.phoneNumber());
            userService.update(curUser);
            mainMenu();
        } else if (photo != null) {
            // set photo for home
        } else if (location != null) {
            // set location for home
        } else if (text != null) {
            // menage text

            if (text.equals("/start")) {

                if (curUser.getContact() == null || curUser.getContact().isEmpty()) {
                    curUser.setState(String.valueOf(BaseState.MAIN_STATE));
                    curUser.setState(String.valueOf(MainStates.REGISTER_STATE));
                    register(curUser);
                } else {
                    curUser.setState(String.valueOf(BaseState.MAIN_STATE));
                    curUser.setState(String.valueOf(MainStates.MENU_STATE));
                    mainMenu();
                }
            }


        }


        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);
    }


    private void mainMenu() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void register(MyUser user) {
        SendMessage sendMessage = messageMaker.register(user);
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