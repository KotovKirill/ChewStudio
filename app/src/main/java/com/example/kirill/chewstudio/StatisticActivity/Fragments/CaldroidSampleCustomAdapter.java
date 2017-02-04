package com.example.kirill.chewstudio.StatisticActivity.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
	private HashMap<DateTime, Integer> calories;
	public CaldroidSampleCustomAdapter(Context context, int month, int year,
									   Map<String, Object> caldroidData,
									   Map<String, Object> extraData,
									   HashMap<DateTime, Integer> calories) {
		super(context, month, year, caldroidData, extraData);
		this.calories = calories;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		if (convertView == null) {
			cellView = inflater.inflate(R.layout.cell_calendar_grid_view_layout, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);

		tv1.setTextColor(Color.BLACK);

		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		if (dateTime.getMonth() != month) {
			tv1.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_darker_gray));
		}

		Integer calorie = calories.get(dateTime);
		if(calorie != null)
			tv2.setText(calorie.toString());

		tv1.setText("" + dateTime.getDay());
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);
		setCustomResources(dateTime, cellView, tv1);

		return cellView;
	}

}
