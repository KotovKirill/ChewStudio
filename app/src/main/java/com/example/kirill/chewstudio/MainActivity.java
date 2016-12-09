package com.example.kirill.chewstudio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kirill.chewstudio.SettingsActivity.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mainButtonsListener(View view) {
        int id = view.getId();
        Class aClass = null;
        switch (id){
            case R.id.content_main_button_settings:
                aClass = SettingsActivity.class;
                break;
            case R.id.content_main_button_information:
                aClass = InformationActivity.class;
                break;
            case R.id.content_main_button_statistic:
                aClass = StatisticActivity.class;
                break;
            case R.id.content_main_button_food:
                aClass = FoodActivity.class;
                break;
        }
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }
}
