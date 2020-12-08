package com.server;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection newConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = String.format("jdbc:mysql://%s:%s/%s",System.getenv("DB_URL"),System.getenv("DB_PORT"),System.getenv("DB_NAME"));
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
