package com.example.hunters;

import android.graphics.drawable.Drawable;

public class Card{

    int set;
    int row;
    int col;
    char position;
    int points;
    int status;// 1 =  in game card,close. 2 = in game card, open. 3 = card taken
    String picName;


    Card (int set, char position, int score){
        row = 0;
        col = 0;
        this.set = set;
        this.position = position;
        this.points = points;
        status = 1;
        picName = null;
    }

    public String toString(){
        return "Card " + set + position;
    }
}
