package model;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithUserDataRecord {
    int orderId;
    String username;
    String email;
    String product;
    double amount;
    Date orderDate;

    public static OrderWithUserDataRecord mapRowsFromResultSet(ResultSet rs) throws SQLException {
        return OrderWithUserDataRecord.builder()
                .orderId(rs.getInt("order_id"))
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .product(rs.getString("product"))
                .amount(rs.getDouble("amount"))
                .orderDate(rs.getDate("order_date"))
                .build();
    }
}