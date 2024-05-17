package uz.pdp.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.stream.Location;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Home {
    private long id;
    private double price;
    private double square;
    private int roomCount;
    private Location location;
}
