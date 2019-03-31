package com.example.dell.nbaparse;

import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.dell.nbaparse.Adapter.ScheduleAdapter;
import com.example.dell.nbaparse.ViewItem.ScheduleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Field;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TeamSchedule extends AppCompatActivity {

    Utils utils = new Utils();

    private Toolbar toolbar;
    private InputStream inputStream;
    private String jsonString;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_schedule);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        new getData().execute();
        initLayout();

    }


    public class getData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private JSONObject season;

        private ArrayList<String> seasonHomeAway = new ArrayList<>();
        private ArrayList<String> seasonDate = new ArrayList<>();
        private ArrayList<String> seasonTime = new ArrayList<>();
        private ArrayList<String> opponentLogoParam = new ArrayList<>();
        private ArrayList<String> seasonCity = new ArrayList<>();
        private ArrayList<String> seasonTeamName = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(TeamSchedule.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            int count = 0;
            String url = "https://www.nba.com/.element/media/2.0/teamsites/warriors/json/schedule-2018.json?";
            try {
                inputStream = new URL(url).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String str;
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                jsonString = buffer.toString();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray array = new JSONObject(jsonString).getJSONArray("games");
                JSONObject opponent;

                seasonHomeAway.clear();
                seasonCity.clear();
                seasonDate.clear();
                seasonTime.clear();

                count = 0;
                for (int i = 0; i < array.length(); i++) {
                    season = array.getJSONObject(i);
                    opponent = season.getJSONObject("opponent");
                    if (season.optString("complete").equals("false")) {
                        seasonHomeAway.add(season.optString("home"));

                        String dateBuffer = season.optString("dateTimeUTC");
                        dateBuffer = dateBuffer.replace("T"," ");
                        Calendar cal = utils.getKoreanTime(dateBuffer);

                        String gameDate = cal.getTime().toString().substring(0, 10);
                        String gameTime = cal.getTime().toString().substring(11,16);
                        if(Integer.parseInt(gameTime.substring(0,2))<12){
                            gameTime = gameTime + " AM";
                        }
                        else{
                            int timeBuffer = Integer.parseInt(gameTime.substring(0,2));
                            if(timeBuffer == 12)
                                break;

                            timeBuffer = timeBuffer - 12;
                            gameTime = timeBuffer + " PM";
                        }

                        seasonDate.add(gameDate);
                        seasonTime.add(gameTime);
                        opponentLogoParam.add(opponent.optString("abbrev"));
                        seasonCity.add(opponent.optString("city"));
                        seasonTeamName.add(opponent.optString("name"));
                        count++;
                    }
                    if (count == 10 || array.getJSONObject(i+1).optString("seasonType").equals("pos"))
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int r;
            for (int i = 0; i < seasonHomeAway.size(); i++) {
                r = utils.getR(opponentLogoParam.get(i));
                scheduleItems.add(new ScheduleItem(seasonHomeAway.get(i), seasonDate.get(i), seasonTime.get(i), seasonCity.get(i), seasonTeamName.get(i), r));
            }
            ScheduleAdapter adapter = new ScheduleAdapter(scheduleItems);
            mRecyclerView.setAdapter(adapter);
            progressDialog.dismiss();

        }
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
