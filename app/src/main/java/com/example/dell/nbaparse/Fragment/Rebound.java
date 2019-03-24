package com.example.dell.nbaparse.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.nbaparse.Adapter.FragmentViewAdapter;
import com.example.dell.nbaparse.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Rebound extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ViewPager viewPager;
    TabLayout tabLayout;

    // TODO: Rename and change types and number of parameters
    public static LeaderContent newInstance(String param1, String param2) {
        LeaderContent fragment = new LeaderContent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_point, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabb);


        // Inflate the layout for this fragment
        new getData().execute();
        return view;
    }


    private class getData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        FragmentViewAdapter adapter = new FragmentViewAdapter(getChildFragmentManager());
        Elements name;
        Elements stat;
        Elements imageUrl;
        Elements lastPlayer;
        Elements lastStat;
        ArrayList<String> urlList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Document doc = Jsoup.connect("https://www.nba.com/warriors/stats/leaders").get();
                Elements point = doc.select("ul.nav.nav-pills.nav-pills-stats-leaders.nav-pills-stats-leaders-REB");
                name = point.select("div.player-name");
                stat = point.select("div.stat-num");
                imageUrl = doc.select("section.tab-container.tab-container-stats-leaders.tab-container-stats-leaders-REB").select("[src]");
                lastPlayer = doc.select(".stat-player-last.stat-player-last-stats-leaders.stat-player-last-stats-leaders-REB.stat-player-last-stats-leaders-REB-1");
                lastStat = doc.select(".stat-figure-last.stat-figure-last-stats-leaders.stat-figure-last-stats-leaders-REB.stat-figure-last-stats-leaders-REB-1");

                for(int i=0; i<3; i++) {
                    String buffer = imageUrl.get(i).toString().replace("\" alt=\"\">","").replace("<img src=\"","");
                    urlList.add(buffer);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for(int i=0; i<urlList.size(); i++){
                adapter.addFragment(new FragmentView().newInstance(stat.get(i).text(), name.get(i).text(),urlList.get(i), lastPlayer.text(), lastStat.text(), i+1, "reb"), name.get(i).text()+"\n"+stat.get(i).text());
            }
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            progressDialog.dismiss();

        }
    }
}
