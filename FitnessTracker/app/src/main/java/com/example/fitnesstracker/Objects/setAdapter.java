package com.example.fitnesstracker.Objects;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.fitnesstracker.Objects.ExercisesData.Exercise;
import com.example.fitnesstracker.R;

import java.util.ArrayList;
import java.util.List;

public class setAdapter extends RecyclerView.Adapter<setAdapter.setViewHolder>  {
    private List<Exercise.Set> sets;


    public static class setViewHolder extends RecyclerView.ViewHolder {
        public TextView setNum;
        public TextView setWeight;
        public TextView setReps;

        public setViewHolder(View itemView) {
            super(itemView);
            setNum = itemView.findViewById(R.id.setNum);
            setWeight = itemView.findViewById(R.id.setWeight);
            setReps = itemView.findViewById(R.id.setReps);
        }
    }
    public setAdapter(List<Exercise.Set> sets){
        this.sets = sets;
    }


    @Override
    public setViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_card, parent, false);
        setViewHolder vh = new setViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(setViewHolder holder, int position) {
        Exercise.Set current = sets.get(position); // Item at a specific index
        holder.setNum.setText(Integer.toString(position + 1));
        holder.setWeight.setText(Integer.toString(current.getWeight()) + " lbs.");
        holder.setReps.setText(Integer.toString(current.getReps()) + " reps");
    }

    @Override
    public int getItemCount() {
        if (sets == null){
            return 0;
        }
        return sets.size();
    }

}
