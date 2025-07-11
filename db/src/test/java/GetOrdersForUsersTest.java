import io.qameta.allure.Allure;
import model.OrderWithUserDataRecord;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static queries.QueriesBank.SELECT_ORDERS_FOR_USERS_JOIN_QUERY;

class GetOrdersForUsersTest extends BaseDBTest {

    @Test
    void getOrdersWithUsers() throws SQLException {

        Allure.step("Executing SQL query: " + SELECT_ORDERS_FOR_USERS_JOIN_QUERY);

        try (
                Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_ORDERS_FOR_USERS_JOIN_QUERY)
        ) {
            List<OrderWithUserDataRecord> orderUserDataRecords = new ArrayList<>();
            while (rs.next()) {
                OrderWithUserDataRecord data = OrderWithUserDataRecord.mapRowsFromResultSet(rs);
                orderUserDataRecords.add(data);
                Allure.step("Order found: " + data);
            }
            assertFalse(orderUserDataRecords.isEmpty(), "No orders found in the database.");
        }
    }
}