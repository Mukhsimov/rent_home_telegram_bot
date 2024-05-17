package uz.pdp.backend.models;

import com.pengrad.telegrambot.model.User;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Favourite {
    private Long userId;
    private Long homeId;
    private Long id;
}
