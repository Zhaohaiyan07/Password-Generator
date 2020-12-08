package com.common;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordORM {
    public static Password toObject(ResultSet resultSet){
        Password password = new Password();
        try {
            password.setText(resultSet.getString("text"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        try {
            password.setStar(resultSet.getBoolean("star"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return password;
    }
}
