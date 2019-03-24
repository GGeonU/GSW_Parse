package com.example.dell.nbaparse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.nbaparse.R;
import com.example.dell.nbaparse.ViewItem.ScheduleItem;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ScheduleItem> scheduleItemArrayList;

    public ScheduleAdapter(ArrayList<ScheduleItem> scheduleItemArrayList) {
        this.scheduleItemArrayList = scheduleItemArrayList;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView homeAway;
        TextView date;
        TextView time;
        TextView area;
        TextView teamName;
        ImageView opponentLogo;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            homeAway = (TextView) itemView.findViewById(R.id.home_away);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            area = (TextView) itemView.findViewById(R.id.area);
            teamName = (TextView) itemView.findViewById(R.id.team_name);
            opponentLogo = (ImageView) itemView.findViewById(R.id.team_logo);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;

        scheduleViewHolder.homeAway.setText(scheduleItemArrayList.get(position).getHomeAway());

        if(scheduleItemArrayList.get(position).getHomeAway().equals("true")){
            scheduleViewHolder.homeAway.setText("H\no\nm\ne");
        }

        else if(scheduleItemArrayList.get(position).getHomeAway().equals("false")){
            scheduleViewHolder.homeAway.setBackgroundColor(Color.parseColor("#999999"));
            scheduleViewHolder.homeAway.setText("A\nw\na\ny");
        }

        scheduleViewHolder.date.setText(scheduleItemArrayList.get(position).getDate());
        scheduleViewHolder.time.setText(scheduleItemArrayList.get(position).getTime());
        scheduleViewHolder.area.setText(scheduleItemArrayList.get(position).getArea());
        scheduleViewHolder.teamName.setText(scheduleItemArrayList.get(position).getTeamName());
        scheduleViewHolder.opponentLogo.setImageResource(scheduleItemArrayList.get(position).getR());
    }

    @Override
    public int getItemCount() {
        return scheduleItemArrayList.size();
    }


}
