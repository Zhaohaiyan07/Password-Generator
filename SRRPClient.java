package com.srrp;

import io.zzax.jadeite.net.Connector;

import java.io.IOException;
import java.net.Socket;

public class SRRPClient {
    private String urlDestination;

    public SRRPClient() {
    }

    public SRRPClient(String urlDestination) {
        this.urlDestination = urlDestination;
    }

    public SRRPResponse send(SRRPRequest request){
        Socket socket = null;
        try {
            socket = new Socket(urlDestination, 2440);
        } catch (IOException e) {
            e.printStackTrace();
            return SRRPResponse.error("no connection");
        }
        Connector connector = new Connector(socket);

        connector.writeLine(request.getAction());
        connector.writeLine(request.getData());

        String status = connector.readLine();
        String result = connector.readLine();

        SRRPResponse srrpResponse = new SRRPResponse(status,result);
        return srrpResponse;
    }

    public String getUrlDestination() {
        return urlDestination;
    }

    public void setUrlDestination(String urlDestination) {
        this.urlDestination = urlDestination;
    }


}
