package com.example.fitnesstracker.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.APIHolder;
import com.example.fitnesstracker.Objects.ExercisesData;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.Objects.exerciseAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class mainLogActivity extends AppCompatActivity {

    private TextView ExerciseTextView;
    private FirebaseAuth mAuth; // Firebase authentication
    private GoogleSignInClient mGoogleSignInClient; // Google sign in client
    private static RecyclerView exerciseHolder; // Recvycler view that holds exercise cards
    private exerciseAdapter exerciseHolderAdapter; // Bridge between recycler view and data for each card
    private RecyclerView.LayoutManager exerciseHolderLayoutManager; // Aligning each card
    private List<ExercisesData> exercisesData; // Exercise data objects; Holdes other relevant data besides the exercise objects
    private Context mContext; //  Current context
    private APIHolder apiHolder; // Api routes
    private Retrofit retrofit; // Retorift client
    private List<ExercisesData.ExerciseBean> exercises; // List of exercises for user
    private static String dateJson; // Stores date in specific format for routes
    private Call<List<ExercisesData>> call; // Call made to api holder
    private Calendar calendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    public static TextView logEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main_log);

        // Sets Context
        mContext = this;

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Log Empty Text View
        logEmpty = findViewById(R.id.logEmptyTv);

        // Set Date
        calendar = Calendar.getInstance();
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        TextView date  = (TextView) findViewById(R.id.dateTextView); // Get hold of textview.
        date.setText(date_n);         // Set it as current date.

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("79837694775-qo77r15dake58is1hefim29ocb33sgm8.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Logging interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Retrofit and Gson
        retrofit = new Retrofit.Builder()
                .baseUrl("https://workoutapp-api-heroku.herokuapp.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Sets date for data grab; (Is reset when using datepicker dialog)
        dateJson = new SimpleDateFormat("MMddyy", Locale.getDefault()).format(new Date()); // Date in JSON Format for
        apiHolder = retrofit.create(APIHolder.class); // API

        // Floating action button; Starts template view when clicked
        FloatingActionButton addExerciseFAB = findViewById(R.id.addExerciseFAB);
        addExerciseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), addExercise.class);
                startActivity(intent);
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
                        signOutUpdateUI(null);
                    }
                });
    }

    // UI updater
    private void signOutUpdateUI(FirebaseUser user) {
        if (user == null) { // Go back to login activity after sign out
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("NewApi")
    public void showCalendar() {
        DialogFragment dialogfragment = new datePicker();
        dialogfragment.show(getFragmentManager(), "DatePickerDialog");
    }

    // Date picker dialog class
    public static class datePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            int day = ((mainLogActivity) getActivity()).calendar.get(Calendar.DAY_OF_MONTH);
            int month = ((mainLogActivity) getActivity()).calendar.get(Calendar.MONTH);
            int year = ((mainLogActivity) getActivity()).calendar.get(Calendar.YEAR);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    R.style.datepicker,this,year,month,day);
            return datepickerdialog;

        }

        // Changes date to selected date in datePicker
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            TextView date = getActivity().findViewById(R.id.dateTextView); // Date text view to be set
            ((mainLogActivity) getActivity()).calendar = Calendar.getInstance(); // Gets current calendar instance
            ((mainLogActivity) getActivity()).calendar.setTimeInMillis(0); // Sets the time
            ((mainLogActivity) getActivity()).calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0); // Sets the calendar date to selected date
            ((mainLogActivity) getActivity()).mYear = year;
            ((mainLogActivity) getActivity()).mMonth = monthOfYear;
            ((mainLogActivity) getActivity()).mDay = dayOfMonth;
            Date SelectedDate = ((mainLogActivity) getActivity()).calendar.getTime(); // Gets the new current date
            String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(SelectedDate); // Text that matches new calendar date
            ((mainLogActivity) getActivity()).dateJson = new SimpleDateFormat("MMddyy", Locale.getDefault()).format(SelectedDate); // Text that matches new calendar date
            date.setText(date_n); // Changes the date
            ((mainLogActivity) getActivity()).getExerciseData(((mainLogActivity) getActivity()).dateJson); // Gets data for that date
        }
    }

    // Recycler View
    public void buildRecyclerView() {
        exercises = null;
        exerciseHolder = findViewById(R.id.recyclerView);
        exerciseHolderLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) exerciseHolderLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        exerciseHolder.setLayoutManager(exerciseHolderLayoutManager);
        exerciseHolderAdapter = new exerciseAdapter(this, exercises);
        exerciseHolder.setAdapter(exerciseHolderAdapter);
    }

    // Gets relevant exercise data
    public void getExerciseData(String dateJson){
        call = apiHolder.getExercises(dateJson); // Call with current date
        buildRecyclerView(); // Recycler view set up
        // Gets Data
        call.enqueue(new Callback<List<ExercisesData>>() {
            @Override
            public void onResponse(Call<List<ExercisesData>> call, Response <List<ExercisesData>> response) {
                if (!response.isSuccessful()){
                    return;
                }

                // Gets exercise data
                exercisesData = response.body(); // List of exercises
                exercises = new ArrayList<ExercisesData.ExerciseBean>();

                // Gets exercise objects from each exercise data object
                for (ExercisesData e : exercisesData){
                    exercises.add(e.get_exercise());
                }

                // Log empty visibility
                if (exercises == null || exercises.isEmpty()){
                    exerciseHolder.setVisibility(View.GONE);
                    logEmpty.setVisibility(View.VISIBLE);
                } else {
                    logEmpty.setVisibility(View.GONE);
                    exerciseHolder.setVisibility(View.VISIBLE);
                }

                // Exercise data is put in recycler view
                exerciseHolderAdapter = new exerciseAdapter(mContext, exercises); // Recycler view Adapter
                exerciseHolder.setAdapter(exerciseHolderAdapter);
            }

            @Override
            public void onFailure(Call<List<ExercisesData>> call, Throwable t) {
                // Do something on failure
            }
        });

    }

    // Delete exercise route
    public void deleteExercise(ExercisesData.ExerciseBean exercise) {
        Gson gson = new Gson();
        String idJson =""; // Id of ExercisesData Object
        final String name = exercise.getName(); // Name of exercise
        ExercisesData ed = new ExercisesData();
        // For each exercises data get the id if the its exercise objects name matches exercise name
        for (ExercisesData d: exercisesData){
            if (d.get_exercise().getName().equals(name)){
                ed.set_id(d.get_id());
                break;
            }
        }

        Call<Void> call = apiHolder.deleteExercise(dateJson, ed);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(mContext,  "Deleted " + name, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Failed to delete " + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getExerciseData(dateJson);
        exerciseHolderAdapter.notifyDataSetChanged();

    }

    public static String getDate(){
        return dateJson;
    }
    public static void setVisible(){
        logEmpty.setVisibility(View.VISIBLE);
        exerciseHolder.setVisibility(View.GONE);
    }

}