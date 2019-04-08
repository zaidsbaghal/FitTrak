package com.example.fitnesstracker;

import java.util.List;

public class Exercise {
    private int exerciseID;
    private int templateID;
    private String name;
    private List<Set> sets;

    public class Set{
        private int reps;
        private int weight;

        public int getReps() {
            return reps;
        }

        public int getWeight() {
            return weight;
        }
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public int getTemplateID() {
        return templateID;
    }

    public String getName() {
        return name;
    }

    public List<Set> getSets(){
        return sets;
    }
}
