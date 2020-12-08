package com.server;

import com.common.Password;
import com.google.gson.Gson;
import com.srrp.SRRPRequest;
import com.srrp.SRRPRequestHandler;
import com.srrp.SRRPResponse;

import java.sql.Connection;
import java.sql.Statement;

public class AddHandler implements SRRPRequestHandler {

    @Override
    public SRRPResponse handle(SRRPRequest request) {
        //Request -> JSON
        String data = request.getData();

        //JSON -> Password Object
        Password password = new Gson().fromJson(data, Password.class);

        //Password Object -> SQL
        //1.先获得sortid的最大值
        String query = "SELECT sortid WHERE ";


        //2.
        query = "INSERT INTO passwords(text,id) VALUES (\"%s\",%d)";
        query = String.format(query,password.getText());

        // SQL -> send
        Connection connection = Database.newConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        }catch(Exception e){
            e.printStackTrace();
        }

        return SRRPResponse.ok("successfully add to database");
    }
}
