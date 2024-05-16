package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.frontend.App;
import uz.pdp.frontend.models.MyUser;

import java.nio.file.Path;
import java.util.List;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected FileWriterAndLoader<MyUser> users = new FileWriterAndLoader<>();
    protected List<MyUser> myUsers = users.fileLoader(Path.of("src/main/resources/users.json"));

    public BaseHandler() {
        this.bot = new TelegramBot(App.BOT_TOKEN);
    }

    public abstract void handle(Update update);

    protected MyUser getOrCreateUser(User from) {
        if (myUsers!=null) {
            for (MyUser myUser : myUsers) {
                if (myUser.getId().equals(from.id())) {
                    return myUser;
                }
            }
        }
            return new MyUser(from.id(), from.firstName(), from.username(), null, null, null);
    }

    // public abstract void execute();
}
