package com.touchsoft.server;

public class ChatUser {
    private String name;
    private String role;
    private boolean isAvailable;
    private SocketProcessor userTo;

    public ChatUser() {

    }

    public ChatUser(String name, String role, boolean isAvailable, SocketProcessor userTo) {
        this.name = name;
        this.role = role;
        this.isAvailable = isAvailable;
        this.userTo = userTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public SocketProcessor getUserTo() {
        return userTo;
    }

    public void setUserTo(SocketProcessor userTo) {
        this.userTo = userTo;
    }
}
