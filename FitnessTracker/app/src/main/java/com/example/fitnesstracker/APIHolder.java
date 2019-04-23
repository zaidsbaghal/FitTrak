package com.example.fitnesstracker;

import com.example.fitnesstracker.Objects.ExercisesData;
import com.example.fitnesstracker.Objects.ExercisesData.ExerciseBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface APIHolder {

    @GET("exercises")
    Call<List<ExercisesData.ExerciseBean>> getAllExercises();

    // Post exercises date
    @GET("users/5cb6474089762f000af18bea/logs/{date}")
    Call<List<ExercisesData>> getExercises(@Path("date") String date);

    // Post an exercise; Needs to be called with the below post method
    @POST("exercises")
    Call<Void> postExercise();

    // Post a posted exercise with the right date; Needs to be called with the above post method
    @POST("users/5cb6474089762f000af18bea/logs/{name}")
    Call<Void> postExerciseDate(@Path("name") String name, @Body String date) ;

    // Deletes Exercise by id
    @POST("users/5cb6474089762f000af18bea/logs/{date}/delete_exercise")
    Call<String> deleteExercise(@Path("date") String date, @Body String id);



}
