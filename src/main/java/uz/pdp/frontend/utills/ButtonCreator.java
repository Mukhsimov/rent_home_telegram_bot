package uz.pdp.frontend.utills;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.jetbrains.annotations.NotNull;

public class ButtonCreator {


    @NotNull
    public InlineKeyboardMarkup inlineKeyboardMarkup(String[][] names, String[][] callbackData, String text) {

        InlineKeyboardButton[][] result = new InlineKeyboardButton[2][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                result[i][j] = new InlineKeyboardButton(names[i][j]).callbackData(callbackData[i][j]);
            }
        }

        return new InlineKeyboardMarkup(result);
    }





}
