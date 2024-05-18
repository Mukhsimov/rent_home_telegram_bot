package uz.pdp.backend.Services.userService;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.backend.models.MyUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService implements BaseService<MyUser> {
    private final FileWriterAndLoader<MyUser> fileWriterAndLoader;

    public UserService() {
        fileWriterAndLoader = new FileWriterAndLoader<>(PathConstants.USERS_PATH);
    }


    @Override
    public void create(MyUser myUser) {
        List<MyUser> users = fileWriterAndLoader.fileLoader(MyUser.class);
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(myUser);
        fileWriterAndLoader.fileWrite(users);
    }


    @Override
    public void update(MyUser myUser) {
        List<MyUser> users = fileWriterAndLoader.fileLoader(MyUser.class);

        for (int i = 0; i < users.size(); i++) {
            if (Objects.equals(users.get(i).getId(), myUser.getId())) {
                users.set(i, myUser);
                fileWriterAndLoader.fileWrite(users);
                return;
            }
        }
    }


    @Override
    public MyUser get(long id) {
        List<MyUser> users = fileWriterAndLoader.fileLoader(MyUser.class);
        if (users == null || users.isEmpty()) {
            System.out.println("eeeee");
            return null;
        }
        for (MyUser user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }




    @Override
    public void delete(long id) {
        List<MyUser> users = fileWriterAndLoader.fileLoader(MyUser.class);

        boolean removed = users.removeIf((user) -> (Objects.equals(user.getId(), id)));
        if (removed) {
            return;
        }
        throw new RuntimeException(id + ": User not found");
    }


}
