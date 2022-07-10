package com.example.hunters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView exm = findViewById(R.id.Title);

        exm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(v==exm){
                    exm.setBackgroundResource(R.drawable.card_button_rear);
                }

            }
        });

        }




    public void moveToGamePlay(View v){
        Intent i = new Intent(this,GamePlayActivity.class);
        startActivity(i);
    }


    public void moveToRules(View v){
        Intent i = new Intent(this,RulesActivity.class);
        startActivity(i);
    }

}