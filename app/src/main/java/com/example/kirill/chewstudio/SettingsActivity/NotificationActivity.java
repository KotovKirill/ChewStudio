package com.example.kirill.chewstudio.SettingsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.example.kirill.chewstudio.R;

public class NotificationActivity extends AppCompatActivity {
    public static final String NOTIFICATIONS_PREFERENCES = "notifications";
    public static final String NOTIFICATIONS_PREFERENCES_TEXT = "textnotifications";
    public static final String NOTIFICATIONS_PREFERENCES_SOUND = "soundnotifications";
    public static final String NOTIFICATIONS_PREFERENCES_RG_LEARNING = "rgnotifications";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CheckBox checkBoxText;
    private CheckBox checkBoxSound;
    private RadioGroup radioGroupLearning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initComponents();
    }

    private void initComponents() {
        preferences = this.getSharedPreferences(NOTIFICATIONS_PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
        initToolbar();
        initButtons();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initButtons() {
        this.checkBoxText = (CheckBox) this.findViewById(R.id.content_settings_checkbox_text);
        this.checkBoxText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(NOTIFICATIONS_PREFERENCES_TEXT, checkBoxText.isChecked());
                editor.apply();
            }
        });
        this.checkBoxSound = (CheckBox) this.findViewById(R.id.content_settings_checkbox_sound);
        this.checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(NOTIFICATIONS_PREFERENCES_SOUND, checkBoxSound.isChecked());
                editor.apply();
            }
        });
        this.radioGroupLearning =
                (RadioGroup) this.findViewById(R.id.content_settings_radio_group_learning);
        this.radioGroupLearning.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editor.putInt(NOTIFICATIONS_PREFERENCES_RG_LEARNING, checkedId);
                editor.apply();
            }
        });

        if(preferences.contains(NOTIFICATIONS_PREFERENCES_TEXT))
            this.checkBoxText.setChecked(preferences
                    .getBoolean(NOTIFICATIONS_PREFERENCES_TEXT, false));
        if(preferences.contains(NOTIFICATIONS_PREFERENCES_SOUND))
            this.checkBoxSound.setChecked(preferences
                    .getBoolean(NOTIFICATIONS_PREFERENCES_SOUND, false));
        if(preferences.contains(NOTIFICATIONS_PREFERENCES_RG_LEARNING))
            this.radioGroupLearning.check(
                    preferences.getInt(NOTIFICATIONS_PREFERENCES_RG_LEARNING, 0));
    }
}
