package model.requests;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String name;
    private String job;
}
