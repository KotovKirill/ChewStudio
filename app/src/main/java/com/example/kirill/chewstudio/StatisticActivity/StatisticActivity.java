package com.example.kirill.chewstudio.StatisticActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.kirill.chewstudio.InformationActivity;
import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.SettingsActivity.AboutUsActivity;

public class StatisticActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        initComponents();
    }

    private void initComponents() {
        initToolbar();
        initNavigationView();
        initTabs();
    }

    private void initTabs() {
        this.viewPager = (ViewPager) this.findViewById(R.id.view_pager_statistic);
        this.viewPager.setAdapter(new PagerAdapter(this, getSupportFragmentManager()));
        viewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(this.viewPager);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigationView() {
        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayout,
                this.toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
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
                        startActivity(new Intent(StatisticActivity.this, AboutUsActivity.class));
                        break;
                    case R.id.menu_navigation_help:
                        startActivity(new Intent(StatisticActivity.this, InformationActivity.class));
                        break;
                }
                return true;
            }
        });

        NavigationMenuView menuView = (NavigationMenuView) this.navigationView.getChildAt(0);
        if(menuView != null)
            menuView.setVerticalScrollBarEnabled(false);
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
