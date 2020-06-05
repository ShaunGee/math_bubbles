package com.hfad.testprep;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class LeaderBoard extends AppCompatActivity {
    LinearLayout verticalLinearLayout, horizontalLinearLayout;
    SQLiteDatabase db;
    Cursor cursor;
    AppDbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        dbManager = new AppDbManager(this);
        db = dbManager.getReadableDatabase();

        // this query sorts return scores in descending order
        cursor = db.query(dbManager.getScoreTableName(), new String[]{"_id", "name", "score"},
                null, null, "score", null, "score DESC");

        horizontalLinearLayout = findViewById(R.id.leaderBoardNameAndScoreContainer);
        verticalLinearLayout = findViewById(R.id.leaderBoardContainer);

        while (cursor.moveToNext()) { //this code creates the leaderboard programmatically
            LinearLayout column = new LinearLayout(this);
            column.setMinimumWidth(verticalLinearLayout.getWidth());
            column.setOrientation(LinearLayout.HORIZONTAL);
            column.setPadding(0, 10, 0, 10);

            for (int c = 1; c < 3; c++) { //if it was 0 it would return the player _id in the db
                //linearLayout.addView();
                TextView t = new TextView(this);
                t.setText(String.valueOf(cursor.getString(c)));
                t.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                t.setBackgroundColor(Color.YELLOW);
                t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                column.addView(t);

            }
            verticalLinearLayout.addView(column);
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
}