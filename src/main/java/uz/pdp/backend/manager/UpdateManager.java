package uz.pdp.backend.manager;

import com.pengrad.telegrambot.model.Update;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.handlers.BaseHandler;
import uz.pdp.backend.handlers.CallBackQueryHandler;
import uz.pdp.backend.handlers.MessageHandler;

public class UpdateManager {
    private final BaseHandler messageHandler;
    private final BaseHandler callBackQuery;
    // private BaseState curState = BaseState.REGISTER_STATE;

    public UpdateManager() {
        callBackQuery = new CallBackQueryHandler();
        messageHandler = new MessageHandler();
    }


    public void manage(Update update) {


        if (update.message() != null) {
            messageHandler.handle(update);
        } else if (update.callbackQuery() != null) {
            callBackQuery.handle(update);
        } else {
            System.err.println("check there!!!");
        }





    }


}
