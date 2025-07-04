package model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderUserData {
    private int orderId;
    private String username;
    private String email;
    private String product;
    private double amount;
    private Date orderDate;
}
