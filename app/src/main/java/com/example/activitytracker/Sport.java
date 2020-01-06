package com.example.activitytracker;

public class Sport {
    private int _id;
    private String _datetime;
    private String _time;
    private String _distance;
    private String _speed;


    public Sport(String s, int speed, int quantity) {
    }

    public Sport(int id, String datetime, String distance, String speed,String time) {
        this._id = id;
        this._datetime = datetime;
        this._time = time;
        this._distance = distance;
        this._speed = speed;

    }

    public Sport(String datetime, String distance, String speed,String time) {
        this._datetime = datetime;
        this._time = time;
        this._distance = distance;
        this._speed = speed;

    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setdatetime(String datetime) {
        this._datetime = datetime;

    }

    public String getdatetime() {
        return this._datetime;
    }

    public void setdistance(String distance) {
        this._distance = distance;
    }

    public String getdistance() {
        return this._distance;
    }
    public  String getspeed(){
        return  this._speed;
    }
    public void setspeed (String speed) {
        this._speed = speed;
    }
    public void settime(String time) {
        this._time = time;

    }

    public String gettime() {
        return this._time;
    }



}
