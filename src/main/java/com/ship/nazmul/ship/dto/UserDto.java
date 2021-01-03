package com.ship.nazmul.ship.dto;

public class UserDto {
    private String name;
    private String username;
    private String email;
    private String phoneNumber;

    public UserDto() {
    }

    public UserDto(String name, String username, String phoneNumber) {
        this.name = name;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
