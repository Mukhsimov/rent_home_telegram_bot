package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.frontend.enums.states.BaseState;
import uz.pdp.frontend.enums.states.childsStates.RegisterStates;
import uz.pdp.frontend.models.MyUser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Thread thread = Thread.currentThread();
        Message message = update.message();
        User from = message.from();
        String text = message.text();
        curUser = getOrCreateUser(from);
        super.update = update;
        if (text.equals("/start")) {
            if (curUser.getContact()!=null&&!curUser.getContact().isEmpty()) {
                mainMenyu();
                curUser.setBaseState(String.valueOf(BaseState.REGISTER_STATE));
                curUser.setState(String.valueOf(RegisterStates.MAIN_MENYU));
            }else {
                register(curUser);
                curUser.setState(String.valueOf(BaseState.REGISTER_STATE));
                curUser.setState(String.valueOf(RegisterStates.REGISTER_STATE));
            }
        }else {
            String baseState = curUser.getBaseState();
            switch (BaseState.valueOf(baseState)){
                case REGISTER_STATE -> registerState();
                case RENT_STATE -> registerState();
                case RENT_OUT_STATE -> registerState();
            }
        }
        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);
    }

    private void registerState() {
        String state = curUser.getState();
        Message message = update.message();
        switch (RegisterStates.valueOf(state)){
            case REGISTER_STATE ->{
                Contact contact = message.contact();
                if (contact!=null) {
                    String phoneNumber = contact.phoneNumber();
                    curUser.setContact(phoneNumber);
                    userService.update(curUser);
                    Boolean b = mainMenyu();
                    if (b){
                        curUser.setState(RegisterStates.MAIN_MENYU.toString());
                        userService.update(curUser);
                    }
                }else {

                }
            }

        }
    }

    private Boolean mainMenyu() {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "MAIN MENYU");
        InlineKeyboardButton[][] button1 = new InlineKeyboardButton[0][2];
        button1[0][0] = new InlineKeyboardButton("rent out home").callbackData("rentOutHome");
        button1[0][1] = new InlineKeyboardButton("rent home").callbackData("rentHome");
        InlineKeyboardMarkup markup1 = new InlineKeyboardMarkup(button1);
        sendMessage.replyMarkup(markup1);
        SendResponse execute = bot.execute(sendMessage);
        return execute.isOk();
        /*InlineKeyboardButton[][] button = {
                {
                        new InlineKeyboardButton("rent out home").callbackData("rentOutHome"),
                        new InlineKeyboardButton("rent home").callbackData("rentHome"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(button);
        SendMessage sendMessage1 = new SendMessage(curUser.getId(), "Main menyu"+"\n"+markup);
        bot.execute(sendMessage1);
         */
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
        List<MyUser> users1 = new ArrayList<>();
        users1.add(user);
        users.fileWrite(Path.of("src/main/resources/users.json"), users1);
        bot.execute(sendMessage);
    }
}
