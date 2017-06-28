package com.example.textSearchEngine.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by suryansh on 22/6/17.
 */
public class ValidationError {

    private int code;

    private String message;

    public ValidationError() {
    }

    public ValidationError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValidationError{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
