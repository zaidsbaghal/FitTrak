package com.example.fitnesstracker.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExercisesData {
    private List<Exercise> exercises; // List of all exercise objects

    public class Exercise {

        @SerializedName("exerciseID")
        private int exerciseID;
        @SerializedName("templateID")
        private String templateID;
        @SerializedName("name")
        private String name;
        @SerializedName("sets")
        private List<Set> sets;

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

        public String getTemplateID() {
            return templateID;
        }

        public String getName() {
            return name;
        }

        public List<Set> getSets() {
            return sets;
        }
    }

    public List<Exercise> getExercises(){
        return exercises;
    }
    public void setExercises(List<Exercise> exercises){
         this.exercises = exercises;
    }
}

