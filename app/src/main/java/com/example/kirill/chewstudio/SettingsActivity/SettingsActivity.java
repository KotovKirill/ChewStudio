package com.example.kirill.chewstudio.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.GadgetActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSendMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComponents();
    }

    private void initComponents() {
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
        this.buttonSendMail = (Button) this.findViewById(R.id.content_settings_button_feedback);
        this.buttonSendMail.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.content_settings_button_feedback:
                buttonClickSendMessage();
                break;
        }
    }

    private void buttonClickSendMessage() {
        ShareCompat.IntentBuilder.from(this)
                .setType("message/rfc822")
                .addEmailTo("example@mail.ru")
                .setSubject(getResources().getString(R.string.app_name))
                .setText(getString(R.string.intent_builder_text_message))
                .setChooserTitle(R.string.intent_builder_text_title)
                .startChooser();
    }
}
