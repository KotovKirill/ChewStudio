package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kirill.chewstudio.R;

import java.util.LinkedList;
import java.util.List;

public class ChooseMenuActivity extends AppCompatActivity {
    private ListView listView;
    private List<Dish> dishList;
    private List<Dish> dishListCopy;
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
            public void onClick(View view) {
                Snackbar.make(view, "Начинаем кушать", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        this.dishList = new LinkedList<Dish>(){
            {
                this.add(new Dish(R.drawable.morozh, R.string.list_view_item_morozh, "150г", "232К", R.drawable.ok));
                this.add(new Dish(R.drawable.syr, R.string.list_view_item_syr, "50г", "384К", R.drawable.ok));
                this.add(new Dish(R.drawable.vyn, R.string.list_view_item_vyn, "150г", "431К", R.drawable.no));
                this.add(new Dish(R.drawable.gamb, R.string.list_view_item_gam, "200г", "232К", R.drawable.ok));
                this.add(new Dish(R.drawable.kofe, R.string.list_view_item_kofe, "150г", "323К", R.drawable.no));
                this.add(new Dish(R.drawable.free, R.string.list_view_item_kartofel, "100г", "678К", R.drawable.no));
                this.add(new Dish(R.drawable.apple, R.string.list_view_item_aple, "50г", "232К", R.drawable.not_yes));
                this.add(new Dish(R.drawable.chay, R.string.list_view_item_tea, "1000г", "1224К", R.drawable.not_yes));
                this.add(new Dish(R.drawable.kasha, R.string.list_view_item_kash, "256г", "124К", R.drawable.ok));
            }
        };
        this.dishListCopy = new LinkedList<>();
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
        listView.setOnScrollListener(touchListener.makeScrollListener());
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
