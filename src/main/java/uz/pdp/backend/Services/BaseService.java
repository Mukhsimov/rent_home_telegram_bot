package uz.pdp.backend.Services;

public interface BaseService<T> {
    // create, delete, update, get
    void create(T t);

    void update(T t);

    T get(long id);

    void delete(long id);

}
