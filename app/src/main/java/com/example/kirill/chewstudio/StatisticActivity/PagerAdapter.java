package com.example.kirill.chewstudio.StatisticActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kirill.chewstudio.StatisticActivity.Fragments.AbstractFragment;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.CalendarFragment.CaldroidSampleCustomFragment;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.CalendarFragment.CalendarFragment;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.Food;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.GraphicFragment;
import com.roomorama.caldroid.CalendarHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;


public class PagerAdapter extends FragmentPagerAdapter {
    private final int n = 12;
    private Context context;
    private Map<Integer, AbstractFragment> map;
    private HashMap<DateTime, Food> food;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.map = new HashMap<>();
        initTabs(context);
    }

    private void initTabs(Context context) {
        Calendar calendar = Calendar.getInstance();
        food = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            int[] calories = new int[n];
            int[] proteins = new int[n];
            int[] fats = new int[n];
            int[] carbohydrates = new int[n];
            for (int j = 0; j < n; j++) {
                calories[j] = (int) (58 + Math.random() * (66-58));
                proteins[j] = (int) (35 + Math.random() * (35 - 32));
                fats[j] = (int) (18 + Math.random() * (18 - 5));
                carbohydrates[j] = (int) (10 + Math.random() * (10 - 5));
            }
            food.put(CalendarHelper.convertDateToDateTime(calendar.getTime()),
                    new Food(calories, proteins, fats, carbohydrates));
            calendar.add(Calendar.DATE, 4);
        }

        CalendarFragment clnd = CalendarFragment.getInstance(context);
        GraphicFragment grph = GraphicFragment.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putSerializable(CaldroidSampleCustomFragment.FOOD, food);

        clnd.setArguments(bundle);
        grph.setArguments(bundle);

        this.map.put(0, clnd);
        this.map.put(1, grph);
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
