package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity.EatActivity.EatActivity;
import com.example.kirill.chewstudio.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseMenuActivity extends AppCompatActivity {
    public static final String DISH_ARRAY = "dish_array";
    private ListView listView;
    private ArrayList<Dish> dishList;
    private ArrayList<Dish> dishListCopy;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);
        initComponents();
    }

    private void initComponents() {
        initToolbar();
        initFloatingsActionButton();
        initListView();
    }

    private void initFloatingsActionButton() {
        Button fab = (Button) findViewById(R.id.button_start);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseMenuActivity.this, EatActivity.class);
                /*Bundle bundle = new Bundle();
                HashMap<Integer, Dish> map = new HashMap();
                for (int i = 0; i < dishList.size(); i++)
                    map.put(i, dishList.get(i));
                bundle.putSerializable(DISH_ARRAY, map);*/
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initListView() {
        this.dishList = new ArrayList<Dish>(){
            {
                this.add(new Dish(R.drawable.morozh, R.string.list_view_item_morozh, "150г", "232К", R.drawable.ok, R.drawable.morozhen, R.string.list_view_item_morozh_description));
                this.add(new Dish(R.drawable.syr, R.string.list_view_item_syr, "50г", "384К", R.drawable.ok, R.drawable.ssyr, R.string.list_view_item_syr_description));
                this.add(new Dish(R.drawable.vyn, R.string.list_view_item_vyn, "150г", "431К", R.drawable.no, R.drawable.vynn, R.string.list_view_item_vyn_description));
                this.add(new Dish(R.drawable.gamb, R.string.list_view_item_gam, "200г", "232К", R.drawable.ok, R.drawable.gambur, R.string.list_view_item_gamb_description));
                this.add(new Dish(R.drawable.kofe, R.string.list_view_item_kofe, "150г", "323К", R.drawable.no, R.drawable.kofee, R.string.list_view_item_kofe_description));
                this.add(new Dish(R.drawable.free, R.string.list_view_item_kartofel, "100г", "678К", R.drawable.no, R.drawable.fre, R.string.list_view_item_free_description));
                this.add(new Dish(R.drawable.apple, R.string.list_view_item_aple, "50г", "232К", R.drawable.not_yes, R.drawable.aple, R.string.list_view_item_apple_description));
                this.add(new Dish(R.drawable.chay, R.string.list_view_item_tea, "1000г", "1224К", R.drawable.not_yes, R.drawable.chayy, R.string.list_view_item_chay_description));
                this.add(new Dish(R.drawable.kasha, R.string.list_view_item_kash, "256г", "124К", R.drawable.ok, R.drawable.kash, R.string.list_view_item_kasha_description));
            }
        };
        this.dishListCopy = new ArrayList<>();
        for(int i = 0; i < dishList.size(); i++)
            this.dishListCopy.add((Dish) this.dishList.get(i).clone());

        this.listView = (ListView) this.findViewById(R.id.content_choose_menu_list_view_messages);
        mAdapter = new ListViewAdapter(this, R.layout.dish_list_view_layout, this.dishList);
        this.listView.setAdapter(mAdapter);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        final LayoutInflater inflater = LayoutInflater.from(this);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dish_card_layout, null);
                layout.setPadding(20, 20, 20, 20);
                ImageView imageView = (ImageView) layout.findViewById(R.id.dish_card_layout_image);
                TextView textView = (TextView) layout.findViewById(R.id.dish_card_text_view_description);
                Dish dish = (Dish) listView.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseMenuActivity.this);
                imageView.setImageResource(dish.getDescribeDrawable());
                textView.setText(dish.getDescribe());
                //builder.setTitle();
                TextView title = new TextView(ChooseMenuActivity.this);
                title.setText(dish.getName());
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.BLACK);
                title.setPadding(20, 20, 20, 20);
                builder.setCustomTitle(title);

                builder.setView(layout);
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_optimal:
                if(item.isChecked()){
                    item.setChecked(false);
                    Toast.makeText(this, "Алгоритм пока не реализован", Toast.LENGTH_SHORT).show();
                }else{
                    item.setChecked(true);
                    Toast.makeText(this, "Алгоритм пока не реализован", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_custom:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mAdapter.clear();
                mAdapter.addAll(this.dishListCopy);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Список восстановлен", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
