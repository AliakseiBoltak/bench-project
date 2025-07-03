package model.responses;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {
    private String id;
    private String name;
    private String job;
    private String createdAt;

}
