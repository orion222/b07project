package com.example.b07demosummer2024.models;

public class Credentials {

    private int id;
    private String username;
    private String password;

    public Credentials() {
    }

    public Credentials(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return getId() + " " + getUsername() + " " + getPassword();
    }
}
