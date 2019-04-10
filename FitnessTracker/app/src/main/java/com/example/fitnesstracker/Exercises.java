package com.example.fitnesstracker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Exercises {
    private List<Exercise> exercises; // List of all exercise objects

    public class Exercise {

        @SerializedName("exerciseID")
        private int exerciseID;
        @SerializedName("templateID")
        private int templateID;
        @SerializedName("name")
        private String name;
        @SerializedName("sets")
        private Set set;

        public class Set {
            @SerializedName("reps")
            private int reps;
            @SerializedName("weights")
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

        public Set getSet() {
            return set;
        }
    }

    public List<Exercise> getExercises(){
        return exercises;
    }
}

