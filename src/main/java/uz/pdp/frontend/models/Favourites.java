package uz.pdp.frontend.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Favourites {
    private UUID userId;
    private UUID homeId;
}
