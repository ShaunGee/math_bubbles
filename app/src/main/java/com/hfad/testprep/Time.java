package com.hfad.testprep;

class Time extends Thread {
    private int setTimeLimit, time,countdown;
    private boolean gameActive;

    Time(Difficulty difficulty) {
        countdown = 4;
        //TODO: we dont need difficulty in here I think....Actually we do but check the diffierence between setTIme and time

        switch (difficulty){
            case EASY:
                setTimeLimit=11;
                time = 11;
                break;
            case MEDUIM:
                setTimeLimit=6;
                time=6;
                break;
            case HARD:
                setTimeLimit=4;
                time=4;
                break;
        }
    }
    void countDownBeforeGame(){
        if (countdown >0){
        countdown--;}
    }

    boolean countDownDone(){
        boolean i = false;
        if (countdown <=0){
            i = true;
        }
        return i;
    }

    String getCountdown() {
        String t = String.valueOf(countdown);
        if(countdown<1){
            t = "GO!";
        }
        return t;
    }
    void startGameTime(){
        gameActive = true;
    }
    void stopGameTime(){
        gameActive = false;
    }
    boolean getGameStatus(){
        return gameActive;
    }

    void tick(){
        time--;
    }

    boolean checkIfTimeOut(){
        boolean t = false;
        if (time<1){
            t = true;
        }
        return t;
    }

    void resetTime(){
        time = setTimeLimit;
    }

    int getTime(){
        return time;
    }

}


