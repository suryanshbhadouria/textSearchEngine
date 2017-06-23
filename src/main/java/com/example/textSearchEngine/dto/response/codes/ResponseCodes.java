package com.example.textSearchEngine.dto.response.codes;

/**
 * Created by suryansh on 23/6/17.
 */
public enum ResponseCodes {
    BAD_REQUEST(200, "Bad Request"), SUCCESSFUL(200, "Successful");
    int code;
    String message;

    ResponseCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
