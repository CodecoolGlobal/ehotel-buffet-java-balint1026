package com.codecool.ehotel.service.logger;

public class ConsoleLogger {
  
public class ConsoleLogger implements Logger{
    @Override
    public void infoLogger(String message) {
        System.out.println(message);
    }

    @Override
    public void errorLogger(String message) {
        System.out.println(message);
    }
}
