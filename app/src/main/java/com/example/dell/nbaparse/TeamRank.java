package com.example.dell.nbaparse;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.dell.nbaparse.Adapter.FragmentViewAdapter;
import com.example.dell.nbaparse.Adapter.TeamRankAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeamRank extends AppCompatActivity {

    Utils utils = new Utils();
    FragmentViewAdapter adapter = new FragmentViewAdapter(getSupportFragmentManager());

    private Toolbar toolbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_rank);

        setToolbar();
        new getData().execute();
    }

    public class getData extends AsyncTask<Void, Void, Void> {
        String teamIdUrlString = null;
        String leagueRosterString = null;

        String east, west, teams;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TeamRank.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String getTeamIdUrl = "https://data.nba.net/prod//v1/current/standings_conference.json";
            String leagueRosterUrl = "https://data.nba.net/prod/v2/2018/teams.json";
            teamIdUrlString = utils.getJsonString(getTeamIdUrl);
            leagueRosterString = utils.getJsonString(leagueRosterUrl);

            try {
                JSONObject conference = new JSONObject(teamIdUrlString).getJSONObject("league").getJSONObject("standard").getJSONObject("conference");
                east = conference.getJSONArray("east").toString();
                west = conference.getJSONArray("west").toString();

                teams = new JSONObject(leagueRosterString).getJSONObject("league").getJSONArray("standard").toString();


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);

            adapter.addFragment(ConferenceRank.newInstance(east, teams), "east");
            adapter.addFragment(ConferenceRank.newInstance(west, teams), "west");

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(1);

            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Standings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }  // set custom toolbar
}
