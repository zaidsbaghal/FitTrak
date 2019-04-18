package com.example.fitnesstracker;

import com.example.fitnesstracker.Objects.ExercisesData.Exercise;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIHolder {

    @GET("exercises")
    Call<List<Exercise>> getExercises();
}
