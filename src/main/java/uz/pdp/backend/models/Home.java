package uz.pdp.backend.models;

import lombok.*;

import javax.xml.stream.Location;
import java.util.Base64;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class Home extends BaseModel {
    private double price;
    private double square;
    private int roomCount;
    private Location location;
}
