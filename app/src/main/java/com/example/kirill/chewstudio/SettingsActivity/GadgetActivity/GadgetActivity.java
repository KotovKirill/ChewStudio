package com.example.kirill.chewstudio.SettingsActivity.GadgetActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;

public class GadgetActivity extends AppCompatActivity {
    public static String BLUETOOTH_PREFERENCES = "bluetooth";
    public static String PREFERENCE_DEVICE_ADDRESS = "address";
    public static String PREFERENCE_DEVICE_NAME = "name";
    private TextView textView;

    private TextView textViewMac;
    private TextView textViewName;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget);
        initComponents();
    }

    private void initComponents() {
        initToolbar();
        preferences = getSharedPreferences(BLUETOOTH_PREFERENCES, Context.MODE_PRIVATE);
        textViewName = (TextView) this.findViewById(R.id.content_settings_text_name_device);
        textViewMac = (TextView) this.findViewById(R.id.content_settings_text_mac_device);

        textView = (TextView) this.findViewById(R.id.content_settings_change_gadget);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GadgetActivity.this, ChangeGadgetActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(preferences.contains(PREFERENCE_DEVICE_NAME) && preferences.contains(PREFERENCE_DEVICE_ADDRESS)){
            textViewName.setText(preferences.getString(PREFERENCE_DEVICE_NAME, ""));
            textViewMac.setText("mac: " + preferences.getString(PREFERENCE_DEVICE_ADDRESS, ""));
        }
    }
}
