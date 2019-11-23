package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //starting activity choose NEW GAME or load saving file for CONTINUE PLAY
        setContentView(R.layout.game_start_activity);
        ConstraintLayout startLayout = findViewById(R.id.game_start_menu);

        Button newGame = startLayout.findViewById(R.id.new_game);
        Button continuePlay = startLayout.findViewById(R.id.continue_play);

        newGame.setOnClickListener(v->{

            Intent toMainActivity = new Intent(this, MainActivity.class);
            toMainActivity.putExtra("continue play previous game", false);
            startActivity(toMainActivity);
            this.finish();

        });

        continuePlay.setOnClickListener(v->{

            Intent toMainActivity = new Intent(this, MainActivity.class);
            toMainActivity.putExtra("continue play previous game", true);
            startActivity(toMainActivity);
            this.finish();

        });



    }

}
