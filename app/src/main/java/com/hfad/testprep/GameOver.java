package com.hfad.testprep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import java.util.Locale;

public class GameOver extends AppCompatActivity {
    TextView highScoreDisplay;
    EditText playerNameInput;
    Intent gamePlay, leaderBoard;
    AppDbManager dbManager;
    SQLiteDatabase db;
    Cursor cursor;
    CheckBox twitterCheckBox;
    private Twitter twitter;
    private String statusString;
    int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        twitter = TwitterFactory.getSingleton();
        leaderBoard = new Intent(this, LeaderBoard.class);

        twitterCheckBox = findViewById(R.id.twitterCheckBox);
        playerNameInput = findViewById(R.id.gameOverTextInput);
        highScoreDisplay = findViewById(R.id.gameOverScoreDisplay);
        gamePlay = getIntent();

        highScore = gamePlay.getIntExtra("score", -1);

        highScoreDisplay.setText(String.valueOf(highScore));
        //db stuff
        dbManager = new AppDbManager(this);
        db = dbManager.getReadableDatabase();
        cursor = db.query(dbManager.getScoreTableName(), new String[]{"_id", "name", "score"}, null, null, "score", null, "score DESC");

    }

    @SuppressLint("DefaultLocale")
    public void scoreSubmit(View view) {
        if (twitterCheckBox.isChecked()) {

            if (!playerNameInput.getText().toString().isEmpty()) {
                System.out.println(playerNameInput.getText().toString());
                AppDbManager.insertScore(db, playerNameInput.getText().toString(), gamePlay.getIntExtra("score", 0));
                statusString = String.format(" %s has just gotten a Highscore of %d on Math Bubbles!\n Can you beat his score? Download and Play!"
                        , playerNameInput.getText().toString(), highScore);
                updateTwitter();
                startActivity(leaderBoard);

            } else {
                emptyPlayerNamePrompt();
            }
        } else{
            if(playerNameInput.getText().toString().isEmpty()){
                emptyPlayerNamePrompt();
            }
            else {
            dbManager.insertScore(db, playerNameInput.getText().toString(), gamePlay.getIntExtra("score", 0));
            startActivity(leaderBoard);
            }
        }
    }

    private void updateTwitter() {
        //credit to matthew_small for sharing this code on slack for us to use.
        Background.run(new Runnable() {
            @Override
            public void run() {
                if (isAuthorised()) {
                    try {
                        twitter.updateStatus(statusString);
                        Log.i("GameOver", String.format(Locale.getDefault(), "Status updated: %s", statusString));
                    } catch (TwitterException e) {
                        Log.i("MainActivity", String.format(Locale.getDefault(),
                                "Something bad happened while tweeting: %s", e.toString()));
                    }
                } else {
                    Log.i("GameOver", "Unable to update status. User not authorised.");
                }
            }
        });

    }

    private boolean isAuthorised() {
        try {
            twitter.verifyCredentials();
            Log.i("MainActivity", "User is verified");
            return true;
        } catch (Exception e) {
            Log.i("MainActivity", "User is not verified");
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private void emptyPlayerNamePrompt(){
        Toast toast = Toast.makeText(getApplicationContext(), "No name entered. Please enter a name", Toast.LENGTH_SHORT);
        toast.show();
        System.out.println("not going through");
    }
}