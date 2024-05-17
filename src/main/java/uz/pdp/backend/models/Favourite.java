package uz.pdp.backend.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Favourite {
    private Long userId;
    private Long homeId;
    private UUID id;
}
