package uz.pdp.frontend.Services.favorites;

import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.frontend.Services.BaseService;
import uz.pdp.frontend.models.Favourite;
import uz.pdp.frontend.models.MyUser;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class FavoritesService implements BaseService<Favourite> {
    FileWriterAndLoader<Favourite> fileWriterAndLoader;

    public FavoritesService() {
        this.fileWriterAndLoader = new FileWriterAndLoader<>();
    }

    @Override
    public void create(Favourite favourite) {
        Path path = Path.of(PathConstants.FAVORITES_PATH);
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(path);
        boolean add = favourites.add(favourite);
        if (add) {
            return;
        }
        throw new RuntimeException("Favorites is not added");
    }

    @Override
    public void update(Favourite favourite) {
   /*     Path usersPath = Path.of(PathConstants.FAVORITES_PATH);
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(usersPath);


        for (int i = 0; i < favourites.size(); i++) {
            if (Objects.equals(favourites.get(i).getId(), myUser.getId())) {
                users.set(i, myUser);
                return;
            }
        }
        fileWriterAndLoader.fileWrite(usersPath, users);*/
    }

    @Override
    public Favourite get(long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
