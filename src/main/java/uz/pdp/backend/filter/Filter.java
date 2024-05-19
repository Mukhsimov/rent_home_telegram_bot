package uz.pdp.backend.filter;

import uz.pdp.backend.models.Home;

@FunctionalInterface
public interface Filter<T> {
    boolean check(T t);
}
