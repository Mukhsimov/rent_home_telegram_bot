package uz.pdp.backend.Services.photo;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.models.Photo;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhotoService implements BaseService<Photo> {
    private final FileWriterAndLoader<Photo> file;

    public PhotoService() {
        this.file = new FileWriterAndLoader<>(PathConstants.PHOTOS_PATH);
    }

    @Override
    public void create(Photo photo) {
        List<Photo> photos = file.fileLoader(Photo.class);
        if (photos == null) photos = new ArrayList<>();
        photos.add(photo);
        file.fileWrite(photos);
    }

    @Override
    public void update(Photo photo) {

        List<Photo> photos = file.fileLoader(Photo.class);

        photos.removeIf((photo1) -> Objects.equals(photo, photo1));

        file.fileWrite(photos);
    }

    @Override
    public Photo get(long id) {
        List<Photo> photos = file.fileLoader(Photo.class);
        for (Photo photo : photos) {
            if (photo.getId().equals(id))
                return photo;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        List<Photo> photos = file.fileLoader(Photo.class);
        photos.removeIf((photo -> photo.getId().equals(id)));
    }

    public List<Photo> getPhotosByHomeID(Long homeID) {
        List<Photo> photos = file.fileLoader(Photo.class);
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos) {
            if (photo.getHomeId().equals(homeID)) {
                result.add(photo);
            }
        }
        return result;
    }
}
