package uz.pdp.frontend.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Favourite {
    private UUID userId;
    private UUID homeId;
}
