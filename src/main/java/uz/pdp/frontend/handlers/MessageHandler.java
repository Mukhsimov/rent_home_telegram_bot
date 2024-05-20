package uz.pdp.frontend.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.filter.Filter;
import uz.pdp.backend.models.Favourite;
import uz.pdp.backend.states.BaseState;
import uz.pdp.backend.states.childsStates.MainStates;
import uz.pdp.backend.models.Home;

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
        System.out.println(curUser);
        if (Objects.equals(text, "/start") && curUser.getContact() != null) {
            curUser.setState(MainStates.MENU_STATE.name());
            curUser.setBaseState(BaseState.MAIN_STATE);
            userService.update(curUser);
            mainMenyu();
        }
        contactChecker();
        String state = curUser.getState();
        switch (state) {
            case "ADD_HOME" -> {
                addHome();
            }
            case "DELETE_HOME" -> {
                deleateHome();
            }
            case "SEARCH_HOME" -> {
                searchHome(text);
            }
            case "ADD_FAVOURITES" -> {
                addFavourites();
            }
            case "DELETE_FAVOURITE" -> {
                deleteFavourite();
            }
            default -> {
                System.out.println("default");
            }
        }
    }

    private void deleateHome() {
        String text = update.message().text();
        List<Home> homes = homeService.showMy(curUser.getId());
        Home home = homes.get(Integer.parseInt(text) - 1);
        homeService.delete(home.getId());
        bot.execute(new SendMessage(curUser.getId(), "deleated successefully"));
    }

    private void addHome() {
        String text = update.message().text();
        String[] parts = text.split("-");
        double price = 0;
        double square = 0;
        int room = 0;
        if (parts.length == 3) {
            price = Double.parseDouble((parts[0]));
            square = Integer.parseInt(parts[1]);
            room = Integer.parseInt(parts[2]);
            Home build = Home.builder().userId(curUser.getId()).price(price).square(square).roomCount(room).build();
            homeService.create(build);
            bot.execute(new SendMessage(curUser.getId(), "added successefully, nigga"));
        } else {
            bot.execute(new SendMessage(curUser.getId(), "something wrong try again, nigga"));
        }
    }

    private void mainMenyu() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void contactChecker() {
        Message message = update.message();
        Contact contact = message.contact();
        User from = message.from();
        if (contact != null) {
            curUser.setContact(contact.phoneNumber());
            userService.update(curUser);
            bot.execute(new SendMessage(from.id(), "you are registred successefully"));
            curUser.setBaseState(BaseState.MAIN_STATE);
            curUser.setState(String.valueOf(MainStates.MENU_STATE));
            mainMenyu();

        } else if (curUser.getContact() == null) {
            curUser.setBaseState((BaseState.MAIN_STATE));
            curUser.setState(String.valueOf(MainStates.REGISTER_STATE));
            SendMessage register = messageMaker.register(curUser);
            bot.execute(register);
        }
    }

    private void searchHome(String text) {
        List<Home> homes = custom(text);
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Home home : homes) {
            i++;
            stringBuilder.append(i)
                    .append(". ")
                    .append(home.toString());
        }
        bot.execute(new SendMessage(curUser.getId(), stringBuilder.toString()));
    }

    private void deleteFavourite() {
        String text = update.message().text();
        List<Favourite> byUser = favoritesService.getByUser(curUser.getId());
        Favourite favourite1 = byUser.get(Integer.parseInt(text)-1);
        Favourite favourite = favoritesService.get(favourite1.getId());
        favoritesService.delete(favourite.getId());
        bot.execute(new SendMessage(curUser.getId(), "successefully deleted"));
    }

    private void addFavourites() {
        String text = update.message().text();
        List<Home> homes = custom(text);
        for (Home home : homes) {
            Favourite.FavouriteBuilder favouriteBuilder = Favourite.builder().userId(curUser.getId()).homeId(home.getId());
            favoritesService.create(favouriteBuilder.build());
        }
        bot.execute(new SendMessage(curUser.getId(), "added successefully"));
    }


    private List<Home> custom(String text) {
        String[] split = text.split("\\. ");
        int option = Integer.parseInt(split[0]);
        int value = Integer.parseInt(split[1]);
        Filter<Home> filter;
        if (option == 1) {
            filter = (home) -> home.getRoomCount() == value;
        } else if (option == 2) {
            filter = (home) -> home.getSquare() <= value;
        } else if (option == 3) {
            filter = (home) -> home.getPrice() <= value;
        } else {
            System.out.println("user entered incorrect data");
            filter = (home) -> false;
        }

        return homeService.getHomesByFilter(filter);

    }

    private String homeToString(Home home) {
        double price = home.getPrice();
        double square = home.getSquare();
        int roomCount = home.getRoomCount();
        photoService.getPhotosByHomeID(home.getId());
        return null;
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