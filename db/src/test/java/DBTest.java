import io.qameta.allure.Allure;
import model.OrderUserData;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static queries.QueriesBank.SELECT_ORDERS_WITH_USERS_JOIN_QUERY;

class DBTest extends BaseDBTest {

    @Test
    void getOrdersWithUsers() throws SQLException {

        Allure.step("Executing SQL query: " + SELECT_ORDERS_WITH_USERS_JOIN_QUERY);

        try (
                Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_ORDERS_WITH_USERS_JOIN_QUERY)
        ) {
            List<OrderUserData> orderUserDataResults = new ArrayList<>();
            while (rs.next()) {
                OrderUserData data = mapRowsFromResultSet(rs);
                orderUserDataResults.add(data);
                Allure.step("Order found: " + data);
            }
            assertFalse(orderUserDataResults.isEmpty(), "No orders found in the database.");
        }
    }

    private OrderUserData mapRowsFromResultSet(ResultSet rs) throws SQLException {
        return new OrderUserData(
                rs.getInt("order_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("product"),
                rs.getDouble("amount"),
                rs.getDate("order_date")
        );
    }
}