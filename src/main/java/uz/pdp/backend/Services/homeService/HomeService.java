package uz.pdp.backend.Services.homeService;

import uz.pdp.backend.Services.BaseService;
import uz.pdp.backend.filter.Filter;
import uz.pdp.backend.models.Favourite;
import uz.pdp.backend.models.Home;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.file_writer_and_loader.FileWriterAndLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<Home> show() {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        for (int i = 0; i < homes.size(); i++) {
            System.out.println(i + 1 + " " +homes);
        }
        return homes;
    }

    public List<Home> showFavourites(List<Favourite> favourites){
        List<Home> homes = new ArrayList<>();
        List<Home> homes1 = loadAndWriter.fileLoader(Home.class);
        for (Home home : homes1) {
            Long id = home.getId();
            for (Favourite favourite : favourites) {
                if(favourite.getHomeId().equals(id)){
                    homes.add(home);
                }
            }
        }
        return homes;
    }

    public List<Home> showMy(long id) {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        List<Home> homes1 = new ArrayList<>();
        for (Home home : homes) {
            if (home.getUserId() == id) {
                homes1.add(home);
            }
        }
        return homes1;
    }

    @Override
    public void update(Home home) {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        for (int i = 0; i < homes.size(); i++) {
            if (Objects.equals(homes.get(i).getId(), home.getId())) {
                homes.set(i, home);
                return;
            }
        }
    }

    @Override
    public Home get(long id) {
        for (Home home : loadAndWriter.fileLoader(Home.class)) {
            if (Objects.equals(home.getId(), id)) return home;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        boolean b = homes.removeIf(home -> Objects.equals(home.getId(), id));
        if(b){
            loadAndWriter.fileWrite(homes);
        }
    }


    public List<Home> getHomesByFilter(Filter<Home> filter) {
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        List<Home> result = new ArrayList<>();
        for (Home home : homes) {
            if (filter.check(home)){
                result.add(home);
            }
        }
        return result;
    }

    public void deleateAccount(long id){
        List<Home> homes = loadAndWriter.fileLoader(Home.class);
        for (Home home : homes) {
            if(home.getUserId()==id){
                delete(home.getId());
            }
        }
    }
}
