import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.guice.ConfigModule;
import org.example.interfaces.ResultSetMapper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.constants.Constants.ENV;

public abstract class BaseDBTest {

    protected static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    protected ConfigLoader configLoader;
    private static final Logger LOGGER = LogManager.getLogger(BaseDBTest.class);

    public Connection getConnection() {
        return threadLocalConnection.get();
    }

    @BeforeClass
    public void setUp() {
        Injector injector = Guice.createInjector(new ConfigModule(ENV));
        configLoader = injector.getInstance(ConfigLoader.class);
        try {
            Connection conn = DriverManager.getConnection(
                    configLoader.getDbUrl(),
                    configLoader.getDbUsername(),
                    configLoader.getDbPassword()
            );
            threadLocalConnection.set(conn);
            LOGGER.info("DB Connection established. - " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            LOGGER.error("Failed to establish DB Connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public void tearDown() {
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

    protected <T> List<T> executeQueryAndMapResult(String query, ResultSetMapper<ResultSet, T> mapper) throws SQLException {
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper.apply(rs));
            }
            return result;
        }
    }
}