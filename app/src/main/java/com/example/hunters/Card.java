package com.example.hunters;

import android.graphics.drawable.Drawable;

public class Card{

    int set;
    int row;
    int col;
    char position;
    int points;
    boolean onBoard;
    String picName;


    Card (int set, char position, int score){
        row = 0;
        col = 0;
        this.set = set;
        this.position = position;
        this.points = points;
        onBoard = true;
        picName = null;
    }

    Card (int set, char position, int score,String picName){
        row = 0;
        col = 0;
        this.set = set;
        this.position = position;
        this.points = points;
        this.picName = picName;
        onBoard = true;
    }

    public String toString(){
        return "Card " + set + position;
    }
}
