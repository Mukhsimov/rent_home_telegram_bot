package uz.pdp.backend.Services.userService;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.backend.models.MyUser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService implements BaseService<MyUser> {
    private final FileWriterAndLoader<MyUser> fileWriterAndLoader;

    public UserService() {
        fileWriterAndLoader = new FileWriterAndLoader<>();
    }


    @Override
    public void create(MyUser myUser) {
        Path userPath = Path.of(PathConstants.USERS_PATH);
        List<MyUser> users = fileWriterAndLoader.fileLoader(userPath);
        users.add(myUser);
        fileWriterAndLoader.fileWrite(userPath, users);
    }


    @Override
    public void update(MyUser myUser) {
        Path usersPath = Path.of(PathConstants.USERS_PATH);
        List<MyUser> users = fileWriterAndLoader.fileLoader(usersPath);


        for (int i = 0; i < users.size(); i++) {
            if (Objects.equals(users.get(i).getId(), myUser.getId())) {
                users.set(i, myUser);
                fileWriterAndLoader.fileWrite(usersPath, users);
                return;
            }
        }
    }


    @Override
    public MyUser get(long id) {
        Path usersPath = Path.of(PathConstants.USERS_PATH);
        List<MyUser> users = fileWriterAndLoader.fileLoader(usersPath);


        for (int i = 0; i < users.size(); i++) {
            MyUser user = users.get(i);
            if (Objects.equals(user.getId(), id)) {
                return users.get(i);
            }
        }
        return null;
    }


    @Override
    public void delete(long id) {
        Path usersPath = Path.of(PathConstants.USERS_PATH);
        List<MyUser> users = fileWriterAndLoader.fileLoader(usersPath);


        boolean removed = users.removeIf((user) -> (Objects.equals(user.getId(), id)));
        if (removed) {
            return;
        }
        throw new RuntimeException(id + ": User not found");
    }









}
