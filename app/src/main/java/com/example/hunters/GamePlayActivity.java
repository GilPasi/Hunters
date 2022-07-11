package com.example.hunters;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GamePlayActivity extends AppCompatActivity
        implements View.OnClickListener
        {

    static final int INIT = 100000;// The initial value is a constant to mark the beginning of a new turn

    static boolean currentTurn = (Math.random()*10)%2==0; // Generate a random boolean
    static Player p1 = new Player(1);
    static Player p2 = new Player(1);
    static int firstCardIndex = INIT,secondCardIndex;
    static int leftMoves = 2;
    static final int SETS = 4, SET_SIZE = 3;
    static final int BONUS = 15;


    static Card c1 = new Card(0,'a',5);
    static Card c2 = new Card(0,'b',5);
    static Card c3 = new Card(0,'c',5);

    static Card c4 = new Card(1,'a',7);
    static Card c5 = new Card(1,'b',7);
    static Card c6 = new Card(1,'c',7);

    static Card c7 = new Card(2,'a',3);
    static Card c8 = new Card(2,'b',3);
    static Card c9 = new Card(2,'c',3);

    static Card c10 = new Card(3,'a',15);
    static Card c11 = new Card(3,'b',5);
    static Card c12 = new Card(3,'c',5);

    static  Card [][] sortedCardsArray = {{c1,c2,c3},{c4,c5,c6},{c7,c8,c9},{c10,c11,c12}};
    static Card [][] cardsArray = new Card [SETS][SET_SIZE];

    static boolean canSleepHunt = false, huntScenario = false, saveOrSearch = false;

    ImageView card00;
    ImageView card01;
    ImageView card02;
    ImageView card10;
    ImageView card11;
    ImageView card12;
    ImageView card20;
    ImageView card21;
    ImageView card22;
    ImageView card30;
    ImageView card31;
    ImageView card32;
    ImageView [] imageViews;


            @Override
    protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);



        card00 = findViewById(R.id.card00) ;
        card01 = findViewById(R.id.card01);
        card02 = findViewById(R.id.card02);
        card10 = findViewById(R.id.card10);
        card11 = findViewById(R.id.card11);
        card12 = findViewById(R.id.card12);
        card20 = findViewById(R.id.card20);
        card21 = findViewById(R.id.card21);
        card22 = findViewById(R.id.card22);
        card30 = findViewById(R.id.card30);
        card31 = findViewById(R.id.card31);
        card32 = findViewById(R.id.card32);



        card00.setOnClickListener(this);
        card01.setOnClickListener(this);
        card02.setOnClickListener(this);
        card10.setOnClickListener(this);
        card11.setOnClickListener(this);
        card12.setOnClickListener(this);
        card20.setOnClickListener(this);
        card21.setOnClickListener(this);
        card22.setOnClickListener(this);
        card30.setOnClickListener(this);
        card31.setOnClickListener(this);
        card32.setOnClickListener(this);




        //Shuffle the cards
                //TODO uncomment this  block

                cardsArray = sortedCardsArray;

//
//                for(int i = 0; i < SETS; i++) {
//                    for(int j = 0; j < SET_SIZE; j++) {
//
//
//                        //Generate a new location
//                        int row = (int)(Math.random() * 10) % SETS;
//                        int col = (int)(Math.random() * 10) % SET_SIZE;
//
//                        if(cardsArray [row][col] != null )
//                            --j;
//                        else
//                            cardsArray[row][col] = sortedCardsArray[i][j];
//                    }
//                }
//
//




    }//End of main





    public void selectCard(View v){
        final int TEXT_BOT = 106, TEXT_TOP = 108;


        //Translate the bottom to indexes block:

        ImageView selectedButton = (ImageView) v;

        String name = selectedButton.toString();
        String buttonName = name.substring(name.length() - 3,name.length() - 1 );

        Toast.makeText(this,buttonName,Toast.LENGTH_LONG).show();//TODO ____________DEBUG_____________

        int cardIndexes = stringToInt(buttonName);


        if(canSleepHunt || cardIndexes == firstCardIndex)//Avoid accidental touch
            return;

        findCardByIndex(cardIndexes).status = 2; // Signal that the card is open
        --leftMoves;


            //Check for hunt scenario
            if(huntScenario){
                huntTurn(cardIndexes);
                return;
            }


            //Check for save or search scenario
            if(isPairFound(findCardByIndex(cardIndexes))){
                saveSearchTurn(cardIndexes);
                return;
            }

            //First Choice scenario
            if (firstCardIndex == INIT){
                firstCardIndex = cardIndexes;//Save the indexes for the next card
                return;
            }

            //No pair scenario
            if (findCardByIndex(cardIndexes).set != findCardByIndex(firstCardIndex).set){

                try
                {
                    Thread.sleep(3000); // Sleep the current thread for 1 second
                } catch (InterruptedException e){
                    Toast.makeText(this,"Problem occurred",Toast.LENGTH_LONG).show();//TODO ____________DEBUG__________

                }
                endTurn();//flip both cards
                return;

            }



            //Found pair scenario , can sleep or hunt
            else {

                // Add cards to the hand
                findCurrentPlayer().addCardToHand(findCardByIndex(cardIndexes));
                findCurrentPlayer().addCardToHand(findCardByIndex(firstCardIndex));

                secondCardIndex = cardIndexes;// Save indexes
                canSleepHunt = true;
             }

    }









    public void sleep(View v){

        if(!canSleepHunt){
            return; // Avoid accidental touch
        }

        // This button also functions as the "save button"
        if(saveOrSearch){
            findCurrentPlayer().addCardToHand(findCardByIndex(firstCardIndex));// Add the card

            exitSaveOrSearch();// Normalize again
            endTurn();
        }


        if(canSleepHunt)
            endTurn();
            //Take both cards

    }



    public void hunt(View v){
        // This button also functions as the "search button"

        if(!canSleepHunt && !saveOrSearch)
            return;// avoid accidental touch


        if(saveOrSearch){
            findCardByIndex(firstCardIndex).status = 3; // The card was discarded
            //TODO Discarding animations

            leftMoves = 2;// A new turn
            exitSaveOrSearch();

        }

        if(canSleepHunt) {// Avoid accidental touch
            //Visual sign of two more turns
            huntScenario = true;
            leftMoves += 2;
            canSleepHunt = false;
        }

    }

//___________Private methods______________

    private  void saveSearchTurn(int cardIndexes) {
        /*This function will run the procedure of turn were a player find the last member of the set.
         * If the card was found on the second turn, he can choose to either take the card or have another turn.
         * If the card was found on the first turn, the player will get the whole set and wont be able to choose.*/
        Toast.makeText(this,"saveSearch",Toast.LENGTH_LONG).show();//TODO ____________DEBUG_____________


            // If the card was found on the first try:
            if(leftMoves == 1){
                Player pCurrent = findCurrentPlayer();
                Player pOther = findOtherPlayer();
                pCurrent.transfer(pOther,findCardByIndex(cardIndexes).set);// Add the card
                pCurrent.transfer(pOther,findCardByIndex(cardIndexes).set);// One for each set member

                pCurrent.score += BONUS;
                endTurn();
            }

            else{
                // Get in to a save or search procedure
                enterSaveOrSearch();


            }
        }





    private void huntTurn(int cardIndexes){
        Player currentPlayer = findCurrentPlayer();



        if(findCardByIndex(cardIndexes).set != findCardByIndex(firstCardIndex).set ){
            leftMoves --;//If the card was not found, deduce one try
            if(leftMoves == 0){
                //Unsuccessful hunt
                endTurn();
                leftMoves = 3;// The other player gain another turn
                //Folding animations
            }
            //Otherwise the turn was just wasted

        }

        else{// The hunt was successful
            currentPlayer.addCardToHand(findCardByIndex(cardIndexes));
            currentPlayer.score += BONUS;
            endTurn();
            //Take all cards animations
        }
    }

    private  void enterSaveOrSearch(){

        //Change texts of both buttons
        TextView s_t =  findViewById(R.id.sleepText);
        TextView h_t =  findViewById(R.id.huntText);

        s_t.setText(R.string.save_title);
        h_t.setText(R.string.search_title);
        saveOrSearch = true;
        canSleepHunt = true;

    }


    private  void exitSaveOrSearch(){
        //Change texts of both buttons
        TextView s_t =  findViewById(R.id.sleepText);
        TextView h_t =  findViewById(R.id.huntText);

        s_t.setText(R.string.sleep_title);
        h_t.setText(R.string.hunt_title);
        saveOrSearch = false;
        canSleepHunt =false;

    }


    private static boolean inArray (Card [][] array, Card tester) {

        for (Card[] row : array)
            for (Card current : row)
                if (current == tester)
                    return true;
        return false;
    }//End of method

    private int stringToInt(String s){

        int ret = 0;
        for(int i = 0; i < s.length(); i++){
            ret *= 10;
            ret += s.charAt(i) - '0';
        }


        return ret;
    }


//TODO make static again
    private void endTurn(){
        Toast.makeText(this,"endTurn",Toast.LENGTH_LONG).show();//TODO ____________DEBUG__________



        imageViews = new ImageView[]{card00, card01, card02, card10, card11, card12, card20,
                card21, card22, card30, card31, card32};

        for(int i = 0 ; i < SET_SIZE*SETS; i++)
                    imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.card_rear));


        firstCardIndex = INIT;
        secondCardIndex = INIT;
        leftMoves = 2;
        canSleepHunt = false;
        currentTurn = !currentTurn;
        huntScenario = false;
        saveOrSearch = false;


    }

    private static Player findCurrentPlayer(){
        //Determine the current player
        Player currentPlayer;
        if(currentTurn)
            currentPlayer = p1;
        else
            currentPlayer = p2;
        return currentPlayer;
    }


    private static Player findOtherPlayer(){
        //Determine the other player
        Player currentPlayer;
        if(!currentTurn)
            currentPlayer = p1;
        else
            currentPlayer = p2;
        return currentPlayer;
    }


    private boolean isPairFound(Card ca){
        /*This method find out if the other cards from the set are on the board or at the hand of a player*/

        for(char i = 0; i < SET_SIZE; i++){//Check the cards on the sorted array
            if(sortedCardsArray[ca.set][i].status == 3) // If status is 3 the card is not in game
                return true;
        }
        return false;
    }


    private Card findCardByIndex(int index){

        return cardsArray[index / 10][index % 10]; }


    @Override
    public void onClick(View v) {

                if(canSleepHunt)//Avoid accidental touch
                    return;

        imageViews = new ImageView[]{card00, card01, card02, card10, card11, card12, card20,
                card21, card22, card30, card31, card32};

        // Look for the right card
        for(int i = 0; i < SET_SIZE*SETS; i++){
            if(imageViews [i] == v){

                Card current = cardsArray[i % SETS][i % SET_SIZE];
                switch(current.set){

                    case(0):
                        imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.alligator));


                        imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.card_rear));

                        break;

                    case(1):
                        imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.piranha));
                        break;

                    case(2):
                        imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.sloth));
                        break;


                    case(3):
                        imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.cassowary));
                        break;

                }

                //imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.title));
                selectCard(v);
                return;// No point to continue
            }
        }
    }


}

