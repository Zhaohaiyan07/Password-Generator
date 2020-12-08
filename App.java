package com.client;

import com.common.Password;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srrp.SRRPClient;
import com.srrp.SRRPRequest;
import com.srrp.SRRPResponse;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Random;

public class App extends Application {

    private LinkedList<Password> passwords = new LinkedList<Password>();
    private Pane listView = new Pane();
    int labelWidth = 80;
    int buttonWidth = 60;


    @Override
    public void start(Stage primaryStage) throws Exception {
        int width = 400;
        int height = 600;
        int margin = 10;

        Pane view = new Pane();

        Pane controlView = new Pane();
        int controlViewHeight = 60;
        controlView.setPrefHeight(controlViewHeight);
        controlView.setLayoutY(height - controlView.getPrefHeight());
        controlView.setPrefWidth(width);
        view.getChildren().add(controlView);

        listView.setPrefWidth(width);
        listView.setPrefHeight(height-controlViewHeight);
        view.getChildren().add(listView);

        //数据库 -> 内存：整体更新
        initAppData();

        Button btnAdd = new Button("新建密码");
        btnAdd.setLayoutX(margin);
        btnAdd.setLayoutY(margin);
        btnAdd.setPrefWidth(controlView.getPrefWidth() - margin*2);
        btnAdd.setPrefHeight(controlView.getPrefHeight() - margin*2);
        controlView.getChildren().add(btnAdd);

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                int textFieldWidth = 80;
                int height = 30;

                Stage popUpStage = new Stage();
                Pane popUpPane = new Pane();
                Scene popUpScene = new Scene(popUpPane, 200, 200);
                popUpStage.setScene(popUpScene);
                popUpStage.show();

                Button btnRandom = new Button("随机");
                Button btnCustomize = new Button("自定义");
                TextField textField = new TextField();

                btnRandom.setLayoutX(margin);
                btnRandom.setLayoutY(margin);
                btnRandom.setPrefWidth(buttonWidth);
                btnRandom.setPrefHeight(height);
                popUpPane.getChildren().add(btnRandom);

                textField.setLayoutX(margin);
                textField.setLayoutY(margin * 2 + btnRandom.getPrefHeight());
                textField.setPrefWidth(textFieldWidth);
                textField.setPrefHeight(height);
                popUpPane.getChildren().add(textField);

                btnCustomize.setLayoutX(margin * 2 + textField.getPrefWidth());
                btnCustomize.setLayoutY(margin * 2 + btnRandom.getPrefHeight());
                btnCustomize.setPrefWidth(buttonWidth);
                btnCustomize.setPrefHeight(height);
                popUpPane.getChildren().add(btnCustomize);

                btnRandom.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //prepare data
                        String passwordText = generatePSW();
                        Password password = new Password(passwordText);

                        //用户 -> 内存：改变更新
                        passwords.add(password);

                        //内存 -> 界面
                        dataToView();

                        //内存 -> 数据库
                        //同时更新 + 局部更新
                        //1. Password 变成 JSON String
                        String passwordJson = new Gson().toJson(password);


                        new Thread(){
                            @Override
                            public void run() {
                                //2. JSON String 变成 Request
                                SRRPRequest srrpRequest = new SRRPRequest();
                                srrpRequest.setAction("add");
                                srrpRequest.setData(passwordJson);

                                //发送
                                SRRPResponse srrpResponse = new SRRPClient().send(srrpRequest);
                            }
                        }.start();
                    }
                });

                btnCustomize.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String passwordText = textField.getText();
                        Password password = new Password(passwordText);

                        //用户 -> 内存：改变更新
                        passwords.add(password);

                        //内存 -> 界面
                        dataToView();

                        String passwordJson = new Gson().toJson(password);
                        new Thread(){
                            @Override
                            public void run() {
                                //2. JSON String 变成 Request
                                SRRPRequest srrpRequest = new SRRPRequest();
                                srrpRequest.setAction("add");
                                srrpRequest.setData(passwordJson);

                                //发送
                                SRRPResponse srrpResponse = new SRRPClient().send(srrpRequest);
                            }
                        }.start();
                    }
                });
            }
        });

        //内存 -> 界面：
        dataToView();

        Scene scene = new Scene(view,width,height);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    //App新打开，从数据库中读取数据到内存中来
    private void initAppData(){
        SRRPRequest srrpRequest = new SRRPRequest();
        srrpRequest.setAction("get");
        SRRPResponse srrpResponse = new SRRPClient().send(srrpRequest);
        String psw = srrpResponse.getData();

        Type type = new TypeToken<LinkedList<Password>>() {}.getType();//泛型是对象类型的LinkedList获取class
        LinkedList linkedList = new Gson().fromJson(psw, type);//转成LinkedList<Password>类型
        passwords = linkedList;
    }



    private void dataToView(){
        //delete old data
        listView.getChildren().clear();

        //generate new data
        int margin = 10;
        int height = 30;
        for (int i = 0; i < passwords.size(); i++) {
            Password password = passwords.get(i);
            String passwordText = password.getText();

            Label label = new Label();
            if(password.isStar()){
                label.setText("⭐" + passwordText);
            }else{
                label.setText(passwordText);
            }

            label.setLayoutX(margin);
            label.setPrefWidth(labelWidth);
            label.setLayoutY(i*(margin+height)+margin);
            label.setPrefHeight(height);
            listView.getChildren().add(label);

            Button btnDelete = new Button();
            btnDelete.setText("删除");
            btnDelete.setLayoutX(margin*2 + label.getPrefWidth());
            btnDelete.setLayoutY(i*(margin+height)+margin);
            btnDelete.setPrefWidth(buttonWidth);
            btnDelete.setPrefHeight(height);
            listView.getChildren().add(btnDelete);

            Button btnUp = new Button();
            btnUp.setText("往上移");
            btnUp.setLayoutX(margin*3 + label.getPrefWidth() + btnDelete.getPrefWidth());
            btnUp.setLayoutY(i*(margin+height)+margin);
            btnUp.setPrefWidth(buttonWidth);
            btnUp.setPrefHeight(height);
            listView.getChildren().add(btnUp);

            Button btnDown = new Button();
            btnDown.setText("往下移");
            btnDown.setLayoutX(margin*4 + label.getPrefWidth() + btnDelete.getPrefWidth() + btnUp.getPrefWidth());
            btnDown.setLayoutY(i*(margin+height)+margin);
            btnDown.setPrefWidth(buttonWidth);
            btnDown.setPrefHeight(height);
            listView.getChildren().add(btnDown);

            Button btnStar = new Button();
            if(password.isStar()){
                btnStar.setText("去星标");
            }else{
                btnStar.setText("星标");
            }
            btnStar.setLayoutX(margin*5 + label.getPrefWidth() + btnDelete.getPrefWidth() + btnUp.getPrefWidth() + btnDown.getPrefWidth());
            btnStar.setLayoutY(i*(margin+height)+margin);
            btnStar.setPrefWidth(buttonWidth);
            btnStar.setPrefHeight(height);
            listView.getChildren().add(btnStar);

            btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    //remove passwordText from memory
                    passwords.remove(password);

                    //remove passwordText
                    dataToView();

                    new Thread(){
                        @Override
                        public void run() {
                            //2. JSON String 变成 Request
                            SRRPRequest request = new SRRPRequest();
                            request.setAction("delete");
                            request.setData(passwordText);

                            //发送
                            new SRRPClient().send(request);
                        }
                    }.start();
                }
            });

            btnUp.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (int j = 0; j < passwords.size(); j++) {
                        Password password = passwords.get(j);
                        if (password.getText().equals(passwordText)) {
                            int destinatedIndex = j-1;
                            if(destinatedIndex < 0){
                                System.out.println("move out of range!");
                            }else{
                                //修改内存
                                Password psw = passwords.get(destinatedIndex);
                                passwords.set(destinatedIndex,password);
                                passwords.set(j,psw);

                                //修改数据库
                                new Thread(){
                                    @Override
                                    public void run() {
                                        //2. JSON String 变成 Request
                                        SRRPRequest request = new SRRPRequest();
                                        request.setAction("moveUp");
                                        request.setData(passwordText);

                                        //发送
                                        new SRRPClient().send(request);
                                    }
                                }.start();
                            }
                            break;
                        }
                    }
                    dataToView();
                }
            });

            btnDown.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (int j = 0; j < passwords.size(); j++) {
                        Password password = passwords.get(j);
                        if (password.getText().equals(passwordText)) {
                            int destinatedIndex = j+1;

                            if(destinatedIndex >= passwords.size()){
                                System.out.println("move out of range!");
                            }else{
                                //修改内存
                                Password psw = passwords.get(destinatedIndex);
                                passwords.set(destinatedIndex,password);
                                passwords.set(j,psw);

                                //修改数据库
                                new Thread(){
                                    @Override
                                    public void run() {
                                        //2. JSON String 变成 Request
                                        SRRPRequest request = new SRRPRequest();
                                        request.setAction("moveDown");
                                        request.setData(passwordText);

                                        //发送
                                        new SRRPClient().send(request);
                                    }
                                }.start();
                            }
                            break;
                        }
                    }
                    dataToView();
                }
            });

            btnStar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(password.isStar()){
                        //修改内存
                        password.setStar(false);

                        //修改数据库
                        new Thread(){
                            @Override
                            public void run() {
                                //2. JSON String 变成 Request
                                SRRPRequest request = new SRRPRequest();
                                request.setAction("unstar");
                                request.setData(passwordText);

                                //发送
                                new SRRPClient().send(request);
                            }
                        }.start();
                    }else{
                        //修改内存中的数据
                        password.setStar(true);

                        //修改数据库中的数据
                        new Thread(){
                            @Override
                            public void run() {
                                //2. JSON String 变成 Request
                                SRRPRequest request = new SRRPRequest();
                                request.setAction("star");
                                request.setData(passwordText);

                                //发送
                                new SRRPClient().send(request);
                            }
                        }.start();
                    }
                    dataToView();
                }
            });
        }
    }


    private String generatePSW(){
        String password = "";
        for (int i = 0; i < 6; i++) {
            password += new Random().nextInt(10); //随机产生0-9数字
        }
        return password;
    }

    public static void main(String[] args) {
        App.launch(args);
    }
}
