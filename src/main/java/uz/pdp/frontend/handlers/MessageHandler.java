package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.frontend.utills.ButtonCreator;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Thread thread = Thread.currentThread();
        Message message = update.message();
        User from = message.from();
        String text = message.text();
        String send = null;
        if(text.equals("/start")){
            send = "---SEND CONTACT---";
        }else if(){

        }
        SendMessage sendMessage = new SendMessage(from.id(), send);

        ButtonCreator buttonCreator = new ButtonCreator();

        String btnName

        buttonCreator.inlineKeyboardMarkup()



/*
        //  exo message:
        SendMessage sendMessage = new SendMessage(message.chat().id(), message.text());
        bot.execute(sendMessage);
*/

        // show sent message
        System.out.println(thread.getName() + '\t' + from.firstName() + "\t " + "is_bot: " + from.isBot() + '\t' + "message: " + text);


    }
}
