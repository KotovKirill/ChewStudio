package com.example.kirill.chewstudio.StatisticActivity.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.decorators.EventDecorator;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.decorators.HighlightWeekendsDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarFragment extends AbstractFragment {
    private View view;
    private TextView textViewDate;
    private TextView textViewCalories;
    private MaterialCalendarView calendarView;
    private HashMap<CalendarDay, Integer> calories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar_statistic, container, false);
        initComponents();
        return view;
    }

    private void initComponents() {
        initTextView();
        initCalendarView();
    }

    private void initTextView() {
        textViewCalories = (TextView) view.findViewById(R.id.fragment_calendar_text_view_kalories);
        textViewDate = (TextView) view.findViewById(R.id.fragment_calendar_text_view_date);
    }

    private void initCalendarView() {
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendar_view);
        ArrayList<CalendarDay> calendarDays = new ArrayList<>();
        calories = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 30; i++) {
            CalendarDay day = CalendarDay.from(calendar);
            calendarDays.add(day);
            calories.put(day, (int) (Math.random() * 200));
            calendar.add(Calendar.DATE, 5);
        }
        calendarView.setSelectedDate(Calendar.getInstance());
        dateSetter();
        calendarView.addDecorators(new HighlightWeekendsDecorator(), new EventDecorator(Color.RED, calendarDays));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateSetter();
            }
        });
    }

    private void dateSetter() {
        textViewCalories.setText("");
        textViewDate.setText(dateFormatter());
        Integer integer = calories.get(calendarView.getSelectedDate());
        if(integer != null)
            textViewCalories.setText(integer +  "K");
    }

    public String dateFormatter(){
        DateFormat instance = SimpleDateFormat.getDateInstance();
        CalendarDay selectedDate = calendarView.getSelectedDate();
        if(selectedDate == null)
            return "Не выбрано";
        return instance.format(selectedDate.getDate());
    }

    public static CalendarFragment getInstance(Context context){
        CalendarFragment fragment = new CalendarFragment();
        fragment.setTitle(context.getString(R.string.calendar_fragment_text_title));
        return fragment;
    }
}
