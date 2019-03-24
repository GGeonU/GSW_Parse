package com.example.dell.nbaparse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Utils utils = new Utils();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;

    private TextView win;
    private TextView loss;
    private TextView winPctV2;
    private TextView confWinLoss;
    private TextView divWinLoss;
    private TextView homeWinLoss;
    private TextView roadWinLoss;
    private TextView last10;

    private TextView lastGameTime;
    private TextView lastGameComplete;
    private ImageView lastVTeamLogo;
    private TextView lastVTeamWinLoss;
    private TextView lastVTeamScore;
    private ImageView lastHTeamLogo;
    private TextView lastHTeamWinLoss;
    private TextView lastHTeamScore;


    private TextView nextGameTime;
    private TextView nextGameComplete;
    private ImageView nextVTeamLogo;
    private TextView nextVTeamWinLoss;
    private TextView nextVTeamTriCode;
    private ImageView nextHTeamLogo;
    private TextView nextHTeamWinLoss;
    private TextView nextHTeamTriCode;

    int lastVTeamR, lastHTeamR, nextVTeamR, nextHTeamR;

    private String mWin;
    private String mLoss;
    private String mWinPctV2;
    private String mConfWinLoss;
    private String mDivWinLoss;
    private String mHomeWinLoss;
    private String mRoadWinLoss;
    private String mLast10;


    private String lastGid, lastDate;
    private String nextGid, nextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        setToolbar();
        setTeamStatLayout();
        setLastGameBoxLayout();
        setNextGameBoxLayout();

        new getTeamStat().execute();
        new getScheduleParser().execute();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.Home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.player_list:
                intent = new Intent(this, PlayerList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                break;

            case R.id.team_schedule:
                intent = new Intent(this, TeamSchedule.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                break;

            case R.id.team_leader:
                intent = new Intent(this, TeamLeader.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                break;

            case R.id.team_rank:
                intent = new Intent(this, TeamRank.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
        }

        return false;
    }

    public class getTeamStat extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private String jsonString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Menu.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            win.setText(mWin);
            loss.setText(mLoss);
            winPctV2.setText(mWinPctV2);
            confWinLoss.setText(mConfWinLoss);
            divWinLoss.setText(mDivWinLoss);
            homeWinLoss.setText(mHomeWinLoss);
            roadWinLoss.setText(mRoadWinLoss);
            last10.setText(mLast10);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://data.nba.net/prod/v1/current/standings_all.json";
            jsonString = utils.getJsonString(url);

            try {
                JSONArray array = new JSONObject(jsonString).getJSONObject("league").getJSONObject("standard").getJSONArray("teams"); //1610612744
                int count = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject teams = array.getJSONObject(i);
                    String teamId = teams.optString("teamId");
                    if (teamId.equals("1610612744")) {  // warriors teamId
                        count = i;
                        break;
                    }
                }

                JSONObject warriors = array.getJSONObject(count);
                mWin = warriors.optString("win") + " W";
                mLoss = warriors.optString("loss") + " L";
                mWinPctV2 = warriors.optString("winPctV2") + " %";
                mConfWinLoss = warriors.optString("confWin") + " - " + warriors.optString("confLoss");
                mDivWinLoss = warriors.optString("divWin") + " - " + warriors.optString("divLoss");
                mHomeWinLoss = warriors.optString("homeWin") + " - " + warriors.optString("homeLoss");
                mRoadWinLoss = warriors.optString("awayWin") + " - " + warriors.optString("awayLoss");
                mLast10 = warriors.optString("lastTenWin") + " - " + warriors.optString("lastTenLoss");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class getScheduleParser extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        private String jsonString;
        private String lastJsonString;
        private String nextJsonString;

        private String mLastVTeamTriCode;
        private String mLastHTeamTriCode;
        private String mLastVTeamScore;
        private String mLastHTeamScore;
        private String mLastVTeamWinLoss;
        private String mLastHTeamWinLoss;

        private String mNextGameStartTime;
        private String mNextVTeamTricode;
        private String mNextHTeamTricode;
        private String mNextVTeamWinLoss;
        private String mNextHTeamWinLoss;

        private String gameDate;
        private String gameTime;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Menu.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            lastGameComplete.setText("Last Game");
            lastGameTime.setText("Final");

            lastVTeamR = utils.getR(mLastVTeamTriCode);
            lastHTeamR = utils.getR(mLastHTeamTriCode);

            lastVTeamLogo.setImageResource(lastVTeamR);
            lastHTeamLogo.setImageResource(lastHTeamR);

            lastVTeamWinLoss.setText(mLastVTeamWinLoss);
            lastHTeamWinLoss.setText(mLastHTeamWinLoss);

            lastVTeamScore.setText(mLastVTeamScore);
            lastHTeamScore.setText(mLastHTeamScore);


            nextGameComplete.setText("Next Game");
            nextGameTime.setText(gameDate + "\n" + gameTime);
            nextVTeamR = utils.getR(mNextVTeamTricode);
            nextHTeamR = utils.getR(mNextHTeamTricode);

            nextVTeamLogo.setImageResource(nextVTeamR);
            nextHTeamLogo.setImageResource(nextHTeamR);

            nextVTeamTriCode.setText(mNextVTeamTricode);
            nextHTeamTriCode.setText(mNextHTeamTricode);

            nextVTeamWinLoss.setText(mNextVTeamWinLoss);
            nextHTeamWinLoss.setText(mNextHTeamWinLoss);

            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://www.nba.com/.element/media/2.0/teamsites/warriors/json/schedule-2018.json?";
            jsonString = utils.getJsonString(url);
            try {
                JSONArray array = new JSONObject(jsonString).getJSONArray("games");
                int count = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    if (object.optString("complete").equals("false")) {
                        count = i;
                        break;
                    }
                }

                JSONObject lastGame = array.getJSONObject(count - 1);
                JSONObject nextGame = array.getJSONObject(count);
                lastGid = lastGame.optString("gid");
                lastDate = lastGame.optString("date").replaceAll("-", "");

                nextGid = nextGame.optString("gid");
                nextDate = nextGame.optString("date").replaceAll("-", "");

                Log.d("lastGid", lastGid + " " + nextGid);


                String lastBoxScoreUrl = "https://data.nba.net/prod/v1/" + lastDate + "/" + lastGid + "_mini_boxscore.json";
                String nextBoxScoreUrl = "https://data.nba.net/prod/v1/" + nextDate + "/" + nextGid + "_mini_boxscore.json";


                lastJsonString = utils.getJsonString(lastBoxScoreUrl);
                nextJsonString = utils.getJsonString(nextBoxScoreUrl);

                JSONObject lastGameData = new JSONObject(lastJsonString).getJSONObject("basicGameData");
                JSONObject lastVTeamObject = lastGameData.getJSONObject("vTeam");
                JSONObject lastHTeamObject = lastGameData.getJSONObject("hTeam");

                JSONObject nextGameData = new JSONObject(nextJsonString).getJSONObject("basicGameData");
                JSONObject nextVTeamObject = nextGameData.getJSONObject("vTeam");
                JSONObject nextHTeamObject = nextGameData.getJSONObject("hTeam");


                mLastVTeamTriCode = lastVTeamObject.optString("triCode");
                mLastVTeamWinLoss = lastVTeamObject.optString("win") + " - " + lastVTeamObject.optString("loss");
                mLastVTeamScore = lastVTeamObject.optString("score");

                mLastHTeamTriCode = lastHTeamObject.optString("triCode");
                mLastHTeamWinLoss = lastHTeamObject.optString("win") + " - " + lastHTeamObject.optString("loss");
                mLastHTeamScore = lastHTeamObject.optString("score");


                mNextGameStartTime = nextGameData.optString("startTimeUTC").replaceAll("T", " ").substring(0, 19);
                Calendar startTime = utils.getKoreanTime(mNextGameStartTime);
                gameDate = startTime.getTime().toString().substring(0, 10);
                gameTime = startTime.getTime().toString().substring(11, 16);
                if (Integer.parseInt(gameTime.substring(0, 2)) < 12) {
                    gameTime = gameTime + " AM";
                } else {
                    gameTime = gameTime + " PM";
                }
                mNextVTeamTricode = nextVTeamObject.optString("triCode");
                mNextVTeamWinLoss = nextVTeamObject.optString("win") + " - " + nextVTeamObject.optString("loss");

                mNextHTeamTricode = nextHTeamObject.optString("triCode");
                mNextHTeamWinLoss = nextHTeamObject.optString("win") + " - " + nextHTeamObject.optString("loss");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder finishCheck = new AlertDialog.Builder(this);
        finishCheck.setMessage("종료하시겠습니까?")
                .setTitle("Warriors")
                .setIcon(R.drawable.ic_gsw_logo);

        finishCheck.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        finishCheck.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        finishCheck.create();
        finishCheck.show();
    }


    //    --------------------------- set Layout ------------------------------

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_button);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setTeamStatLayout(){
        win = (TextView) findViewById(R.id.win);
        loss = (TextView) findViewById(R.id.loss);
        winPctV2 = (TextView) findViewById(R.id.winPctV2);
        confWinLoss = (TextView) findViewById(R.id.conf_win_loss);
        divWinLoss = (TextView) findViewById(R.id.div_win_loss);
        homeWinLoss = (TextView) findViewById(R.id.home_win_loss);
        roadWinLoss = (TextView) findViewById(R.id.road_win_loss);
        last10 = (TextView) findViewById(R.id.last10);
    }

    private void setLastGameBoxLayout(){
        View lastGameBoxView = findViewById(R.id.last_game_layout);
        lastGameComplete = (TextView) lastGameBoxView.findViewById(R.id.game_complete);
        lastGameTime = (TextView) lastGameBoxView.findViewById(R.id.game_time);
        lastVTeamLogo = (ImageView) lastGameBoxView.findViewById(R.id.vteam_logo);
        lastVTeamScore = (TextView) lastGameBoxView.findViewById(R.id.vteam_score);
        lastVTeamWinLoss = (TextView) lastGameBoxView.findViewById(R.id.vteam_win_loss);

        lastHTeamLogo = (ImageView) lastGameBoxView.findViewById(R.id.hteam_logo);
        lastHTeamScore = (TextView) lastGameBoxView.findViewById(R.id.hteam_score);
        lastHTeamWinLoss = (TextView) lastGameBoxView.findViewById(R.id.hteam_win_loss);
    }

    private void setNextGameBoxLayout(){
        View nextGameBoxView = findViewById(R.id.next_game_layout);
        nextGameComplete = (TextView) nextGameBoxView.findViewById(R.id.game_complete);
        nextGameTime = (TextView) nextGameBoxView.findViewById(R.id.game_time);
        nextVTeamLogo = (ImageView) nextGameBoxView.findViewById(R.id.vteam_logo);
        nextVTeamTriCode = (TextView) nextGameBoxView.findViewById(R.id.vteam_score);
        nextVTeamWinLoss = (TextView) nextGameBoxView.findViewById(R.id.vteam_win_loss);

        nextHTeamLogo = (ImageView) nextGameBoxView.findViewById(R.id.hteam_logo);
        nextHTeamTriCode = (TextView) nextGameBoxView.findViewById(R.id.hteam_score);
        nextHTeamWinLoss = (TextView) nextGameBoxView.findViewById(R.id.hteam_win_loss);
    }

}
