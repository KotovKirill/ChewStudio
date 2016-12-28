package com.example.kirill.chewstudio.SettingsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.chewstudio.R;

public class LearningActivity extends AppCompatActivity {
    public static final String LEARNING_PREFERENCES = "learning";
    public static final String LEARNING_PREFERENCES_MODE_L = "learningmode";
    public static final String LEARNING_PREFERENCES_MODE_L_TEXT = "learningmodetext";

    private SharedPreferences preferences;
    private CheckBox checkBoxLearningMode;
    private SeekBar seekBarCount;
    private TextView textViewStart_m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        initComponents();
    }

    private void initComponents() {
        this.preferences = this.getSharedPreferences(LEARNING_PREFERENCES, Context.MODE_PRIVATE);
        initToolbar();
        initSeekBar();
        initButtons();
        initFloatingActionButton();
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
        this.checkBoxLearningMode =
                (CheckBox) this.findViewById(R.id.content_settings_checkbox_learning_mode);
        if(preferences.contains(LEARNING_PREFERENCES_MODE_L))
            this.checkBoxLearningMode.setChecked(preferences
                    .getBoolean(LEARNING_PREFERENCES_MODE_L, false));
        if(preferences.contains(LEARNING_PREFERENCES_MODE_L_TEXT)) {
            this.textViewStart_m.setText(preferences
                    .getString(LEARNING_PREFERENCES_MODE_L_TEXT, ""));
            this.seekBarCount.setProgress(Integer.valueOf(this.textViewStart_m.getText().toString()));
        }

        if(!checkBoxLearningMode.isChecked()) {
            seekBarCount.setEnabled(false);
            textViewStart_m.setActivated(false);
        }
        checkBoxLearningMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    seekBarCount.setEnabled(true);
                    textViewStart_m.setEnabled(true);
                }
                else{
                    seekBarCount.setEnabled(false);
                    textViewStart_m.setEnabled(false);
                }
            }
        });
    }

    private void initSeekBar() {
        textViewStart_m = (TextView) this.findViewById(R.id.startText);
        this.seekBarCount = (SeekBar) this.findViewById(R.id.seekBar_count);
        this.seekBarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewStart_m.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(LEARNING_PREFERENCES_MODE_L, checkBoxLearningMode.isChecked());
                editor.putString(LEARNING_PREFERENCES_MODE_L_TEXT, textViewStart_m.getText().toString());
                editor.apply();
                Toast.makeText(LearningActivity.this, getResources()
                        .getString(R.string.content_settings_text_save_settings), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
