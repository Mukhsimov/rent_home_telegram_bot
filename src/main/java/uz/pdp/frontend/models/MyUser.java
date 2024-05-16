package uz.pdp.frontend.models;

import com.pengrad.telegrambot.model.Contact;
import lombok.*;
import uz.pdp.frontend.enums.states.BaseState;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyUser {
    private Long id;
    private String name;
    private String userName;
    private String baseState;
    private String state;
    private Contact contact;
}
