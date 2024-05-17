package uz.pdp.backend.models;

import lombok.*;


@AllArgsConstructor
@Getter
public class Favourite extends BaseModel{
    private Long userId;
    private Long homeId;
}