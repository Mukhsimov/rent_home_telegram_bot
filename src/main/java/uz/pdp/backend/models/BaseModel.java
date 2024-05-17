package uz.pdp.backend.models;


import lombok.Getter;

@Getter
public abstract class BaseModel {
    private final Long id;

    public BaseModel() {
        this.id = System.currentTimeMillis()/2;
    }
}
