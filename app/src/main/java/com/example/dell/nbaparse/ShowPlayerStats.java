package com.example.dell.nbaparse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;

public class ShowPlayerStats extends AppCompatActivity {

    Utils utils = new Utils();

    String[] nameArray;
    String jsonString;

    private TextView mpgSeason;
    private TextView fgpSeason;
    private TextView tppSeason;
    private TextView ftpSeason;
    private TextView ppgSeason;
    private TextView rpgSeason;
    private TextView apgSeason;  // season stats

    private TextView mpgCareer;
    private TextView fgpCareer;
    private TextView tppCareer;
    private TextView ftpCareer;
    private TextView ppgCareer;
    private TextView rpgCareer;
    private TextView apgCareer; // career stats

    private TextView playerFirstName;
    private TextView playerLastName;
    private TextView playerNumber;
    private TextView playerPosition;
    private ImageView playerImage;
    private Bitmap bitmap;

    private TextView height;
    private TextView weight;
    private TextView born;
    private TextView age;
    private TextView from;
    private TextView debut;
    private TextView years;
    private TextView previously;

    private String name;
    private String num;
    private String position;

    private String firstName;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_player_stats);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String[] arr = name.split(" ");
        firstName = arr[0];
        lastName = arr[1];
        num = intent.getStringExtra("number");
        position = intent.getStringExtra("position");
        bitmap = intent.getParcelableExtra("image");
        nameArray = name.split(" ");

        setPlayerInfoLayout();
        setSeasonLayout();
        setCareerLayout();
        new getPlayerStats().execute();

    }

    private class getPlayerStats extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private String smpg, sfgp, stpp, sftp, sppg, srpg, sapg;
        private String cmpg, cfgp, ctpp, cftp, cppg, crpg, capg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ShowPlayerStats.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            playerFirstName.setText(firstName);
            playerLastName.setText(lastName);
            playerNumber.setText("#" + num);
            playerPosition.setText(position);
            playerImage.setImageBitmap(bitmap);
            try {
                Document doc = Jsoup.connect("http://www.nba.com/players/" + nameArray[0] + "/" + nameArray[1]).get();
                Element getPlayerId = doc.getElementsByTag("meta").get(9);
                String playerId = getPlayerId.attr("content");
                if (name.equals("Andre Iguodala")) {
                    playerId = "2738";
                }

                doc = Jsoup.connect("http://www.nba.com/players/" + nameArray[0] + "/" + nameArray[1] + "/" + playerId).get();

                Elements playerInfo = doc.select("section.nba-player-vitals");
                Elements metric = playerInfo.select("p.nba-player-vitals__top-info-metric");
                Elements imperial = playerInfo.select("p.nba-player-vitals__top-info-imperial");
                Elements info = playerInfo.select("span.nba-player-vitals__bottom-info");
                Elements previouslyElements = info.get(5).select("p");

                StringBuffer previouslyBuffer = new StringBuffer();
                for (int i = 0; i < previouslyElements.size(); i++) {
                    if (i == 3) {
                        break;
                    }

                    if (i == previouslyElements.size() - 1 || i == 2) {
                        previouslyBuffer.append(previouslyElements.get(i).text());
                    } else {
                        previouslyBuffer.append(previouslyElements.get(i).text() + "\n");
                    }
                }

                height.setText(imperial.get(0).text() + "\n" + metric.get(0).text());
                weight.setText(imperial.get(1).text() + "\n" + metric.get(1).text());
                born.setText(info.get(0).text());
                age.setText(info.get(1).text());
                from.setText(info.get(2).text());
                debut.setText(info.get(3).text());
                years.setText(info.get(4).text());
                previously.setText(previouslyBuffer.toString());

                String url = "https://data.nba.net/prod/v1/2018/players/" + playerId + "_profile.json";

                jsonString = utils.getJsonString(url);

                try {
                    JSONObject object = new JSONObject(jsonString).getJSONObject("league").getJSONObject("standard").getJSONObject("stats");
                    JSONObject latest = object.getJSONObject("latest");
                    JSONObject careerSummary = object.getJSONObject("careerSummary");

                    smpg = latest.optString("mpg");
                    sfgp = latest.optString("fgp");
                    stpp = latest.optString("tpp");
                    sftp = latest.optString("ftp");
                    sppg = latest.optString("ppg");
                    srpg = latest.optString("rpg");
                    sapg = latest.optString("apg");

                    cmpg = careerSummary.optString("mpg");
                    cfgp = careerSummary.optString("fgp");
                    ctpp = careerSummary.optString("tpp");
                    cftp = careerSummary.optString("ftp");
                    cppg = careerSummary.optString("ppg");
                    crpg = careerSummary.optString("rpg");
                    capg = careerSummary.optString("apg");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mpgSeason.setText(smpg);
            fgpSeason.setText(sfgp);
            tppSeason.setText(stpp);
            ftpSeason.setText(sftp);
            ppgSeason.setText(sppg);
            rpgSeason.setText(srpg);
            apgSeason.setText(sapg);

            mpgCareer.setText(cmpg);
            fgpCareer.setText(cfgp);
            tppCareer.setText(ctpp);
            ftpCareer.setText(cftp);
            ppgCareer.setText(cppg);
            rpgCareer.setText(crpg);
            apgCareer.setText(capg);

            progressDialog.dismiss();
        }
    }

    private void setSeasonLayout(){
        mpgSeason = (TextView) findViewById(R.id.mpg_season);
        fgpSeason = (TextView) findViewById(R.id.fgp_season);
        tppSeason = (TextView) findViewById(R.id.tpp_season);
        ftpSeason = (TextView) findViewById(R.id.ftp_season);
        ppgSeason = (TextView) findViewById(R.id.ppg_season);
        rpgSeason = (TextView) findViewById(R.id.rpg_season);
        apgSeason = (TextView) findViewById(R.id.apg_season);
    }

    private void setCareerLayout(){
        mpgCareer = (TextView) findViewById(R.id.mpg_career);
        fgpCareer = (TextView) findViewById(R.id.fgp_career);
        tppCareer = (TextView) findViewById(R.id.tpp_career);
        ftpCareer = (TextView) findViewById(R.id.ftp_career);
        ppgCareer = (TextView) findViewById(R.id.ppg_career);
        rpgCareer = (TextView) findViewById(R.id.rpg_career);
        apgCareer = (TextView) findViewById(R.id.apg_career);
    }

    private void setPlayerInfoLayout(){

        playerFirstName = (TextView) findViewById(R.id.player_first_name);
        playerLastName = (TextView) findViewById(R.id.player_last_name);
        playerNumber = (TextView) findViewById(R.id.player_number);
        playerPosition = (TextView) findViewById(R.id.player_position);
        playerImage = (ImageView) findViewById(R.id.player_image);

        height = (TextView) findViewById(R.id.player_height);
        weight = (TextView) findViewById(R.id.player_weight);
        born = (TextView) findViewById(R.id.born);
        age = (TextView) findViewById(R.id.age);
        from = (TextView) findViewById(R.id.from);
        debut = (TextView) findViewById(R.id.debut);
        years = (TextView) findViewById(R.id.years);
        previously = (TextView) findViewById(R.id.previously);

    }
}
