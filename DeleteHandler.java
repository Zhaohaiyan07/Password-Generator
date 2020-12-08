package com.server;

import com.srrp.SRRPRequest;
import com.srrp.SRRPRequestHandler;
import com.srrp.SRRPResponse;

import java.sql.Connection;
import java.sql.Statement;

public class DeleteHandler implements SRRPRequestHandler {
    @Override
    public SRRPResponse handle(SRRPRequest request) {
        // prepare SQL query
        String pswDelete = request.getData();
        String query = "DELETE FROM passwords WHERE text = \"%s\"";

        query = String.format(query, pswDelete);

        // establish connection to database
        Connection connection = Database.newConnection();
        try {
            Statement statement = connection.createStatement();
            //return 'false' means if the query is an update and return no results
            boolean execute = statement.execute(query);
            //如何检测是否删除成功？
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SRRPResponse.ok("successfully add to database");
    }
}
