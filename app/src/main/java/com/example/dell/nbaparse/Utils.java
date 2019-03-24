package com.example.dell.nbaparse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    Class<R.drawable> drawableClass = R.drawable.class;

    public int getR(String triCode) {
        int r = 0;

        triCode = triCode.toLowerCase();
        try {
            Field field = drawableClass.getField("ic_" + triCode + "_logo");
            r = field.getInt(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return r;
    }

    public Bitmap toImage(String paramUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL("https:" + paramUrl);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream buffer = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public Calendar getKoreanTime(String dateBuffer){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        Calendar calendar = null;
        try {
            date = dateFormat.parse(dateBuffer);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 9);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
    
    public String getJsonString(String url){
        
        String jsonString = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
            jsonString = buffer.toString();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
