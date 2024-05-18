package uz.pdp.backend.Services;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.jetbrains.annotations.NotNull;

public class ButtonCreator {


    @NotNull
    public InlineKeyboardMarkup inlineKeyboardMarkup(String[][] names, String[][] callbackData) {

        int x = names.length;
        int y = names[0].length;

        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[x][y];



        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                buttons[i][j] = new InlineKeyboardButton(names[i][j]).callbackData(callbackData[i][j]);
            }
        }

        return new InlineKeyboardMarkup(buttons);




    }





}
