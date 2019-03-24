package com.example.dell.nbaparse.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.nbaparse.R;
import com.example.dell.nbaparse.ViewItem.PlayerInfoItem;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PlayerInfoAdapter extends BaseAdapter {

    ArrayList<PlayerInfoItem> playerInfoItems = new ArrayList<>();

    public ArrayList<PlayerInfoItem> getPlayerInfoItems() {
        return playerInfoItems;
    }

    @Override
    public int getCount() {
        return playerInfoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return playerInfoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.playerinfo_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.player_image);
        TextView playerName = (TextView) convertView.findViewById(R.id.player_name);
        TextView playerNumber = (TextView) convertView.findViewById(R.id.player_number);
        TextView playerPosition = (TextView) convertView.findViewById(R.id.player_position);

        PlayerInfoItem playerInfoItem = playerInfoItems.get(position);

        imageView.setImageBitmap(playerInfoItem.getPlayerImage());
        playerName.setText(playerInfoItem.getPlayerName());
        playerNumber.setText(playerInfoItem.getPlayerNumber());
        playerPosition.setText(playerInfoItem.getPlayerPosition());

        return convertView;
    }


    public void addItem(Bitmap image, String name, String number, String position){
        PlayerInfoItem item = new PlayerInfoItem();
        item.setPlayerImage(image);
        item.setPlayerName(name);
        item.setPlayerNumber(number);
        item.setPlayerPosition(position);
        playerInfoItems.add(item);
    }
}
