package queries;

import org.example.utils.FileManager;

import static constants.Constants.SQL_QUERIES_PATH;

public class QueriesBank {

    public static final String SELECT_ORDERS_FOR_USERS_JOIN_QUERY =
            FileManager.readFileAsString(SQL_QUERIES_PATH +
                    "select_orders_for_users_join_query.sql");
}