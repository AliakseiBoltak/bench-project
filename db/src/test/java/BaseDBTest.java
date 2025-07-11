import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.example.constants.Constants.ENV;

public abstract class BaseDBTest {

    protected static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    private static final Logger LOGGER = LogManager.getLogger(BaseDBTest.class);

    public Connection getConnection() {
        return threadLocalConnection.get();
    }

    @BeforeClass
    public static void setUpClass() {
        ConfigLoader loader = new ConfigLoader(ENV);
        try {
            Connection conn = DriverManager.getConnection(
                    loader.getDbUrl(),
                    loader.getDbUsername(),
                    loader.getDbPassword()
            );
            threadLocalConnection.set(conn);
            LOGGER.info("DB Connection established. - " + conn.getMetaData().getURL());
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
                    LOGGER.info("DB Connection closed");
                }
            } catch (SQLException e) {
                LOGGER.error("Failed to close DB Connection: " + e.getMessage());
            } finally {
                threadLocalConnection.remove();
            }
        }
    }
}
