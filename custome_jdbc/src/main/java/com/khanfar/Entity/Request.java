package com.khanfar.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {


    private String username ;

    private String password  ;


    public Request(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Request() {
        // default constructor
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}