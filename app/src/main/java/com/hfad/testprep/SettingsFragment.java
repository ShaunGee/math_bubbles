package com.hfad.testprep;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences_screen);

        ListPreference difficulty = findPreference("difficulty");
        Preference resetLeaderBoard = findPreference("clearLeaderBoard");
        String d = difficulty.getValue();
        resetLeaderBoard.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                resetLeaderBoard();
                return false;
            }
        });
    }

    public void resetLeaderBoard() {
        AppDbManager dbManager = new AppDbManager(getActivity());
        SQLiteDatabase db = dbManager.getReadableDatabase();
        dbManager.resetScoresLeaderBoard(db);
        db.close();

        Toast toast = Toast.makeText(getActivity(), "Leader Board Cleared", Toast.LENGTH_LONG);
        toast.show();
    }
}