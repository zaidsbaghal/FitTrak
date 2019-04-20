package com.example.fitnesstracker;

import com.example.fitnesstracker.Objects.ExercisesData.Exercise;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface APIHolder {

    @GET("exercises")
    Call<List<Exercise>> getExercises();


    @DELETE("exercise/{name}")
    Call<String> deleteExercise(@Path("name") String exercise);

}
