package uz.pdp.backend.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyUser {
    private Long id;
    private String name;
    private String userName;
    private String baseState;
    private String state;
    private String contact;
}
