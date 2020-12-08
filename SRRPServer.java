package com.srrp;

import io.zzax.jadeite.net.Connector;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SRRPServer {
    //request.action作为Map的key
    private Map<String, SRRPRequestHandler> handlers = new HashMap<String, SRRPRequestHandler>();

    public void addHandler(String action, SRRPRequestHandler handler){
        handlers.put(action, handler);
    }
    ServerSocket serverSocket = null;
    public void start(){ //启动服务器
        try {
            serverSocket = new ServerSocket(2440);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Worker worker = new Worker(socket);
            new Thread(worker).start();

        }

    }
    public class Worker implements Runnable{
        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Connector connector = new Connector(socket);

            String action = connector.readLine();
            String data = connector.readLine();
            SRRPRequest srrpRequest = new SRRPRequest(action, data);

            //这里涉及到多态，拿到了handler，我不知道具体是哪一个handler，但我知道一定能处理request
            SRRPRequestHandler srrpRequestHandler = handlers.get(srrpRequest.getAction());                //这里可以换成SRRPResponse.error("No handler found for this action")
            SRRPResponse response = srrpRequestHandler != null ? srrpRequestHandler.handle(srrpRequest) : new SRRPResponse("Error","No handler found for this action");

            connector.writeLine(response.getStatus());
            connector.writeLine(response.getData());
        }
    }
}
