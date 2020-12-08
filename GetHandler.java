package com.server;

import com.common.Password;
import com.common.PasswordORM;
import com.google.gson.Gson;
import com.srrp.SRRPRequest;
import com.srrp.SRRPRequestHandler;
import com.srrp.SRRPResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class GetHandler implements SRRPRequestHandler {
    @Override
    public SRRPResponse handle(SRRPRequest request) {
        // prepare the SQL query
        String query = "SELECT * FROM passwords";

        // establish the connection
        Connection connection = Database.newConnection();
        SRRPResponse response = new SRRPResponse();
        try {
            //execute the query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // prepare the data sent back to Client
            LinkedList<Password> passwords = new LinkedList<>();
            while(resultSet.next()){
                Password password = PasswordORM.toObject(resultSet);
                passwords.add(password);
            }

            String psw = new Gson().toJson(passwords);

            response.setStatus("ok");
            response.setData(psw);

        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
