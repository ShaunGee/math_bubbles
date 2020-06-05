package com.hfad.testprep;


import android.graphics.Color;
import android.graphics.Paint;
import java.util.List;
import java.util.Random;


//This class helps with the game states, number of bubbles per difficulty and keeping track of score
class Game {

    private boolean gameActive, roundActive;
    private int score;
    private Difficulty difficulty;
    private int bubbleSpeed;

    Game(Difficulty difficulty) {
        bubbleSpeed = 1;
        this.difficulty = difficulty;

        Paint bubble = new Paint();
        Random random = new Random();

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        int color = Color.rgb(red, green, blue);

        bubble.setColor(color);
    }

    void increaseBubbleSpeed() {
        ++bubbleSpeed;
    }

    int getBubbleSpeed() {
        return bubbleSpeed;
    }

    int getAmountOfDisplayBubbles() {
        int i = 0;
        switch (difficulty) {
            case EASY:
                i = 4;
                break;
            case MEDUIM:
                i = 5;
                break;
            case HARD:
                i = 6;
                break;
        }
        return i;
    }

    void startGame() {
        gameActive = true;
    }

    void endGame() {
        gameActive = false;
    }

    void startRound() {
        roundActive = true;
    }


    boolean getRoundActiveStatus() {
        return roundActive;
    }

    boolean isActive() {
        return gameActive;
    }


    void deleteBubble() {
        score++;
    }

    int getScore() {
        return score;
    }

    boolean getGameActiveStatus() {
        return gameActive;
    }

}

