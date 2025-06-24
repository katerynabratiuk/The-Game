package org.server.db.database_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class DbConnection {


    private static final String DB_NAME = ENV_VARS.get("POSTGRES_DB");
    private static final String PORT = ENV_VARS.get("POSTGRES_PORT");
    private static final String HOST = ENV_VARS.get("POSTGRES_HOST");
    private static final String USER = ENV_VARS.get("POSTGRES_USER");
    private static final String PASSWORD = ENV_VARS.get("POSTGRES_PASSWORD");

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

