package uz.pdp.backend.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Favourite extends BaseModel{
    private Long userId;
    private Long homeId;
}
