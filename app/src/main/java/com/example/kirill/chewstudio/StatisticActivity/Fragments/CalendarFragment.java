package com.example.kirill.chewstudio.StatisticActivity.Fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kirill.chewstudio.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CalendarHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import hirondelle.date4j.DateTime;

public class CalendarFragment extends AbstractFragment {
    private View view;
    private CaldroidFragment caldroidFragment;
    private HashMap<DateTime, Integer> calories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar_statistic, container, false);
        initComponents();
        return view;
    }

    private void initComponents() {
        initCalendarView();
    }

    private void initCalendarView() {
        caldroidFragment = new CaldroidSampleCustomFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);

        Calendar calendar = Calendar.getInstance();
        calories = new HashMap<>();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.blue));
        for (int i = 0; i < 30; i++) {
            calories.put(CalendarHelper.convertDateToDateTime(calendar.getTime()), (int) (Math.random() * 200));
            caldroidFragment.setBackgroundDrawableForDate(colorDrawable, calendar.getTime());
            calendar.add(Calendar.DATE, 5);
        }
        args.putSerializable(CaldroidSampleCustomFragment.CALORIES, calories);

        Date blueDate = new Date();
        ColorDrawable colorAccent = new ColorDrawable(getResources().getColor(R.color.colorAccent));
        caldroidFragment.setBackgroundDrawableForDate(colorAccent, blueDate);

        caldroidFragment.setArguments(args);
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
    }

    public static CalendarFragment getInstance(Context context){
        CalendarFragment fragment = new CalendarFragment();
        fragment.setTitle(context.getString(R.string.calendar_fragment_text_title));
        return fragment;
    }
}
