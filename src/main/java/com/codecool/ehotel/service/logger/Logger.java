package com.codecool.ehotel.service.logger;

public interface Logger {

    static Logger instance = null;
    void infoLogger(String message);
    void errorLogger(String message);
     void handleError(Exception e);
}
