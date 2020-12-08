package com.srrp;

public class SRRPResponse {
    private String status = "ok";
    private String data = "";

    public static final String statusOK = "ok"; //尽量不要手动输入，否则client如果利用这个来作为判断标志的话，会增加出错几率
    public static final String statusError = "error";

    public boolean isOk(){
        return status.equals(statusOK);
    }

    public static SRRPResponse ok(String data){ //只是把下面这一行new给包成一个静态函数，之后调用比较方便
        return new SRRPResponse(statusOK,data);
    }

    public static SRRPResponse error(String data){
        return new SRRPResponse(statusError,data);
    }

    public SRRPResponse() {
    }

    public SRRPResponse(String status, String data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
