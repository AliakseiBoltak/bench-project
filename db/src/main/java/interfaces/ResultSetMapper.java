package interfaces;

import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapper<T, R> {
    R apply(T t) throws SQLException;
}
