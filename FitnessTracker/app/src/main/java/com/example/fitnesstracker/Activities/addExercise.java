package com.example.fitnesstracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnesstracker.APIHolder;
import com.example.fitnesstracker.Objects.ExercisesData;
import com.example.fitnesstracker.Objects.addSetAdapter;
import com.example.fitnesstracker.Objects.date;
import com.example.fitnesstracker.Objects.setAdapter;
import com.example.fitnesstracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addExercise extends AppCompatActivity {
    private ExercisesData newExd = new ExercisesData(); // New exercise data object
    private ExercisesData.ExerciseBean newEx = new ExercisesData.ExerciseBean(); // New Exercise to be added
    private List<ExercisesData.ExerciseBean.SetsBean> sets; // List of sets
    private Button addSetBtn;
    private EditText nameEt;
    private APIHolder apiHolder;
    private Context mContext; //  Current context
    private String dateJson;
    private date d;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Context
        mContext = this;

        // Recycler view
        RecyclerView addSets = findViewById(R.id.addSetsRv);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Exercise");

        // Add initial set
        sets = new ArrayList<ExercisesData.ExerciseBean.SetsBean>();
        ExercisesData.ExerciseBean.SetsBean newSet = new ExercisesData.ExerciseBean.SetsBean();
        newSet.setReps(0);
        newSet.setWeights(0);
        sets.add(newSet);

        // Sets RecyclerView and Adapter
        final addSetAdapter setAdapter = new addSetAdapter(sets);
        addSets.setAdapter(setAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addSets.getContext());
        addSets.setLayoutManager(layoutManager);

        // Buttons and Views
        nameEt = findViewById(R.id.addExerciseNameEt);
        MenuItem doneBtn = findViewById(R.id.addExerciseDoneBtn);

        // Logging interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Retrofit and Gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://workoutapp-api-heroku.herokuapp.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Sets date for data grab; (Is reset when using datepicker dialog)
        dateJson = mainLogActivity.getDate();
        apiHolder = retrofit.create(APIHolder.class); // API


        // Floating action button; Adds set when clicked
        FloatingActionButton addExerciseFAB = findViewById(R.id.addSetFAB);
        addExerciseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExercisesData.ExerciseBean.SetsBean newSet = new ExercisesData.ExerciseBean.SetsBean();
                newSet.setReps(0);
                newSet.setWeights(0);
                sets.add(newSet); // Add new set to list
                setAdapter.notifyDataSetChanged(); // Notify adapter of a new data set change
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_exercise_menu, menu);
        MenuItem doneBtn = menu.findItem(R.id.addExerciseDoneBtn);
        return true;
    }

    // Options menu click actions
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addExerciseDoneBtn:
                createExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createExercise() {
        name = nameEt.getText().toString();
        newEx.setName(name); // Sets the name
        newEx.setSets(sets);
        newExd.set_exercise(newEx);

        Call<Void> call = apiHolder.postExercise(newEx); // Call with current date
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(mContext,  "Added " + newEx.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext,  "Failed " + newEx.getName() , Toast.LENGTH_SHORT).show();
            }
        });

        if (name.contains(" ")){
            name = name.trim();
        }

        d = new date();
        d.setDate(dateJson);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<Void> callDate = apiHolder.postExerciseDate(name,d); // Call with current date

                callDate.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        }, 1000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
