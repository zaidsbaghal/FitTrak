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
import com.example.fitnesstracker.exerciseAdapter;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class mainLogActivity extends AppCompatActivity {

    private TextView ExerciseTextView;
    private FirebaseAuth mAuth; // Firebase authentication
    private GoogleSignInClient mGoogleSignInClient; // Google sign in client
    private RecyclerView exerciseHolder; // Recvycler view that holds exercise cards
    private exerciseAdapter exerciseHolderAdapter; // Bridge between recycler view and data for each card
    private RecyclerView.LayoutManager exerciseHolderLayoutManager; // Aligning each card
    private List<ExercisesData> exercisesData;
    private Context mContext;
    private APIHolder apiHolder;
    private Retrofit retrofit;
    private List<ExercisesData.ExerciseBean> exercises;
    private int month;
    private int day;
    private int year;
    private String d;

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

        // Set Date
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

        // Retrofit and Gson
        retrofit = new Retrofit.Builder()
                .baseUrl("https://workoutapp-api-heroku.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiHolder = retrofit.create(APIHolder.class); // API
        Call<List<ExercisesData>> call = apiHolder.getExercises(d); // Call

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
                exercises= new ArrayList<ExercisesData.ExerciseBean>();

                for (ExercisesData e : exercisesData){
                    exercises.add(e.get_exercise());
                }
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                d = Integer.toString(month) + Integer.toString(day) + Integer.toString(year);

                // Exercise data is put in recycler view
                exerciseHolderAdapter = new exerciseAdapter(mContext, exercises); // Recycler view Adapter
                exerciseHolder.setAdapter(exerciseHolderAdapter);
            }

            @Override
            public void onFailure(Call<List<ExercisesData>> call, Throwable t) {
            }
        });


        // Floating action button; Starts template view when clicked
        FloatingActionButton addExerciseFAB = findViewById(R.id.addExerciseFAB);
        addExerciseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), templateView.class);
                startActivity(intent);
            }
        });
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
            final Calendar calendar = Calendar.getInstance();

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    R.style.datepicker,this,year,month,day);
            return datepickerdialog;

        }

        // Changes date to selected date in datePicker
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            TextView date = getActivity().findViewById(R.id.dateTextView); // Date text view to be set
            Calendar calendar = Calendar.getInstance(); // Gets current calendar instance
            calendar.setTimeInMillis(0); // Sets the time
            calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0); // Sets the calendar date to selected date
            Date SelectedDate = calendar.getTime(); // Gets the new current date
            String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(SelectedDate); // Text that matches new calendar date
            date.setText(date_n); // Changes the date
        }
    }
    // Delete exercise route
    public void deleteExercise(ExercisesData.ExerciseBean exercise) {
        Gson gson = new Gson();
        String nameJson = gson.toJson(exercise); // Name to be deleted in json

        Call<String> call = apiHolder.deleteExercise(nameJson);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(mContext, "Success?", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}