package com.server;

import com.srrp.SRRPRequest;
import com.srrp.SRRPRequestHandler;
import com.srrp.SRRPResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UnStarHandler implements SRRPRequestHandler {
    @Override
    public SRRPResponse handle(SRRPRequest request) {
        String password = request.getData();
        String query = "UPDATE passwords SET star = false WHERE text = \"%s\"";
        query = String.format(query, password);

        Connection connection = Database.newConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return SRRPResponse.ok("successfully unstar the password!");
    }
}
