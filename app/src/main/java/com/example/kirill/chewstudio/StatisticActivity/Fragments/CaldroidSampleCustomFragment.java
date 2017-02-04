package com.example.kirill.chewstudio.StatisticActivity.Fragments;

import android.os.Bundle;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomFragment extends CaldroidFragment {
	public static final String CALORIES = "calories";
	private HashMap<DateTime, Integer> calories;

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		Bundle arguments = getArguments();
		calories = (HashMap<DateTime, Integer>) arguments.getSerializable(CALORIES);

		return new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData, calories);
	}

}
