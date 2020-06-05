package com.hfad.testprep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePlay extends AppCompatActivity {

    private Handler animationHandler, timeHandler;
    private Time time;
    private Intent gameOverIntent;
    BackgroundView backgroundView;
    List<String[]> answers;
    private float acelVal, acelLast, shake;
    private TextView timeDisplay;
    private TextView scoreDisplay;
    List<String[]> rows;
    Difficulty gameDifficulty;
    SharedPreferences test;
    int timeScore,totalScore;

    public GamePlay() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);



        backgroundView = findViewById(R.id.backgroundView);
        timeDisplay = findViewById(R.id.timeDisplay);
        TextView gameQuestionDisplay = findViewById(R.id.gameQuestion);
        scoreDisplay = findViewById(R.id.scoreDisplay);
        gameOverIntent = new Intent(this, GameOver.class);

        scoreDisplay.setText("0"); //sets initial score display to 0 before countdown

        timeHandler = new Handler(); //timeHandler
        animationHandler = new Handler(); //animationHandler

        //for the shake sensor
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
        timeScore = 0;

        //getting difficulty from settings
        test = PreferenceManager.getDefaultSharedPreferences(this);
        setDifficulty(test.getString("difficulty", "MEDIUM"));

        time = new Time(gameDifficulty);

        //reads questions from csv using AnswerGetter Class
        AnswerGetter answerGetter = new AnswerGetter(GamePlay.this);
        rows = new ArrayList<>();
        answers = new ArrayList<>();
        try {
            rows = answerGetter.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < rows.size(); i++) {
            answers.add(i, rows.get(i));
        }

        //this starts the game
        backgroundView.startGame(gameDifficulty, answers, gameQuestionDisplay);
        backgroundView.startRound(); //TODO: do we need this

        time.startGameTime();

        //end game and start gameover screen
        // this removes bubbles from screen when game ends
        //score is based on the time user popped bubble and total survived rounds
        Runnable gameTime = new Runnable() {
            @Override
            public void run() {
                if (time.getGameStatus()) {

                    if (!time.checkIfTimeOut() & time.countDownDone()) {
                        time.tick();
                        timeDisplay.setText(String.valueOf(time.getTime()));

                    } else if (!time.checkIfTimeOut() & !time.countDownDone()) {
                        time.countDownBeforeGame();
                        timeDisplay.setText(String.valueOf(time.getCountdown()));
                    } else { //end game and start gameover screen
                        time.stopGameTime();
                        backgroundView.endGame(); // this removes bubbles from screen when game ends
                        timeDisplay.setText("game over!");
                        totalScore = backgroundView.getScore() + timeScore; //score is based on the time user popped bubble and total survived rounds
                        gameOverIntent.putExtra("score", totalScore);
                        startActivity(gameOverIntent);

                    }
                    if (backgroundView.getTimeReset()) {
                        timeScore += time.getTime();
                        time.resetTime();
                        timeDisplay.setText(String.valueOf(time.getTime()));
                        scoreDisplay.setText(String.valueOf(timeScore));
                    }
                }
                timeHandler.postDelayed(this, 1000);
            }

        };

        Runnable gameAnimation = new Runnable() {
            @Override
            public void run() {
                if (time.getGameStatus()) {
                    backgroundView.invalidate();
                }
                animationHandler.postDelayed(this, 24);
            }
        };
        animationHandler.post(gameAnimation);
        timeHandler.post(gameTime);
    }
    //shake sensor
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((x * x + y * y + z * z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            if (shake > 11) {
                backgroundView.separateBubbles();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    private void setDifficulty(String string) {

        switch (string) {
            case "EASY":
                gameDifficulty = Difficulty.EASY;
                break;
            case "MEDIUM":
                gameDifficulty = Difficulty.MEDUIM;
                break;
            case "HARD":
                gameDifficulty = Difficulty.HARD;
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        time.stopGameTime();
    }

    @Override
    protected void onStart() {
        super.onStart();
        time.startGameTime();
    }
}