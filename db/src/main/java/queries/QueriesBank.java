package queries;

import org.example.utils.FileManager;

import static constants.Constants.SQL_QUERIES_PATH;

public class QueriesBank {

    public static final String SELECT_ORDERS_WITH_USERS_JOIN_QUERY =
            FileManager.readFileAsString(SQL_QUERIES_PATH +
                    "select_orders_with_user_join_query.sql");
}