package com.server;

import com.srrp.SRRPRequest;
import com.srrp.SRRPRequestHandler;
import com.srrp.SRRPResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MoveDownHandler implements SRRPRequestHandler {
    @Override
    public SRRPResponse handle(SRRPRequest request) {
        //prepare SQL query
        String pswMoveDown = request.getData();
        String query = "SELECT id FROM passwords WHERE text = \"%s\"";

        // establish a connection with database
        Connection connection = Database.newConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int id = resultSet.getInt("id");

            // move the row up
            query = "UPDATE passwords SET id = %d WHERE id = %d";
            query = String.format(query,id,id+1);
            statement.execute(query);

            //move the row down
            query = "UPDATE passwords SET id = %d WHERE text = \"%s\"";
            query = String.format(query,id+1,pswMoveDown);
            statement.execute(query);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
