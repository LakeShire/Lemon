package com.github.lakeshire.lemonapp.model;

/**
 * Created by nali on 2016/4/13.
 */
public class Response {
    int error;
    String reason;
    String result;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
