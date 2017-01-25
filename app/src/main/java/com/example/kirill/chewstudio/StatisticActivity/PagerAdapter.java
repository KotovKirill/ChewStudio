package com.example.kirill.chewstudio.StatisticActivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kirill.chewstudio.StatisticActivity.Fragments.AbstractFragment;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.CalendarFragment;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.GraphicFragment;

import java.util.HashMap;
import java.util.Map;


public class PagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private Map<Integer, AbstractFragment> map;
    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.map = new HashMap<>();
        initTabs(context);
    }

    private void initTabs(Context context) {
        this.map.put(0, CalendarFragment.getInstance(context));
        this.map.put(1, GraphicFragment.getInstance(context));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.map.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return this.map.get(position);
    }

    @Override
    public int getCount() {
        return this.map.size();
    }
}
