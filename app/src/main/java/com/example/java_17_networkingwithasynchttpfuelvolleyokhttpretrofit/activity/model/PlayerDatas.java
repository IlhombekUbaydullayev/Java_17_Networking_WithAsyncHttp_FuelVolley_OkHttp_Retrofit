package com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.model;

import java.util.List;

public class PlayerDatas {

    private String status;
    private String message;
    private List<Player> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Player> getData() {
        return data;
    }

    public void setData(List<Player> data) {
        this.data = data;
    }
}
