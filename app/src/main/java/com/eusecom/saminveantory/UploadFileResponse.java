package com.eusecom.saminveantory;

// This is used to map the JSON keys to the object by GSON
public class UploadFileResponse {

    String login;
    String name;

    @Override
    public String toString() {
        return(login);
    }
}