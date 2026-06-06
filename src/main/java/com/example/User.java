package com.example;

public class User{
    static int totalUsers = 0;
    String userName;

    public User(String name){
        userName = name;
        totalUsers++;
    }
}
