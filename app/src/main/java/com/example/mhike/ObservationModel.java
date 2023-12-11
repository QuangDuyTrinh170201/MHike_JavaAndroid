package com.example.mhike;


public class ObservationModel {

    private int id;
    private String observation, time, comment;
    private int hikeId;

    public ObservationModel(int id, String observation, String time, String comment, int hikeId) {
        this.id = id;
        this.observation = observation;
        this.time = time;
        this.comment = comment;
        this.hikeId = hikeId;
    }

    public ObservationModel(String observation, String time, String comment, int hikeId) {
        this.observation = observation;
        this.time = time;
        this.comment = comment;
        this.hikeId = hikeId;
    }

    public ObservationModel(String observation, String time, String comment) {
        this.observation = observation;
        this.time = time;
        this.comment = comment;
    }

    public ObservationModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getObservation() {
        return observation;
    }

    public String getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    @Override
    public String toString() {
        return "Observation ID: " + id +
                "\nObservation: " + observation +
                "\nTime of Observation: " + time +
                "\nAdditional Comment: " + comment +
                "\nHike ID: " + hikeId;
    }
}
