package uz.pdp.frontend.Services;

import uz.pdp.frontend.models.MyUser;

public interface BaseService<T> {
    // create, delete, update, get
    void create(T t);

    void update(T t);

    T get(long id);

    void delete(long id);

}
