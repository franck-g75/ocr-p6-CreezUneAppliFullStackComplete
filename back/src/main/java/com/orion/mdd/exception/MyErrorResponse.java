package com.orion.mdd.exception;

import java.util.Map;


public class MyErrorResponse {

    private String code;
    private String message;
    private Map<String, String> data;

    public MyErrorResponse(String errorCode, String errorMessage) {
        this.code = errorCode;
        this.message = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> validationErrors) {
        this.data = validationErrors;
    }
    
}