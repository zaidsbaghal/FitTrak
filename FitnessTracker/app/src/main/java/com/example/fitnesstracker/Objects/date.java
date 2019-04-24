package com.example.fitnesstracker.Objects;

import com.google.gson.annotations.SerializedName;

public class date {
    @SerializedName("date")
    private String date;

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }
}
