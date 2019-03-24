package com.example.dell.nbaparse.ViewItem;

import android.graphics.Bitmap;
import android.media.Image;

public class PlayerInfoItem {

    Bitmap playerImage;
    String playerName;
    String playerNumber;
    String playerPosition;

    public Bitmap getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(Bitmap playerImage) {
        this.playerImage = playerImage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(String playerPosition) {
        this.playerPosition = playerPosition;
    }
}
