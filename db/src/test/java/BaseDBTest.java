import constants.Constants;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDBTest {

    protected static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    private static final Logger LOGGER = LogManager.getLogger(BaseDBTest.class);

    public Connection getConnection() {
        return threadLocalConnection.get();
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            Connection conn = DriverManager.getConnection(
                    Constants.DB_TEST_URL,
                    Constants.DB_TEST_USER,
                    Constants.DB_TEST_PASSWORD
            );
            threadLocalConnection.set(conn);
            Allure.step("DB Connection established. - " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            LOGGER.error("Failed to establish DB Connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        Connection conn = threadLocalConnection.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    Allure.step("DB Connection closed");
                }
            } catch (SQLException e) {
                LOGGER.error("Failed to close DB Connection: " + e.getMessage());
            } finally {
                threadLocalConnection.remove();
            }
        }
    }
}
