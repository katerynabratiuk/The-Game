package com.example.game.Server.DatabaseAccess;

import com.example.game.Server.EnvLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class SamplePostgresDriver {
    public static void TestConnection() {
        Map<String, String> dotenv = EnvLoader.loadEnv(".env");

        String dbName = dotenv.get("POSTGRES_DB");
        String port = dotenv.get("POSTGRES_PORT");
        String host = dotenv.get("POSTGRES_HOST");
        String password = dotenv.get("POSTGRES_PASSWORD");
        String user = dotenv.get("POSTGRES_USER");

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


