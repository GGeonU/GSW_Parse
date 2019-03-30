package com.example.dell.nbaparse.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.nbaparse.R;
import com.example.dell.nbaparse.ViewItem.TeamRankItem;

import java.util.ArrayList;

public class TeamRankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TeamRankItem> teamRankItems;

    public TeamRankAdapter(ArrayList<TeamRankItem> teamRankItems) {
        this.teamRankItems = teamRankItems;
    }

    public static class TeamRankViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView triCode;
        private TextView win;
        private TextView loss;
        private TextView winPct;
        private TextView gb;
        private TextView last10;
        private TextView strk;
        private ImageView teamLogo;

        public TeamRankViewHolder(View itemView) {
            super(itemView);
            teamLogo = (ImageView) itemView.findViewById(R.id.team_logo);
            rank = (TextView) itemView.findViewById(R.id.team_rank);
            triCode = (TextView) itemView.findViewById(R.id.tricode);
            win = (TextView) itemView.findViewById(R.id.win);
            loss = (TextView) itemView.findViewById(R.id.loss);
            winPct = (TextView) itemView.findViewById(R.id.win_pct);
            gb = (TextView) itemView.findViewById(R.id.gb);
            last10 = (TextView) itemView.findViewById(R.id.last_10);
            strk = (TextView) itemView.findViewById(R.id.strk);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_rank_item, parent, false);

        return new TeamRankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TeamRankViewHolder teamRankViewHolder = (TeamRankViewHolder) holder;
        TeamRankItem teamRankItem = teamRankItems.get(position);

        if((position+1)%2==0){
            teamRankViewHolder.itemView.setBackgroundColor(Color.parseColor("#F3F4F5"));
        }

        teamRankViewHolder.rank.setText(String.valueOf(position+1));
        teamRankViewHolder.triCode.setText(teamRankItem.getTricode());
        teamRankViewHolder.teamLogo.setImageResource(teamRankItem.getTeamLogoR());
        teamRankViewHolder.win.setText(teamRankItem.getWin());
        teamRankViewHolder.loss.setText(teamRankItem.getLoss());
        teamRankViewHolder.winPct.setText(teamRankItem.getWinPct());
        teamRankViewHolder.gb.setText(teamRankItem.getGb());
        teamRankViewHolder.last10.setText(teamRankItem.getLast10());
        teamRankViewHolder.strk.setText(teamRankItem.getStrk());
    }

    @Override
    public int getItemCount() {
        return teamRankItems.size();
    }
}
