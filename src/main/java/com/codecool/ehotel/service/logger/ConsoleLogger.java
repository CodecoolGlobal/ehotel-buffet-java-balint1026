package com.codecool.ehotel.service.logger;

public class ConsoleLogger implements Logger{

    @Override
    public void infoLogger(String info) {
        System.out.println(info);
    }

    @Override
    public void errorLogger(String error) {
        System.out.println(error);
    }
}
