package com.example.fitnesstracker;

import android.content.Context;
import android.net.sip.SipSession;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.Activities.mainLogActivity;
import com.example.fitnesstracker.Objects.ExercisesData.Exercise;
import com.example.fitnesstracker.Objects.setAdapter;

import java.util.ArrayList;
import java.util.List;

public class exerciseAdapter extends RecyclerView.Adapter<exerciseAdapter.exerciseViewHolder> implements Filterable {
    private List<Exercise> exercises;
    private List<Exercise> exercisesFull;
    private Context mContext;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);

    public static class exerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView exerciseName;
        public RecyclerView setsRv;
        public ImageView deleteExercise;
        public CardView card;

        public exerciseViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName); // Name of exercise
            setsRv = itemView.findViewById(R.id.setsRv); // Recycler view that holds set data for each exercise
            deleteExercise = itemView.findViewById(R.id.exerciseDelete);
            card = itemView.findViewById(R.id.exerciseCard);

        }
    }
    public exerciseAdapter(Context context, List<Exercise> exercises){
        this.exercises = exercises;
        if(exercises != null) {
            exercisesFull = new ArrayList<>(exercises);
        }
        mContext = context;
    }

    @Override
    public exerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
        exerciseViewHolder vh = new exerciseViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final exerciseViewHolder holder,final int position) {
        Exercise current = exercises.get(position); // Item at a specific index
        holder.exerciseName.setText(current.getName());

        // Sets RecyclerView
        setAdapter setAdapter = new setAdapter(current.getSets());
        holder.setsRv.setAdapter(setAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.setsRv.getContext());
        holder.setsRv.setLayoutManager(layoutManager);

        // On Exercise Click
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                v.startAnimation(buttonClick);
                Toast.makeText(mContext, "Click " + pos, Toast.LENGTH_SHORT).show();

            }
        });

        // On exercise delete click
        holder.deleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                v.startAnimation(buttonClick);
                Toast.makeText(mContext, "Delete " + pos, Toast.LENGTH_SHORT).show();
                exercises.remove(pos);
                ((mainLogActivity)mContext).deleteExercise(exercises.get(pos));
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exercises == null){
            return 0;
        }

        return exercises.size();
    }

    @Override
    public Filter getFilter() {
        return templateFilter;
    }

    private Filter templateFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Exercise> filteredList = new ArrayList<>(); // List that contains the filtered items

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(exercisesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim(); //

                for (Exercise e: exercisesFull){
                    if (e.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(e); // Add filtered exercise to filtered list
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exercises.clear();
            exercises.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
