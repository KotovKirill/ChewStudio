package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirill.chewstudio.R;

import java.util.List;


public class ListViewAdapter extends ArrayAdapter<Dish> {
    public ListViewAdapter(Context context, int resource, List<Dish> objects) {
        super(context, resource, objects);
    }

    static class ViewHolder{
        private ImageView Drawable;
        private ImageView drawableAvatar;
        private TextView Name;
        private TextView dishWeigth;
        private TextView dishEnergy;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if(row == null){
            viewHolder = new ViewHolder();
            row = LayoutInflater.from(getContext()).inflate(R.layout.dish_list_view_layout, parent, false);
            viewHolder.Drawable = (ImageView) row.findViewById(R.id.drawable_id);
            viewHolder.drawableAvatar = (ImageView) row.findViewById(R.id.drawable_avatar_id);
            viewHolder.Name = (TextView) row.findViewById(R.id.Name);
            viewHolder.dishWeigth = (TextView) row.findViewById(R.id.dish_weight);
            viewHolder.dishEnergy = (TextView) row.findViewById(R.id.dish_energy);
            row.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) row.getTag();
        viewHolder.Drawable.setImageResource(this.getItem(position).getDrawable());
        viewHolder.drawableAvatar.setImageResource(this.getItem(position).getDrawableAvatar());
        viewHolder.Name.setText(this.getItem(position).getName());
        viewHolder.dishWeigth.setText(this.getItem(position).getWeight());
        viewHolder.dishEnergy.setText(this.getItem(position).getEnergy());
        return row;
    }
}
