package uz.pdp.backend.Services.homeService;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.models.Home;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeService implements BaseService<Home> {
    private final FileWriterAndLoader<Home> loadAndWriter;

    public HomeService() {
        this.loadAndWriter = new FileWriterAndLoader<>(PathConstants.HOMES_PATH);
    }

    @Override
    public void create(Home home) {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        if (homes == null) {
            homes = new ArrayList<>();
        }
        homes.add(home);
        loadAndWriter.fileWrite(homes);
    }

    @Override
    public void update(Home home) {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        for (int i = 0; i < homes.size(); i++) {

        }
    }

    @Override
    public Home get(long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
