package com.example.kirill.chewstudio.SettingsActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.GadgetActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSendMail;
    private Button buttonExit;
    private TextView textViewUserName;
    private TextView textViewUserSecName;
    private SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME)
                && mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME)) {
            this.textViewUserName.setText(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME, "Не выбрано"));
            this.textViewUserSecName.setText(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME, "Не выбрано"));
        }
    }

    private void initComponents() {
        this.mSettings = getSharedPreferences(EditAccountSettingsActivity.ACCOUNT_PREFERENCES,
                Context.MODE_PRIVATE);
        initToolbar();
        initButtons();
        this.textViewUserName = (TextView) this.findViewById(R.id.content_settings_edit_text_user_name);
        this.textViewUserSecName = (TextView) this.findViewById(R.id.content_settings_edit_text_user_secname);
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

        this.buttonExit = (Button) this.findViewById(R.id.button_exit);
        this.buttonExit.setOnClickListener(this);
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
