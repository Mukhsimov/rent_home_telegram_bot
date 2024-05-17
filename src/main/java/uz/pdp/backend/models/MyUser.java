package uz.pdp.backend.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MyUser extends BaseModel{
    private String name;
    private String userName;
    private String baseState;
    private String state;
    private String contact;
}
