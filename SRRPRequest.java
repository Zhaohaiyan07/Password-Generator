package com.srrp;

public class SRRPRequest {
    private String action;
    private String data;

    public SRRPRequest() {
    }

    public SRRPRequest(String action, String data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
