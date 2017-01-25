package com.example.kirill.chewstudio.FoodActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity.ChooseMenuActivity;
import com.example.kirill.chewstudio.R;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        initComponents();
    }

    private void initComponents() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void buttonsListener(View view) {
        int id = view.getId();
        if(id == R.id.content_food_button_self_menu){
            Intent intent = new Intent(this, ChooseMenuActivity.class);
            startActivity(intent);
        }
    }
}
