package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.Task;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;


import static com.example.fitnesstracker.R.id.logOut;

public class mainLogActivity extends AppCompatActivity {

    private TextView ExerciseTextView;
    private FirebaseAuth mAuth; // Firebase authentication
    private GoogleSignInClient mGoogleSignInClient; // Google sign in client
    private DatePickerDialog calendar; // Popup calendar selector
    private TextView date;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main_log);


        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("79837694775-qo77r15dake58is1hefim29ocb33sgm8.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Retrofit
        ExerciseTextView = findViewById(R.id.text_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.myjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIHolder apiHolder = retrofit.create(APIHolder.class);

        Call<List<Exercise>> call = apiHolder.getExercises();

        call.enqueue(new Callback<List<com.example.fitnesstracker.Exercise>>() {
            @Override
            public void onResponse(Call<List<com.example.fitnesstracker.Exercise>> call, Response<List<com.example.fitnesstracker.Exercise>> response) {
                if (!response.isSuccessful()){
                    ExerciseTextView.setText("Code " + response.code());
                    return;
                }

                List<Exercise> exercises = Arrays.asList(); // List of exercises

                for (Exercise exercise : exercises){ // For each exercise in the exercises list
                    String content = "";
                    content += "Name " + exercise.getName() + "\n";

                    ExerciseTextView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<com.example.fitnesstracker.Exercise>> call, Throwable t) {
                ExerciseTextView.setText(t.getMessage()); // Error Message
            }
        });
    }


    // Options menu for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_log_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Options menu click actions
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logOut:
                signOut();
                return true;
            case R.id.settings:
                // Action goes here
                return true;
            case R.id.calendarBtn:
                showCalendar();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Google SignOut
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    // UI updater
    private void updateUI(FirebaseUser user) {
        if (user == null) { // Go back to login activity after sign out
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("NewApi")
    public void showCalendar() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        date = findViewById(R.id.logEmptyTextView);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}