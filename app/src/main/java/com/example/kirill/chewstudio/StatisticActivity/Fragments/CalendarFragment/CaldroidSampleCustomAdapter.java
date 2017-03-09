package com.example.kirill.chewstudio.StatisticActivity.Fragments.CalendarFragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.StatisticActivity.Fragments.Food;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
	private HashMap<DateTime, Food> food;
	public CaldroidSampleCustomAdapter(Context context, int month, int year,
									   Map<String, Object> caldroidData,
									   Map<String, Object> extraData,
									   HashMap<DateTime, Food> food) {
		super(context, month, year, caldroidData, extraData);
		this.food = food;
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

		Food food = this.food.get(dateTime);
		if(food != null){
			int[] calories = food.getCalories();
			int calorie = 0;
			for (int calory : calories)
				calorie += calory;
			tv2.setText(String.valueOf((calorie)));
		}

		tv1.setText("" + dateTime.getDay());
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);
		setCustomResources(dateTime, cellView, tv1);

		return cellView;
	}

}
