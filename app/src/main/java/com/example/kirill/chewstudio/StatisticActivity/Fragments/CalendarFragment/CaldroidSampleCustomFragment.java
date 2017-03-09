package com.example.kirill.chewstudio.StatisticActivity.Fragments.CalendarFragment;

import android.os.Bundle;
import android.util.Log;

import com.example.kirill.chewstudio.StatisticActivity.Fragments.Food;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomFragment extends CaldroidFragment {
	public static final String FOOD = "calories";
	private HashMap<DateTime, Food> food;

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		Bundle arguments = getArguments();
		food = (HashMap<DateTime, Food>) arguments.getSerializable(FOOD);
		return new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData, food);
	}

}
