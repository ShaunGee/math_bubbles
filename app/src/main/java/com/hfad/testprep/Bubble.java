package com.hfad.testprep;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.Random;

public class Bubble {
    private int screenWidth, screenHeight, bubblePosX, bubblePosY;
    private Paint bubble;
    boolean isActive;
    private int horizontalDir;
    private int verticalDir;
    private boolean isPopped;
    private Paint text;
    private String answer;
    private int speed;
    private Answers bubbleAnswers;

    static int tempSpeed;

    private Random random;
    private int size;


    public Bubble(PointF pointF, int speed, Answers answer) {

        random = new Random();
        this.speed = speed;
        tempSpeed = 2;
        size = 150; //bubble radius
        this.screenWidth = (int) pointF.x;
        this.screenHeight = (int) pointF.y;
        isActive = false;
        isPopped = false;
        this.answer = answer.getAnswer();
        bubbleAnswers = answer;

        if (random.nextBoolean()) {
            horizontalDir = 1;
        } else {
            horizontalDir = -1;
        }

        if (random.nextBoolean()) {
            verticalDir = -1;
        } else {
            verticalDir = 1;
        }

    }

    public void createBubble(Canvas canvas) {
        isActive = true;

        Random random = new Random();
        //set position
        bubblePosX = random.nextInt(screenWidth);
        bubblePosY = random.nextInt(screenHeight);


        //set color
        bubble = new Paint();
        text = new Paint();
        bubble.setColor(Color.BLUE);

        text.setTextAlign(Paint.Align.CENTER);
        text.setColor(Color.YELLOW);
        text.setTextSize(80);
        text.setAntiAlias(true);
        text.setTypeface(Typeface.DEFAULT_BOLD);

        canvas.drawCircle(bubblePosX, bubblePosY, size, bubble);
    }

    public boolean getPoppedStatus() {
        return isPopped;
    }

    public void popBubble() {
        isPopped = true;
        isActive = false;
    }

    public void updateBubblePos(Canvas canvas) {
        canvas.drawCircle(bubblePosX, bubblePosY, size, bubble);
        canvas.drawText(answer, bubblePosX, bubblePosY, text);
    }

    public boolean exists() {
        return isActive;
    }


    public void moveBubble() {

        //determines bounce
        if (bubblePosX + size >= screenWidth) {
            horizontalDir = -1;
        } else if (bubblePosX - size <= 0) {
            horizontalDir = 1;
        }
        if (bubblePosY + size >= screenHeight) {
            verticalDir = -1;
        } else if (bubblePosY - size <= 0) {
            verticalDir = 1;
        }

        bubblePosX = bubblePosX + (tempSpeed * horizontalDir * speed);
        bubblePosY = bubblePosY + (tempSpeed * verticalDir * speed);

        if (tempSpeed >= 2) {
            tempSpeed--;
        }

    }

    public boolean bubbleClicked(MotionEvent event) {

        boolean clicked = false;
        double area = Math.sqrt(Math.pow(event.getX() - bubblePosX, tempSpeed) + Math.pow(event.getY() - bubblePosY, tempSpeed));
        if (area < 15) {
            clicked = true;
        }
        return clicked;
    }

    public boolean isCorrect(String answer) {
        boolean correct = false;
        if (bubbleAnswers.checkAnswerr(answer)) {
            correct = true;
        }
        return correct;
    }

    public void disperse() {
        tempSpeed = 50;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            verticalDir = 1;
        } else {
            horizontalDir = -1;
        }
    }
}