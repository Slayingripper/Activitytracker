package com.example.activitytracker;

public class attributes {

   private String time;
   private String dateTime;
   private String distance;



    private  String speed;
    public attributes(String dateTime, String time, String distance,String speed) {

        this.dateTime = dateTime;
        this.time = time;
        this.distance = distance;
        this.speed = speed;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
