package com.example.projectphoenix;

import java.util.ArrayList;

public class User {
    private String email;
    private String username;
    private String userType;
    private int age;
    private ArrayList<User> friends;
    private int gamerPoints;


    //Empty constructor required for Firebase
    public User() {

    }

    public User(String emailIn, String usernameIn, int ageIn) {
        this.email = emailIn;
        this.username = usernameIn;
        this.age = ageIn;
        this.friends = new ArrayList<User>();
        this.userType = "user";
        this.gamerPoints = 0;
    }

    public int getGamerPoints() {
        return gamerPoints;
    }

    public void setGamerPoints(int gamerPoints) {
        this.gamerPoints = gamerPoints;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
