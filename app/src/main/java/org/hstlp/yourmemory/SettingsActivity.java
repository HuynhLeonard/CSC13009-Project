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
        sharePrf = getSharedPreferences("AppPreferences", MODE_PRIVATE);    // get the value here
        edit = sharePrf.edit();
        // get dark mode status
        boolean status = sharePrf.getBoolean("darkmode", false);
        if (status) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);

        // content for setting fragment here
    }
}
