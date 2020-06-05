package com.hfad.testprep;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BackgroundView extends View {

    int randQuestionId;
    Difficulty difficulty;
    private String roundAnswer;
    private PointF gameScreen;
    int bubbleToDisplay;
    List<Bubble> bubbles;
    private TextView questionTextDisplay;
    String roundQuestion;
    Game game;
    List<String[]> answers;
    private boolean timerReset;


    public BackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);

        answers = new ArrayList<>();
        bubbles = new ArrayList<>();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        gameScreen = new PointF();
        gameScreen.x = w;
        gameScreen.y = h;

        setQuestion();

        createbubbleList();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (game.getGameActiveStatus()) {

            if (game.getRoundActiveStatus()) {
                for (int i = 0; i < bubbles.size(); i++) {
                    //create a list of bubbles here


                    if (bubbles.get(i).exists() & !bubbles.get(i).getPoppedStatus()) {
                        bubbles.get(i).moveBubble();
                        bubbles.get(i).updateBubblePos(canvas);
                    } else if (!bubbles.get(i).exists() & !bubbles.get(i).getPoppedStatus()) {
                        bubbles.get(i).createBubble(canvas);
                    } else {
                        bubbles.get(i).exists();
                    }
                }
            }
        }
    }


    public void createbubbleList() {
        List<Bubble> toGoList = new ArrayList<>();

        if (timerReset){ //stops it from increasing speed when time resets
            game.increaseBubbleSpeed();
        }

        toGoList.add(new Bubble(gameScreen, game.getBubbleSpeed(), new Answers(Arrays.toString(answers.get(randQuestionId)))));  //adds the rounds question to the list
        for (int i = 0; i < (bubbleToDisplay - 1); i++) {
            Random random = new Random();
            toGoList.add(new Bubble(gameScreen, game.getBubbleSpeed(), new Answers(Arrays.toString(answers.get(random.nextInt(answers.size()))))));
        }
        bubbles = toGoList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //when correct bubble is clicked
        for (int i = 0; i < bubbles.size(); i++) {
            if (bubbles.get(i).bubbleClicked(event) & bubbles.get(i).isCorrect(roundAnswer) & game.isActive()) {

                bubbles.get(i).popBubble();
                bubbles.remove(i);
                game.deleteBubble();
                setQuestion();
                timerReset = true;
                createbubbleList();
            }
        }
        return super.onTouchEvent(event);

    }

    public boolean getTimeReset(){
        boolean t = false;
        if (timerReset){
            t = true;
            timerReset=false;
        }
        return t;
    }

    //starts the game and sets values
    public void startGame(Difficulty difficulty, List<String[]> answers, TextView questionTextDisplay) {
        this.questionTextDisplay = questionTextDisplay;
        this.difficulty = difficulty;
        game = new Game(difficulty);
        this.answers = answers;
        game.startGame();
        timerReset = false;
        bubbleToDisplay = game.getAmountOfDisplayBubbles();
    }

    public void endGame(){
        game.endGame();
    }

    public void startRound() { game.startRound(); }

    public int getScore(){
        return game.getScore();
    }

    public void setQuestion() {
        Random random = new Random();
        randQuestionId = random.nextInt(answers.size());
        //game.includeQuestion(randQuestionId); //sets a random question and answer from list to be included
        String[] a = answers.get(randQuestionId);
        roundQuestion = a[0].replaceAll("\\[|\\]", ""); //replaces unnecesary things from string
        roundAnswer = a[1].replaceAll("\\[|\\]", "");
        questionTextDisplay.setText(roundQuestion);
    }

    public void separateBubbles() {
        for (int i = 0; i < bubbles.size(); i++) {
                bubbles.get(i).disperse();
        }
    }
}