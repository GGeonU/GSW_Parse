package com.example.dell.nbaparse.ViewItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ScheduleItem {

    String homeAway;
    String date;
    String time;
    String area;
    String teamName;
    int r;

    public ScheduleItem(String homeAway, String date, String time, String area, String teamName, int param) {
        this.homeAway = homeAway;
        this.date = date;
        this.time = time;
        this.area = area;
        this.teamName = teamName;
        this.r = param;
    }

    public String getHomeAway() {
        return homeAway;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getArea() {
        return area;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getR() {
        return r;
    }
}
