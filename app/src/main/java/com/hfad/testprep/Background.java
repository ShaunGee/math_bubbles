package com.hfad.testprep;
/*
      Shamelessly stolen from Jason Holdsworth's TwitterDemo tutorial
*/

class Background {
    static void run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
