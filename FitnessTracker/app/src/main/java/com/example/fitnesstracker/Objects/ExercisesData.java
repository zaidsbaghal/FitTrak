package com.example.fitnesstracker.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExercisesData {
    @SerializedName("_id")
    private String _id;
    private String _date;
    private String _userid;
    private ExerciseBean _exercise;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_userid() {
        return _userid;
    }

    public void set_userid(String _userid) {
        this._userid = _userid;
    }

    public ExerciseBean get_exercise() {
        return _exercise;
    }

    public void set_exercise(ExerciseBean _exercise) {
        this._exercise = _exercise;
    }

    public static class ExerciseBean {
        private String _id;
        private String name;
        private String templateID;
        private String created;
        private List<SetsBean> sets;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTemplateID() {
            return templateID;
        }

        public void setTemplateID(String templateID) {
            this.templateID = templateID;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public List<SetsBean> getSets() {
            return sets;
        }

        public void setSets(List<SetsBean> sets) {
            this.sets = sets;
        }

        public static class SetsBean {

            private int reps;
            private int weights;

            public int getReps() {
                return reps;
            }

            public void setReps(int reps) {
                this.reps = reps;
            }

            public int getWeights() {
                return weights;
            }

            public void setWeights(int weights) {
                this.weights = weights;
            }
        }
    }
}

