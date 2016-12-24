package com.example.kirill.chewstudio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.chewstudio.SettingsActivity.AboutUsActivity;
import com.example.kirill.chewstudio.SettingsActivity.EditAccountSettingsActivity;
import com.example.kirill.chewstudio.SettingsActivity.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences mSettings;
    private TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.textViewUserName = (TextView) this.findViewById(R.id.content_main_text_view_user);
        if(mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME)
                && mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME))
            this.textViewUserName.setText("Пользователь: "
                    + mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME, "")
                    + " " + mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME, ""));
    }

    private void initComponents() {
        this.mSettings = getSharedPreferences(EditAccountSettingsActivity.ACCOUNT_PREFERENCES,
                Context.MODE_PRIVATE);
        initToolbar();
        initNavigationView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar,
                R.string.view_navigation_open, R.string.view_navigation_close);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        this.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.menu_navigation_write_author:
                        buttonClickSendMessage();
                        break;
                    case R.id.menu_navigation_about:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        break;
                    case R.id.menu_navigation_help:
                        startActivity(new Intent(MainActivity.this, InformationActivity.class));
                        break;
                }
                return true;
            }
        });

        NavigationMenuView menuView = (NavigationMenuView) this.navigationView.getChildAt(0);
        if(menuView != null)
            menuView.setVerticalScrollBarEnabled(false);
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
