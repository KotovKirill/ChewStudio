package com.example.kirill.chewstudio.StatisticActivity.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.CalendarFragment.CaldroidSampleCustomFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;
import com.roomorama.caldroid.CalendarHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class GraphicFragment extends AbstractFragment implements CompoundButton.OnCheckedChangeListener{
    public static final String PREFERENCES_GRAPHIC = "graphic";
    public static final String PREFERENCES_GRAPHIC_CHECKBOX_CALORIES = "calories";
    public static final String PREFERENCES_GRAPHIC_CHECKBOX_PROTEINS = "proteins";
    public static final String PREFERENCES_GRAPHIC_CHECKBOX_FATS = "fats";
    public static final String PREFERENCES_GRAPHIC_CHECKBOX_CARBOHIDRATS = "carbohidrats";

    private View view;
    private HashMap<DateTime, Food> food;

    private SharedPreferences preferences;
    private CheckBox checkBoxCalories;
    private CheckBox checkBoxProteins;
    private CheckBox checkBoxFats;
    private CheckBox checkBoxCarbohydrates;
    private TextView textViewChangeDate;

    private LineChart lineChart;
    private int[] caloriesToday;
    private int[] proteinsToday;
    private int[] fatsToday;

    private int[] carbohydratesToday;
    private int[] calories;
    private int[] proteins;
    private int[] fats;
    private int[] carbohydrates;
    private ArrayList<String> labels;
    private ArrayList<String> labelsToday;
    private int dayLast;
    private int monthLast;
    private int yearLast;
    private Calendar calendarForArrow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragmet_graphic_statistic, container, false);
        this.initComponents();
        return view;
    }

    private void initComponents() {
        this.preferences = getActivity().getSharedPreferences(PREFERENCES_GRAPHIC, Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        this.dayLast = calendar.get(Calendar.DATE);
        this.monthLast = calendar.get(Calendar.MONTH) + 1;
        this.yearLast = calendar.get(Calendar.YEAR);
        calendarForArrow = Calendar.getInstance();
        calendarForArrow.set(yearLast, monthLast - 1, dayLast);
        food = (HashMap<DateTime, Food>) getArguments().getSerializable(CaldroidSampleCustomFragment.FOOD);
        initCheckBoxes();
        initGraphic();
        initTextViewChangeDate();
        initArrowButtons();
    }

    private void initGraphic() {
        lineChart = (LineChart) view.findViewById(R.id.chart);
        /*layout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(getContext(), "dsdsd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(getContext(), "dsdsd", Toast.LENGTH_SHORT).show();
            }
        });*/
        Food food = this.food.get(CalendarHelper.convertDateToDateTime(new Date()));
        calories = food.getCalories();
        proteins = food.getProteins();
        fats = food.getFats();
        carbohydrates = food.getCarbohydrates();

        caloriesToday = calories;
        proteinsToday = proteins;
        fatsToday = fats;
        carbohydratesToday = carbohydrates;

        labels = new ArrayList<>();
        for (int i = 0; i < calories.length; i++)
            labels.add(String.valueOf(i + 1));
        labelsToday = new ArrayList<>(labels);

        initLineData(calories, proteins, fats, carbohydrates, labels);

        lineChart.setDescription("Питание");
        Legend legend = lineChart.getLegend();
        legend.setTextSize(15);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(100f);
        legend.setFormSize(15f);

        lineChart.animateY(500);
        lineChart.setNoDataText("Данные отсутствуют");//
    }

    private void initLineData(int[] calories, int[] proteins, int[] fats, int[] carbohydrates, ArrayList<String> labels) {
        if(calories == null || proteins == null
                || fats == null || carbohydrates == null){
            lineChart.clear();
            return;
        }
        ArrayList<Entry> entriesColories = new ArrayList<>();
        ArrayList<Entry> entriesProteins = new ArrayList<>();
        ArrayList<Entry> entriesFats = new ArrayList<>();
        ArrayList<Entry> entriesCarb = new ArrayList<>();
        for(int i = 0; i < calories.length; i++) {
            entriesColories.add(new Entry(calories[i], i));
            entriesProteins.add(new Entry(proteins[i], i));
            entriesFats.add(new Entry(fats[i], i));
            entriesCarb.add(new Entry(carbohydrates[i], i));
        }

        final LineDataSet datasetColories = new LineDataSet(entriesColories, " - калории за день");
        final LineDataSet datasetProteins = new LineDataSet(entriesProteins, " - белки за день");
        final LineDataSet datasetFats = new LineDataSet(entriesFats, " - жиры за день");
        final LineDataSet datasetCarb = new LineDataSet(entriesCarb, " - углеводы за день");

        customDataSets(datasetColories, getContext().getResources().getColor(R.color.colorAccent));
        customDataSets(datasetProteins, getContext().getResources().getColor(android.R.color.holo_green_dark));
        customDataSets(datasetFats, getContext().getResources().getColor(android.R.color.holo_orange_dark));
        customDataSets(datasetCarb, getContext().getResources().getColor(android.R.color.darker_gray));

        LineData data = new LineData(labels, new LinkedList<LineDataSet>(){
            {
                if(checkBoxCalories.isChecked())
                    add(datasetColories);
            }
            {
                if (checkBoxProteins.isChecked())
                    add(datasetProteins);
            }
            {
                if (checkBoxFats.isChecked())
                    add(datasetFats);
            }
            {
                if (checkBoxCarbohydrates.isChecked())
                    add(datasetCarb);
            }
        });
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private void initCheckBoxes() {
        this.checkBoxCalories = (CheckBox) view.findViewById(R.id.fragment_graphic_statistic_checkbox_show_calories);
        this.checkBoxProteins = (CheckBox) view.findViewById(R.id.fragment_graphic_statistic_checkbox_show_proteins);
        this.checkBoxFats = (CheckBox) view.findViewById(R.id.fragment_graphic_statistic_checkbox_show_fats);
        this.checkBoxCarbohydrates = (CheckBox) view.findViewById(R.id.fragment_graphic_statistic_checkbox_show_carbohydrates);

        this.checkBoxCalories.setChecked(preferences.getBoolean(PREFERENCES_GRAPHIC_CHECKBOX_CALORIES, true));
        this.checkBoxProteins.setChecked(preferences.getBoolean(PREFERENCES_GRAPHIC_CHECKBOX_PROTEINS, true));
        this.checkBoxFats.setChecked(preferences.getBoolean(PREFERENCES_GRAPHIC_CHECKBOX_FATS, true));
        this.checkBoxCarbohydrates.setChecked(preferences.getBoolean(PREFERENCES_GRAPHIC_CHECKBOX_CARBOHIDRATS, true));

        this.checkBoxCalories.setOnCheckedChangeListener(this);
        this.checkBoxProteins.setOnCheckedChangeListener(this);
        this.checkBoxFats.setOnCheckedChangeListener(this);
        this.checkBoxCarbohydrates.setOnCheckedChangeListener(this);
    }

    private void customDataSets(LineDataSet dataset, int color) {
        dataset.setColors(ColorTemplate.LIBERTY_COLORS);
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        dataset.setCircleColor(color);
        dataset.setColor(color);
    }

    private void initTextViewChangeDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String str = format.format(new Date());
        this.textViewChangeDate = (TextView) view.findViewById(R.id.fragment_graphic_statistic_text_view_change_date);
        this.textViewChangeDate.setText(
                view.getResources().getString(R.string.fragment_graphic_statistic_text_view_change_date) + " " + str);
        this.textViewChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                        SmoothDateRangePickerFragment
                                .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                                    @Override
                                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                                               int yearStart, int monthStart,
                                                               int dayStart, int yearEnd,
                                                               int monthEnd, int dayEnd) {
                                        String date;
                                        if(yearStart != yearEnd || monthStart != monthEnd || dayStart != dayEnd)
                                            date = view.getResources().getString(
                                                    R.string.fragment_graphic_statistic_text_view_change_date) + " "
                                                    + dayStart + "." + (++monthStart)
                                                    + "." +  yearStart + " - " + dayEnd + "."
                                                    + (++monthEnd) + "." + yearEnd;
                                        else{
                                            date = view.getResources().getString(
                                                    R.string.fragment_graphic_statistic_text_view_change_date) + " "
                                                    + dayStart + "." + (++monthStart)
                                                    + "." +  yearStart;
                                            ++monthEnd;
                                        }
                                        dayLast = dayEnd;
                                        monthLast = monthEnd;
                                        yearLast = yearEnd;
                                        textViewChangeDate.setText(date);
                                        calendarForArrow.set(yearLast, monthLast - 1, dayLast);
                                        DateTime dateStart = DateTime.forDateOnly(yearStart, monthStart, dayStart);
                                        DateTime dateEnd = DateTime.forDateOnly(yearEnd, monthEnd, dayEnd);

                                        initGraphicRange(dateStart, dateEnd);

                                    }
                                });
                smoothDateRangePickerFragment.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    public int daysBetween(DateTime d1, DateTime d2){
        return (int)( (d2.getMilliseconds(TimeZone.getDefault()) - d1.getMilliseconds(TimeZone.getDefault())) / (1000 * 60 * 60 * 24));
    }

    private void initGraphicRange(DateTime dateStart, DateTime dateEnd) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CalendarHelper.convertDateTimeToDate(dateStart));
        int count = daysBetween(dateStart, dateEnd);
        int day = dateStart.getDay();
        int month = dateStart.getMonth();
        int year = dateStart.getYear();
        DateTime today = CalendarHelper.convertDateToDateTime(new Date());
        int day1 = today.getDay();
        int month1 = today.getMonth();
        int year1 = today.getYear();
        if(count == 0 ) {
            if (day == day1 && month == month1 && year == year1) {
                calories = caloriesToday;
                proteins = proteinsToday;
                fats = fatsToday;
                carbohydrates = carbohydratesToday;
                labels = new ArrayList<>(labelsToday);
                initLineData(caloriesToday, proteinsToday, fatsToday, carbohydratesToday, labelsToday);
                return;
            }
            else {
                Food food = this.food.get(CalendarHelper.convertDateToDateTime(calendar.getTime()));
                if(food != null){

                }
                else {
                    calories = null;
                    proteins = null;
                    fats = null;
                    carbohydrates = null;
                    labels = new ArrayList<>();
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                    return;
                }
            }
        }
        LinkedList<Integer> caloriesLst = new LinkedList<>();
        LinkedList<Integer> proteinsLst = new LinkedList<>();
        LinkedList<Integer> fatsLst = new LinkedList<>();
        LinkedList<Integer> carbohydratesLst = new LinkedList<>();
        labels.clear();
        for (int i = 0; i <= count; i++) {
            Food food = this.food.get(CalendarHelper.convertDateToDateTime(calendar.getTime()));
            if(food != null){
                int[] foodCalories = food.getCalories();
                int[] foodProteins = food.getProteins();
                int[] foodFats = food.getFats();
                int[] foodCarbohydrates = food.getCarbohydrates();
                int countCalories = 0;
                int countProteins = 0;
                int countFats = 0;
                int countCarbohydrates = 0;
                for (int j = 0; j < foodCalories.length; j++) {
                    countCalories += foodCalories[j];
                    countProteins += foodProteins[j];
                    countFats += foodFats[j];
                    countCarbohydrates += foodCarbohydrates[j];
                }
                caloriesLst.add(countCalories);
                proteinsLst.add(countProteins);
                fatsLst.add(countFats);
                carbohydratesLst.add(countCarbohydrates);
                labels.add(format.format(calendar.getTime()));
            }
            calendar.add(Calendar.DATE, 1);
        }

        calories = listToArray(caloriesLst);
        proteins = listToArray(proteinsLst);
        fats = listToArray(fatsLst);
        carbohydrates = listToArray(carbohydratesLst);
        initLineData(calories, proteins, fats, carbohydrates, labels);
    }

    private int[] listToArray(LinkedList<Integer> list){
        int[] mass = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            mass[i] = list.get(i);
        return mass;
    }

    private void initArrowButtons() {
        ImageButton buttonArrowLeft = (ImageButton) view.findViewById(R.id.fragment_graphic_statistic_button_arrow_left);
        ImageButton buttonArrowRight = (ImageButton) view.findViewById(R.id.fragment_graphic_statistic_button_arrow_right);

        buttonArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarForArrow.add(Calendar.DATE, -1);
                buttonsArrowHandler(calendarForArrow);
            }
        });
        buttonArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarForArrow.add(Calendar.DATE, 1);
                buttonsArrowHandler(calendarForArrow);
            }
        });
    }

    private void buttonsArrowHandler(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String str = format.format(calendar.getTime());
        this.textViewChangeDate.setText(
                view.getResources().getString(R.string.fragment_graphic_statistic_text_view_change_date) + " " + str);

        Food food = GraphicFragment.this.food.get(CalendarHelper.convertDateToDateTime(calendar.getTime()));
        if(food != null) {
            calories = food.getCalories();
            proteins = food.getProteins();
            fats = food.getFats();
            carbohydrates = food.getCarbohydrates();
            labels = new ArrayList<>();
            for (int i = 0; i < calories.length; i++)
                labels.add(String.valueOf(i + 1));
            initLineData(calories, proteins, fats, carbohydrates, labels);
            lineChart.animateX(300);
        }
        else {
            calories = null;
            proteins = null;
            fats = null;
            carbohydrates = null;
            labels = new ArrayList<>();
            initLineData(calories, proteins, fats, carbohydrates, labels);
        }
    }

    public static GraphicFragment getInstance(Context context){
        GraphicFragment fragment = new GraphicFragment();
        fragment.setTitle(context.getString(R.string.graphic_fragment_text_title));
        return fragment;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        SharedPreferences.Editor edit = preferences.edit();
        switch (id){
            case R.id.fragment_graphic_statistic_checkbox_show_calories:
                if(!isChecked){
                    checkBoxCalories.setChecked(false);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_CALORIES, false);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                else{
                    checkBoxCalories.setChecked(true);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_CALORIES, true);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                break;
            case R.id.fragment_graphic_statistic_checkbox_show_proteins:
                if(!isChecked){
                    checkBoxProteins.setChecked(false);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_PROTEINS, false);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                else{
                    checkBoxProteins.setChecked(true);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_PROTEINS, true);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                break;
            case R.id.fragment_graphic_statistic_checkbox_show_fats:
                if(!isChecked){
                    checkBoxFats.setChecked(false);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_FATS, false);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                else{
                    checkBoxFats.setChecked(true);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_FATS, true);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                break;
            case R.id.fragment_graphic_statistic_checkbox_show_carbohydrates:
                if(!isChecked){
                    checkBoxCarbohydrates.setChecked(false);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_CARBOHIDRATS, false);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                else{
                    checkBoxCarbohydrates.setChecked(true);
                    edit.putBoolean(PREFERENCES_GRAPHIC_CHECKBOX_CARBOHIDRATS, true);
                    initLineData(calories, proteins, fats, carbohydrates, labels);
                }
                break;
        }

        edit.apply();
    }
}
