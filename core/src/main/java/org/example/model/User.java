package org.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    String usertype;
    String username;
    String password;
}