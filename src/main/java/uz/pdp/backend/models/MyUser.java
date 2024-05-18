package uz.pdp.backend.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.pdp.backend.states.BaseState;

import java.lang.reflect.Field;

@Getter
@Setter
@ToString
public class MyUser extends BaseModel {
    private String name;
    private String userName;
    private BaseState baseState;
    private String state;
    private String contact;

    @Builder
    public MyUser(Long id, String name, String userName, BaseState baseState, String state, String contact) {
        setIdReflectively(id);
        this.name = name;
        this.userName = userName;
        this.baseState = baseState;
        this.state = state;
        this.contact = contact;
    }

    public void setIdReflectively(Long id) {
        try {
            Field field = BaseModel.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(this, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to set ID reflectively", e);
        }
    }

    public void setID(Long id) {
        setIdReflectively(id);
    }
}