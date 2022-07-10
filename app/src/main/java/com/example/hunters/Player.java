package com.example.hunters;

public class Player {
    int number;
    int score;
    int selections;
    String name;
    Card [] hand;
    final int handSize = 12;

    Player(int number){
        this.number = number;

        hand = new Card[handSize];
        score = 0;
        selections = 2;
        name ="Player " + number;
    }

    public void addCardToHand(Card c){
        for(int i = 0; i < handSize; i++){
            if(hand[i] == null)
                hand[i] = c;
        }
        score += c.points;// Add the score as well

        c.onBoard = false; // The card is no longer on the board

    }

    public void transfer(Player p, int set){
        /**This function transfer a card relaying on their given set number */
        int i = 0 ;
        while(i < handSize){
            if(hand[i++] == null)// Prevent runtime problem
                continue;

            if(hand[i].set == set){
                p.addCardToHand(hand[i]);
                //Subtract from the other player
                score -= hand[i].points;
                hand[i] = null;

            }//Close if
        }//Close while
    }//Close transfer function
}//Close class
