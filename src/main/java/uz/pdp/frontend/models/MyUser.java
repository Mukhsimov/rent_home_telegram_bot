package uz.pdp.frontend.models;

import lombok.*;
import uz.pdp.frontend.enums.states.BaseState;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MyUser {
    private UUID id;
    private String name;
    private String userName;
    private String baseState;
    private String state;
}
