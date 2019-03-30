package com.example.dell.nbaparse.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.nbaparse.Adapter.TeamRankAdapter;
import com.example.dell.nbaparse.R;
import com.example.dell.nbaparse.ViewItem.TeamRankItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConferenceRank extends Fragment {

    private ArrayList<TeamRankItem> teamRankItems = new ArrayList<>();

    private RecyclerView recyclerView;

    private String jsonArrayString = null;
    private String teams = null;
    private JSONArray conferenceArray = null;
    private JSONArray teamArray = null;

    private RecyclerView.LayoutManager layoutManager;

    public static ConferenceRank newInstance(String conference, String teams) {
        ConferenceRank fragment = new ConferenceRank();
        Bundle args = new Bundle();
        args.putString("conference", conference);
        args.putString("teams", teams);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jsonArrayString = getArguments().getString("conference");
            teams = getArguments().getString("teams");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conference_rank, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        new getData().execute();
        return view;
    }


    public class getData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        private String triCode;
        private String win;
        private String loss;
        private String winPct;
        private String gb;
        private String last10;
        private String strk;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                conferenceArray = new JSONArray(jsonArrayString);
                teamArray = new JSONArray(teams);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < conferenceArray.length(); i++) {
                try {
                    JSONObject jsonObject = conferenceArray.getJSONObject(i);
                    String teamId = jsonObject.optString("teamId");
                    win = jsonObject.optString("win");
                    loss = jsonObject.optString("loss");
                    winPct = jsonObject.optString("winPctV2");
                    gb = jsonObject.optString("gamesBehind");
                    last10 = jsonObject.optString("lastTenWin") + "-" + jsonObject.optString("lastTenLoss");
                    strk = jsonObject.optString("streak");

                    for (int j = 0; j < teamArray.length(); j++) {
                        JSONObject getTriCode = teamArray.getJSONObject(j);
                        if (getTriCode.optString("teamId").equals(teamId)) {
                            triCode = getTriCode.optString("tricode");
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                teamRankItems.add(new TeamRankItem(i + 1, triCode, win, loss, winPct, gb, last10, strk));
                Log.d("items", i + 1 + " " + triCode + " " + win + " " + loss + " " + winPct + " " + gb + " " + last10 + " " + strk);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TeamRankAdapter adapter = new TeamRankAdapter(teamRankItems);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();

        }
    }

}
