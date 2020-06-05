package com.hfad.testprep;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AppDbManager extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "gameScores.db";
    private static final String TABLENAME = "SCORES";

    //ContentValues scoreValues;

    public AppDbManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLENAME+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT, score INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static void insertScore(SQLiteDatabase db, String name, int score){
        ContentValues scoreValues = new ContentValues();
        scoreValues.put("name", name);
        scoreValues.put("score", score);
        db.insert(TABLENAME, null,scoreValues);
    }

    public void resetScoresLeaderBoard(SQLiteDatabase db){
        db.execSQL(String.format("DELETE FROM %s;", TABLENAME));
    }

    public String getScoreTableName(){
        return TABLENAME;
    }
}
