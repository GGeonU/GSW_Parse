package com.example.dell.nbaparse.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.nbaparse.R;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ShowLeader extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Bitmap bitmap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url;
    private String lastLeader;
    private String lastLeadStat;
    private String part;
    private int rankCount;

    // TODO: Rename and change types and number of parameters
    public static ShowLeader newInstance(String param1, String param2, String url, String lastLeader, String lastLeadStat, int rankCount, String part) {
        ShowLeader fragment = new ShowLeader();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("imageUrl", url);
        args.putString("lastLeader", lastLeader);
        args.putString("lastLeadStat", lastLeadStat);
        args.putInt("count", rankCount);
        args.putString("part", part);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            url = getArguments().getString("imageUrl");
            lastLeader = getArguments().getString("lastLeader");
            lastLeadStat = getArguments().getString("lastLeadStat");
            rankCount = getArguments().getInt("count");
            part = getArguments().getString("part");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_rank_view, container, false);
        TextView stat = (TextView) view.findViewById(R.id.player_stat);
        TextView name = (TextView) view.findViewById(R.id.player_name);
        TextView kind = (TextView) view.findViewById(R.id.kind);
        ImageView imageView = (ImageView) view.findViewById(R.id.player_image);
        TextView leader = (TextView) view.findViewById(R.id.last_team_leader);
        TextView lastStat = (TextView) view.findViewById(R.id.last_stat);
        TextView last = (TextView) view.findViewById(R.id.last);
        TextView season = (TextView) view.findViewById(R.id.season);
        TextView count = (TextView) view.findViewById(R.id.count);

        if(rankCount == 1){
            count.setText("1st Team Leader");
        }
        else if(rankCount == 2){
            count.setText("2nd Team Leader");
        }
        else{
            count.setText("3rd Team Leader");
        }

        stat.setText(mParam1);
        kind.setText(part);
        name.setText(mParam2);
        season.setText("2018-19 Season");
        last.setText("Last Team Leader");
        leader.setText(lastLeader);
        lastStat.setText(lastLeadStat);
        imageView.setImageBitmap(GetImageFromURL(url));

        return view;
    }

    private Bitmap GetImageFromURL(String strImageURL) {
        Bitmap imgBitmap = null;
        StrictMode.enableDefaults();

        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);

            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBitmap;
    }

}
