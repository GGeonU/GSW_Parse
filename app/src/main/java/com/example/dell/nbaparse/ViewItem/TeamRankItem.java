package com.example.dell.nbaparse.ViewItem;

import com.example.dell.nbaparse.Utils;

public class TeamRankItem {

    Utils utils = new Utils();

    private int count;
    private String teamId;
    private String tricode;
    private String win;
    private String loss;
    private String winPct;
    private String gb;
    private String last10;
    private String strk;

    public TeamRankItem(int count, String tricode, String win, String loss,
                        String winPct, String gb, String last10, String strk) {
        this.count = count;
        this.tricode = tricode;
        this.win = win;
        this.loss = loss;
        this.winPct = winPct;
        this.gb = gb;
        this.last10 = last10;
        this.strk = strk;
    }

    public int getCount() {
        return count;
    }

    public String getTricode() {
        return tricode;
    }

    public String getWin() {
        return win;
    }

    public String getLoss() {
        return loss;
    }

    public int getTeamLogoR(){
        int r = utils.getR(tricode.toLowerCase());
        return r;
    }

    public String getWinPct() {
        return winPct;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getGb() {
        return gb;
    }

    public String getLast10() {
        return last10;
    }

    public String getStrk() {
        return strk;
    }
}
