import io.qameta.allure.Allure;
import model.OrderWithUserDataRecord;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static queries.QueriesBank.SELECT_ORDERS_FOR_USERS_JOIN_QUERY;

class GetOrdersForUsersTest extends BaseDBTest {

    @Test (description = "Test to retrieve orders with user data from the database")
    void getOrdersWithUsers() throws SQLException {

        Allure.step("Executing SQL query: " + SELECT_ORDERS_FOR_USERS_JOIN_QUERY);

        List<OrderWithUserDataRecord> orderUserDataRecords = executeQueryAndMapResult(
                SELECT_ORDERS_FOR_USERS_JOIN_QUERY,
                OrderWithUserDataRecord::mapRowsFromResultSet
        );
        orderUserDataRecords.forEach(data -> Allure.step("Order found: " + data));
        assertFalse(orderUserDataRecords.isEmpty(), "No orders found in the database.");
    }
}