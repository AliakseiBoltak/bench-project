package model.responses;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {
    private int id;
    private String name;
    private String job;
    private String createdAt;

}
