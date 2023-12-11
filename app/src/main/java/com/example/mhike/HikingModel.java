package com.example.mhike;

public class HikingModel {
    private int id;
    private String hikeName, location, date;
    private int length;
    private String parking, difficulty, description, leaderName;

    public HikingModel(int id, String hikeName, String location, String date, int length, String parking, String difficulty, String description, String leaderName) {
        this.id = id;
        this.hikeName = hikeName;
        this.location = location;
        this.date = date;
        this.length = length;
        this.parking = parking;
        this.difficulty = difficulty;
        this.description = description;
        this.leaderName = leaderName;
    }

    public HikingModel(String hikeName, String location, String date, int length, String parking, String difficulty, String description, String leaderName) {
        this.hikeName = hikeName;
        this.location = location;
        this.date = date;
        this.length = length;
        this.parking = parking;
        this.difficulty = difficulty;
        this.description = description;
        this.leaderName = leaderName;
    }

    public HikingModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getHikeName() {
        return hikeName;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getLength() {
        return length;
    }

    public String getParking() {
        return parking;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
