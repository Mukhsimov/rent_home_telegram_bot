package uz.pdp.backend.Services.favorites;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;
import uz.pdp.backend.models.Favourite;

import java.nio.file.Path;
import java.util.List;

public class FavoritesService implements BaseService<Favourite> {
    FileWriterAndLoader<Favourite> fileWriterAndLoader;

    public FavoritesService() {
        this.fileWriterAndLoader = new FileWriterAndLoader<>();
    }

    @Override
    public void create(Favourite favourite) {
        Path path = Path.of(PathConstants.FAVORITES_PATH);
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(path);
        int i=0;
        for (Favourite favourite1 : favourites) {
            if(favourite.getId().equals(favourite1.getId())){
                i++;
            }
        }
        if(i==0) {
            favourites.add(favourite);
        }
        fileWriterAndLoader.fileWrite(path, favourites);
    }

    @Override
    public void update(Favourite favourite) {
        Path usersPath = Path.of(PathConstants.FAVORITES_PATH);
        List<Favourite> favourites = fileWriterAndLoader.fileLoader(usersPath);
        int i=0;
        for (Favourite favourite1 : favourites) {
            if(favourite.getId().equals(favourite1.getId())){
                favourites.set(i, favourite);
            }
            i++;
        }
        fileWriterAndLoader.fileWrite(usersPath, favourites);
    }

    @Override
    public Favourite get(long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
