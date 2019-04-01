package com.agoda.model;

/**
 * Credentials class , stores username and password
 */
public class Credential
{
    private String userName;
    private String password;

    public Credential(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
