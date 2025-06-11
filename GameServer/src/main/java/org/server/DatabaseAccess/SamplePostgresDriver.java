package org.server.DatabaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.lib.Environment.EnvLoader.ENV_VARS;

public class SamplePostgresDriver {
    public static void TestConnection() {
        String dbName = ENV_VARS.get("POSTGRES_DB");
        String port = ENV_VARS.get("POSTGRES_PORT");
        String host = ENV_VARS.get("POSTGRES_HOST");
        String password = ENV_VARS.get("POSTGRES_PASSWORD");
        String user = ENV_VARS.get("POSTGRES_USER");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to the database!");

            ResultSet rs = stmt.executeQuery("SELECT version()");
            while (rs.next()) {
                System.out.println("PostgreSQL version: " + rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


