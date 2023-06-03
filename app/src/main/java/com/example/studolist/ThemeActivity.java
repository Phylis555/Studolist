package com.example.studolist;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ThemeActivity extends AppCompatActivity {
    private TextView mode_text;
    private SwitchCompat aSwitch;
    boolean nightMode;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NIGHT = "night";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
    {
        setTheme(R.style.DarkTheme);
        nightMode = true;
    } else{
        setTheme(R.style.AppTheme);
        nightMode = false;
    }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        aSwitch = findViewById(R.id.mode);
        mode_text = findViewById(R.id.mode_text);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nightMode = sharedPreferences.getBoolean(NIGHT, false);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean(NIGHT,true);
                    mode_text.setText("Dark mode");
                } else {
                    AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO));
                    editor.putBoolean(NIGHT,false);
                    mode_text.setText("Light mode");

                }
                    editor.apply();
            }
        });
        if(nightMode)
        {
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }

    }

}