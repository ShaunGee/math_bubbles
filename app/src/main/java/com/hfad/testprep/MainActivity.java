package com.hfad.testprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view) {
        Intent intent = new Intent(this, GamePlay.class);
        startActivity(intent);
    }

    public void leaderBoard(View view) {
        Intent intent = new Intent(this, LeaderBoard.class);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void instructions(View view) {
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }
}
