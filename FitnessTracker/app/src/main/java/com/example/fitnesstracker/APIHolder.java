package com.example.fitnesstracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIHolder {

    @GET("/bins/jsqou")
    Call<Exercises> getExercises();

}
