package uz.pdp.backend.Services.favorites;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.backend.models.Favourite;

import java.util.ArrayList;
import java.util.List;

public class FavoritesService implements BaseService<Favourite> {
    FileWriterAndLoader<Favourite> fileWriterAndLoader;

    public FavoritesService() {
        this.fileWriterAndLoader = new FileWriterAndLoader<>(PathConstants.FAVORITES_PATH);
    }

    @Override
    public void create(Favourite favourite) {
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(Favourite.class);
        if (favourites == null) {
            favourites = new ArrayList<>();
        }
        favourites.add(favourite);
        fileWriterAndLoader.fileWrite(favourites);
    }

    @Override
    public void update(Favourite favourite) {
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(Favourite.class);
        int i = 0;
        for (Favourite favourite1 : favourites) {
            if (favourite.getId().equals(favourite1.getId())) {
                favourites.set(i, favourite);
            }
            i++;
        }
        fileWriterAndLoader.fileWrite(favourites);
    }


    @Override
    public Favourite get(long id) {
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(Favourite.class);
        for (Favourite favourite : favourites) {
            if (favourite.getId().equals(id)){
                return favourite;
            }
        }
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
