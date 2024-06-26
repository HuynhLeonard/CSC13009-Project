package org.hstlp.yourmemory;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends Activity{
    SharedPreferences sharePrf;  // using shared reference for storing the setting components (isDark,...)
    SharedPreferences.Editor edit;

    SwitchCompat changeDark;
    Button reAnalyse;
    Button analyse;

    public SettingsActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharePrf = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        edit = sharePrf.edit();
        boolean status = sharePrf.getBoolean("darkmode", false);
        if (status) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
        reAnalyse = findViewById(R.id.reanalyze);
        analyse = findViewById(R.id.analyze);
        changeDark = findViewById(R.id.switchDarkmode);

        changeDark.setChecked(status);
        changeDark.setOnCheckedChangeListener((compoundButton, isDark) -> {
            MainActivity.mainActivity.setIsDark(isDark);
            recreate();
        });
        reAnalyse.setOnClickListener(view -> {
            MainActivity.mainActivity.purgeDatabase();
            MainActivity.mainActivity.analyse();
            Toast.makeText(this, "Reanalysing...", Toast.LENGTH_SHORT).show();
        });
        analyse.setOnClickListener(view -> {
            MainActivity.mainActivity.analyse();
            Toast.makeText(this, "Analysing...", Toast.LENGTH_SHORT).show();
        });
    }
}
