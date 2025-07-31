package model.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {
    int id;
    String name;
    String job;
    String createdAt;
}