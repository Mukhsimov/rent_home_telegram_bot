package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.frontend.enums.states.BaseState;
import uz.pdp.frontend.enums.states.childsStates.RegisterStates;
import uz.pdp.frontend.models.MyUser;
import uz.pdp.frontend.utills.ButtonCreator;

import java.nio.file.Path;
import java.util.List;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Thread thread = Thread.currentThread();
        Message message = update.message();
        User from = message.from();
        String text = message.text();
        String send = null;
        curUser = new MyUser(from.id(), from.firstName(), from.username(), null, null, null);
        if (text.equals("/start")) {
            MyUser user = null;
            for (MyUser myUser : myUsers) {
                if (myUser.getId().equals(curUser.getId())) {
                    user = myUser;
                }
            }
            if (user != null) {
                register(user);
                curUser.setState(String.valueOf(BaseState.REGISTER_STATE));
                curUser.setState(String.valueOf(RegisterStates.REGISTER_STATE));
            } else {
                mainMenyu();
                curUser.setState(String.valueOf(BaseState.REGISTER_STATE));
                curUser.setState(String.valueOf(RegisterStates.MAIN_MENYU));
            }
        }
        /*else if () {

        }*/

        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);
    }

    private void mainMenyu() {
        InlineKeyboardButton[][] button = {
                {
                        new InlineKeyboardButton("rent out home").callbackData("rentOutHome"),
                        new InlineKeyboardButton("rent home").callbackData("rentHome"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(button);
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Main menyu"+"\n"+markup);
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
        users.fileWrite(Path.of("src/main/resources/users.json"), (List<MyUser>) user);
    }
}
