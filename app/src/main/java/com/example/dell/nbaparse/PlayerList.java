package com.example.dell.nbaparse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.nbaparse.Adapter.PlayerInfoAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerList extends AppCompatActivity {

    PlayerInfoAdapter adapter = new PlayerInfoAdapter();
    Utils utils = new Utils();

    private ArrayList<String> playerName = new ArrayList<>();
    private ArrayList<String> playerNumber = new ArrayList<>();
    private ArrayList<String> playerPosition = new ArrayList<>();
    private ArrayList<String> playerImageUrl = new ArrayList<>();
    private ArrayList<Bitmap> playerImage = new ArrayList<>();

    private Toolbar toolbar;
    private ListView roster;   // show player list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);
        roster = (ListView) findViewById(R.id.roster);

        setToolbar();
        new getRoster().execute();

    }

    private class getRoster extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.getPlayerInfoItems().clear();
            progressDialog = new ProgressDialog(PlayerList.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://www.nba.com/warriors/stats").get();
                Elements playerInfoElements = doc.select("table.stats-table.player-stats.season-averages.table.table-striped.table-bordered.sticky-enabled");
                Elements playerNameElements = playerInfoElements.select("span.playerName");
                Elements playerNumberElements = doc.select("span.playerNumber");
                Elements playerPositionElements = doc.select("span.playerPosition");
                Elements playerImageElements = doc.select("div.player-name__inner-wrapper").select("[src]");

                for (int i = 0; i < playerNameElements.size(); i++) {
                    playerName.add(playerNameElements.get(i).text());
                    playerNumber.add(playerNumberElements.get(i).text());
                    playerPosition.add(playerPositionElements.get(i).text());
                    playerImageUrl.add(playerImageElements.get(i).toString().replace("<img src=\"", "").replace("\" width=\"60px\" alt=\"\">", ""));
                }

                for (int i = 0; i < playerImageUrl.size(); i++) {
                    playerImage.add(utils.toImage(playerImageUrl.get(i)));
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error", "document parsing error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < playerImage.size(); i++) {
                adapter.addItem(playerImage.get(i), playerName.get(i), playerNumber.get(i), playerPosition.get(i));
            }
            roster.setAdapter(adapter);
            progressDialog.dismiss();

            roster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PlayerList.this, ShowPlayerStats.class);
                    intent.putExtra("name", playerName.get(position));
                    intent.putExtra("number", playerNumber.get(position));
                    intent.putExtra("position", playerPosition.get(position));
                    intent.putExtra("image", playerImage.get(position));
                    intent.putExtra("listViewPosition", position);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    } // toolbar back button

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Roster");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }  // set custom toolbar
}

