package uz.pdp.backend.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Favourite extends BaseModel{
    private Long userId;
    private Long homeId;
}
