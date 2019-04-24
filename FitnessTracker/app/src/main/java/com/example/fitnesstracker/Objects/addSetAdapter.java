package com.example.fitnesstracker.Objects;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fitnesstracker.Objects.ExercisesData.ExerciseBean;
import com.example.fitnesstracker.R;

import java.util.List;

public class addSetAdapter extends RecyclerView.Adapter<addSetAdapter.setViewHolder>  {
    private List<ExerciseBean.SetsBean> sets;

    public static class setViewHolder extends RecyclerView.ViewHolder {
        public TextView setNum;
        public EditText setWeight;
        public EditText setReps;

        public setViewHolder(View itemView) {
            super(itemView);
            setNum = itemView.findViewById(R.id.addSetNum);
            setWeight = itemView.findViewById(R.id.addSetWeight);
            setReps = itemView.findViewById(R.id.addSetReps);
        }
    }
    public addSetAdapter(List<ExerciseBean.SetsBean> sets){
        this.sets = sets;
    }


    @Override
    public setViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_set_card, parent, false);
        setViewHolder vh = new setViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final setViewHolder holder, int position) {
        final ExerciseBean.SetsBean current = sets.get(position); // Item at a specific index
        holder.setNum.setText(Integer.toString(position + 1));

        holder.setWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    current.setWeights(Integer.parseInt(holder.setWeight.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.setReps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    current.setReps(Integer.parseInt(holder.setReps.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (sets == null){
            return 0;
        }
        return sets.size();
    }

}
