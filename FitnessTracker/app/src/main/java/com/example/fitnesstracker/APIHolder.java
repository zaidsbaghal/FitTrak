package com.example.fitnesstracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIHolder {

    @GET("bins/jsqou")
    Call<List<Exercise>> getExercises();

}
