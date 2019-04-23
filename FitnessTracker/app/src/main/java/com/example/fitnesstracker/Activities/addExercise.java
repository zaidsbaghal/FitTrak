package com.example.fitnesstracker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.fitnesstracker.Objects.ExercisesData;
import com.example.fitnesstracker.R;

public class addExercise extends AppCompatActivity {
    private ExercisesData.ExerciseBean newEx = new ExercisesData.ExerciseBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Exercise");

    }
}
