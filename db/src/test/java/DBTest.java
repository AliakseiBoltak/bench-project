import constants.Constants;
import model.OrderUserData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.testng.Assert.assertFalse;

class DBTest {

    private static final Logger LOGGER = LogManager.getLogger(DBTest.class);
    private static Connection conn;


    @BeforeClass
    void setUp() throws SQLException {
        LOGGER.info("Setting up DBTest class.");
        conn = DriverManager.getConnection(Constants.DB_TEST_URL, Constants.DB_TEST_USER, Constants.DB_TEST_PASSWORD);
        LOGGER.info("DB Connection established. - " + conn.getMetaData().getURL());
    }

    @Test
    void simpleDbTest() throws SQLException {
        String sql = "SELECT o.id AS order_id, u.username, u.email, o.product, o.amount, o.order_date " +
                "FROM orders o JOIN users u ON o.user_id = u.id";

        LOGGER.info("Executing SQL query: " + sql);

        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            List<OrderUserData> orderUserDataResults = new ArrayList<>();
            while (rs.next()) {
                OrderUserData data = mapRowsFromResultSet(rs);
                orderUserDataResults.add(data);
                LOGGER.info("Order found: " + data);
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