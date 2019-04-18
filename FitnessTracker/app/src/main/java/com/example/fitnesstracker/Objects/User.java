package com.example.fitnesstracker.Objects;

public class User {
    private String userId;
    private String name;
    private String username;
    private ExercisesData data;

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }

    public ExercisesData getData(){
        return data;
    }
}
