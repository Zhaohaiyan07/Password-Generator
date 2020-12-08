package com.server;

import com.srrp.SRRPServer;

public class Driver {
    public static void main(String[] args) {
        SRRPServer srrpServer = new SRRPServer();
        srrpServer.addHandler("add",new AddHandler());
        srrpServer.addHandler("get",new GetHandler());
        srrpServer.addHandler("delete",new DeleteHandler());
        srrpServer.addHandler("moveUp",new MoveUpHandler());
        srrpServer.addHandler("moveDown",new MoveDownHandler());
        srrpServer.addHandler("star",new StarHandler());
        srrpServer.addHandler("unstar",new UnStarHandler());
        srrpServer.start();
    }
}
