package com.example.kirill.chewstudio.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.GadgetActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void buttonsListener(View view) {
        int id = view.getId();
        Class aClass = null;
        switch (id){
            case R.id.content_settings_button_edit_information:
                aClass = EditAccountSettingsActivity.class;
                break;
            case R.id.content_settings_button_gadget:
                aClass = GadgetActivity.class;
                break;
            case R.id.content_settings_button_learning:
                aClass = LearningActivity.class;
                break;
            case R.id.content_settings_button_notifications:
                aClass = NotificationActivity.class;
                break;
            case R.id.content_settings_button_about_us:
                aClass = AboutUsActivity.class;
                break;
        }
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }
}
