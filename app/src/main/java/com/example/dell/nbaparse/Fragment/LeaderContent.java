package com.example.dell.nbaparse.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

public class LeaderContent extends Fragment {

    // TODO: Rename and change types of parameters
    private String param;

    ViewPager viewPager;
    TabLayout tabLayout;

    // TODO: Rename and change types and number of parameters
    public static LeaderContent newInstance(String param) {
        LeaderContent fragment = new LeaderContent();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString("param");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_point, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabb);

        // Inflate the layout for this fragment

        new getData().execute(param);
        return view;
    }



    private class getData extends AsyncTask<String, Void, String> {
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
        protected String doInBackground(String... strings) {
            try {

                Log.d("strings" , strings[0]);

                Document doc = Jsoup.connect("https://www.nba.com/warriors/stats/leaders").get();
                Elements point = doc.select("ul.nav.nav-pills.nav-pills-stats-leaders.nav-pills-stats-leaders-"+strings[0]);
                name = point.select("div.player-name");
                stat = point.select("div.stat-num");
                imageUrl = doc.select("section.tab-container.tab-container-stats-leaders.tab-container-stats-leaders-"+strings[0]).select("[src]");
                lastPlayer = doc.select(".stat-player-last.stat-player-last-stats-leaders.stat-player-last-stats-leaders-"+strings[0]+".stat-player-last-stats-leaders-"+strings[0]+"-1");
                lastStat = doc.select(".stat-figure-last.stat-figure-last-stats-leaders.stat-figure-last-stats-leaders-"+strings[0]+".stat-figure-last-stats-leaders-"+strings[0]+"-1");

                for(int i=0; i<3; i++) {
                    String buffer = imageUrl.get(i).toString().replace("\" alt=\"\">","").replace("<img src=\"","");
                    urlList.add(buffer);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(strings[0].contains("PCT"))
                return "%";
            else
                return strings[0];
        }

        @Override
        protected void onPostExecute(String part) {
            for(int i=0; i<urlList.size(); i++){
                adapter.addFragment(new FragmentView().newInstance(stat.get(i).text().replaceAll("%", ""), name.get(i).text(),urlList.get(i), lastPlayer.text(), lastStat.text(), i+1, part.toLowerCase()), name.get(i).text()+"\n"+stat.get(i).text());
            }
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);
            progressDialog.dismiss();

        }
    }
}
