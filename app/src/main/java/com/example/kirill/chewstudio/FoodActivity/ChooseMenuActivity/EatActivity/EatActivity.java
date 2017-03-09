package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity.EatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity.Dish;
import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.ChangeGadgetActivity;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.GadgetActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EatActivity extends AppCompatActivity {
    public static final int REQUEST_SET_GADGET = 1;
    public static final String REQUEST_FLAG_GADGET = "flag";
    private static final int SCAN_PERIOD = 1000;
    private static final int SCAN_STEP = 25;
    private final static UUID SERVICE_MY_CHARACTERISTIC = UUID.fromString("00001524-1212-efde-1523-785feabcd123");
    final static UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String macAddress;
    private String name;
    private SharedPreferences preferences;

    private CustomSeekBar seekbar;
    private TextView textViewChews;
    private TextView textViewCalories;
    private TextView textViewProteins;
    private TextView textViewFats;
    private TextView textViewCarb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);
        initComponents();
    }

    private void initComponents() {
        this.preferences = getSharedPreferences(GadgetActivity.BLUETOOTH_PREFERENCES, Context.MODE_PRIVATE);
        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        extras.setClassLoader(getClassLoader());
        map = (HashMap<Integer, Dish>) extras.getSerializable(ChooseMenuActivity.DISH_ARRAY);*/
        initBluetoothServices();
        initToolbar();
        //initBluetoothConnection();
        initSeekBar();
        initTextViews();
        initChartChews();
        initViewPager();
    }

    private void initBluetoothServices() {
        BluetoothManager mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (!mBluetoothAdapter.isEnabled())                                                         // Если Bluetooth запрешен, то
        {
//            btAdapterService.setBluetoothState(BluetoothAdapterService.ON);

            // Установить флаг запроса
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);         // Создать намерение для открытия новой активити
            startActivity(enableBtIntent);                          // Запустить окно системной активити с запросом разрешения Bluetooth

        }
    }

    private void initBluetoothConnection() {
        if(preferences.contains(GadgetActivity.PREFERENCE_DEVICE_ADDRESS) && preferences.contains(GadgetActivity.PREFERENCE_DEVICE_NAME)) {
            macAddress = preferences.getString(GadgetActivity.PREFERENCE_DEVICE_ADDRESS, "");
            name = preferences.getString(GadgetActivity.PREFERENCE_DEVICE_NAME, "");
            Log.d("action button", "enter");
            scanLeDevice();
        }
        else {
            //Toast.makeText(ChooseMenuActivity.this, "Устройство в списке не авыбрано!!! Тут я буду предлагать выбрать устройство", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EatActivity.this, ChangeGadgetActivity.class);
            intent.putExtra(REQUEST_FLAG_GADGET, REQUEST_SET_GADGET);
            startActivityForResult(intent, REQUEST_SET_GADGET);
        }
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

    private void initSeekBar() {
        float totalSpan = 1500;
        float redSpan = 200;
        float greenSpan = 400;
        float yellowSpan = 350;
        ArrayList<ProgressItem> progressItemList;
        ProgressItem mProgressItem;
        seekbar = ((CustomSeekBar) findViewById(R.id.seekBar0));

        progressItemList = new ArrayList<ProgressItem>();
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = ((redSpan / totalSpan) * 100);
        mProgressItem.color = R.color.red;
        progressItemList.add(mProgressItem);

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (yellowSpan / totalSpan) * 100;
        mProgressItem.color = R.color.yellow;
        progressItemList.add(mProgressItem);

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (greenSpan / totalSpan) * 100;
        mProgressItem.color = R.color.blue;
        progressItemList.add(mProgressItem);

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (yellowSpan / totalSpan) * 100;
        mProgressItem.color = R.color.yellow;
        progressItemList.add(mProgressItem);

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = ((redSpan / totalSpan) * 100);
        mProgressItem.color = R.color.red;
        progressItemList.add(mProgressItem);

        seekbar.setProgress(40);
        seekbar.setEnabled(false);
        seekbar.initData(progressItemList);
        seekbar.invalidate();
    }

    private void initTextViews() {
        this.textViewChews = (TextView) this.findViewById(R.id.content_eat_text_view_chews);
        this.textViewCalories = (TextView) this.findViewById(R.id.content_eat_text_view_calories);
        this.textViewProteins = (TextView) this.findViewById(R.id.content_eat_text_view_proteins);
        this.textViewFats = (TextView) this.findViewById(R.id.content_eat_text_view_fats);
        this.textViewCarb = (TextView) this.findViewById(R.id.content_eat_text_view_carb);
    }

    private LineChart mChart;
    private ArrayList<String> labels;

    private void initChartChews() {
        mChart = (LineChart) findViewById(R.id.chart_chews);
        mChart.setGridBackgroundColor(Color.WHITE);
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getXAxis().setDrawLabels(false);
        mChart.getLegend().setEnabled(false);
        mChart.setDescription("Жевание");

        ArrayList<Entry> entries = new ArrayList<>();
        LineDataSet dataset = new LineDataSet(entries, "");

        labels = new ArrayList<String>();
        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.LIBERTY_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        dataset.setDrawValues(false);
        dataset.setCircleSize(0f);

        mChart.setData(data);
        //mChart.animateY(100);
    }

    Thread thread = null;
    private void feedMultiple() {


        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    runOnUiThread(runnable);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    int index = 0;
    private void addEntry() {
        LineData data = mChart.getData();
        if (data != null) {
            labels.add("хз");
            data.addEntry(new Entry((float) + Math.sin((float)index), index), 0);
            index++;
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(7);
            mChart.moveViewToX(data.getXValCount());
        }
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) this.findViewById(R.id.content_eat_view_pager_dishes);
        List<View> list = new ArrayList<>();
        LayoutInflater inflator = LayoutInflater.from(this);

        ArrayList<Dish> dishList = new ArrayList<Dish>(){
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
        for (int i = 0; i < dishList.size(); i++) {
            Dish dish = dishList.get(i);
            View page = inflator.inflate(R.layout.dish_card_layout, null);
            ImageView image = (ImageView) page.findViewById(R.id.dish_card_layout_image);
            TextView textView = (TextView) page.findViewById(R.id.dish_card_text_view_description);
            image.setImageResource(dish.getDescribeDrawable());
            textView.setText(dish.getDescribe());
            list.add(page);
        }
        viewPager.setAdapter(new ViewPagerAdapter(list, viewPager));
    }

    private void scanLeDevice() {
        new Thread() {
            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);

                for (int i=0; i<=SCAN_PERIOD; i+=SCAN_STEP) {
                    try {
                        Thread.sleep(SCAN_STEP);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.w("BLE01", "stopLeScan");
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mBluetoothGatt != null)
            this.mBluetoothGatt.disconnect();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initBluetoothConnection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            if(requestCode == REQUEST_SET_GADGET){
                if(preferences.contains(GadgetActivity.PREFERENCE_DEVICE_ADDRESS) && preferences.contains(GadgetActivity.PREFERENCE_DEVICE_NAME)) {
                    macAddress = preferences.getString(GadgetActivity.PREFERENCE_DEVICE_ADDRESS, "");
                    name = preferences.getString(GadgetActivity.PREFERENCE_DEVICE_NAME, "");
                    scanLeDevice();
                }
            }
            else
                Toast.makeText(this, "Устройство не выбрано!", Toast.LENGTH_SHORT).show();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device.getName() != null && device.getAddress() != null && name != null && macAddress != null)
                        if (device.getName().equals(name) && device.getAddress().equals(macAddress)) {
                            Log.w("BLE01", "123");
                            connectLeDevice(device);
                        }
                }
            });
        }

    };

    private void connectLeDevice(final BluetoothDevice device) {
        new Thread()
        {
            @Override
            public void run() {
                mBluetoothGatt = device.connectGatt(getApplicationContext(), false, mGattCallback);
            }
        }.start();
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)                                               // Если операция прошла успешно, то
            {
                if (newState == BluetoothProfile.STATE_CONNECTED)
                {
                    Log.w("BLE01", "Connected to GATT, searching for services");
                    //statusUpdate("Connected to GATT, searching for services ");
                    printViewRate("Подключение прошло успешно.");
                    mBluetoothGatt.discoverServices();
                }
                else if (newState == BluetoothProfile.STATE_DISCONNECTED)
                {
                    // На телефоне Samsung S4 mini при разрыве связи попадает сюда
                    //changeConnectStatusText("NOT CONNECTED", R.color.colorAlert);

                    Log.w("BLE01", "Disconnected from GATT server");
                    //statusUpdate("Disconnected from GATT server");
                }
            }
            else
            {
                //changeConnectStatusText("NOT CONNECTED", R.color.colorAlert);

                //statusUpdate("GATT connection error: " + status);                                   // Обычно происходит, если Bluetooth-устройство не отвечает (выключено),
                // либо при ошибке связи
                // На телефоне ZTE при разрыве связи выводится Gatt Error: 8 (GATT CONN TIMEOUT)
                toastGattError(status);

            }
        }

        public void toastGattError(final int status)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "GATT Connection Error: " + status,
                            Toast.LENGTH_SHORT).show();
                }
            });

        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {

            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();

                //statusUpdate("Services discovered:");

                for(BluetoothGattService gattService : gattServices) {
                    //statusUpdate("> " + gattService.getUuid());


                    List<BluetoothGattCharacteristic> gattCharacteristics =
                            gattService.getCharacteristics();

                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
                    {
                        int flag = gattCharacteristic.getProperties();

                        if (SERVICE_MY_CHARACTERISTIC.equals(gattCharacteristic.getUuid()))
                        {
                            //statusUpdate("found! " + BluetoothGattCharacteristic.PROPERTY_NOTIFY);
                            if ((gattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
                            {

                                mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);

                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(
                                        CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);

                                //statusUpdate(" desc = " + descriptor);


                                if (descriptor != null)
                                {
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                                    mBluetoothGatt.writeDescriptor(descriptor);

                                    // changeConnectStatusText("CONNECTED", R.color.colorDarkGreen);

                                }


                            }
                        }

//                            mBluetoothGatt.readCharacteristic(gattCharacteristic);                      // Прочитать характеристику (нельзя читать несколько подряд, пока предыдущие не отработал?)

                        try                                                                             //
                        {                                                                               //
                            //statusUpdate(" -> " + gattCharacteristic.getUuid());
//                            statusUpdate("    " + gattCharacteristic.getValue());
                            // statusUpdate("    " + flag);


                        }                                                                               //
                        catch (Exception e)                                                             //
                        {                                                                               //
                            e.printStackTrace();                                                        //
                        }                                                                               //

                    }

//                    if (CHEW_SERVICE1.equals(gattService.getUuid().toString()))
//                    {
//                        mBluetoothGattService = gattService;
//
//                        statusUpdate("Found communication Service");
//                    }
                }
            }
            else
            {
                //statusUpdate("onServicesDiscovered received: " + status);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                //statusUpdate("Readed");
                //statusUpdate(">> " + characteristic.getUuid());

                // statusUpdate("   " + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));

//               statusUpdate("   " + characteristic.getStringValue(0));


            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            final BluetoothGattCharacteristic characteristic)
        {
            int val = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);

//            if (val < 255)
//            {
            printTextViews(val);
//            }
//            else
//            {
//                printViewRate("Глотание");
//            }

//           statusUpdate("Characteristic CHANGED: " + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0));


        }


    };

    public void printViewRate(final String msg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(EatActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int prevChew;
    public void printTextViews(final int msg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("Chews", msg + "");
                feedMultiple();
                textViewChews.setText(getResources().getString(R.string.content_eat_text_view_chews) + " " + String.valueOf(msg));
                if(prevChew > msg){
                    textViewCalories.setText(getResources().getString(R.string.content_eat_text_view_calories) + " " + String.valueOf(msg * 5));
                    textViewProteins.setText(getResources().getString(R.string.content_eat_text_view_proteins) + " " + String.valueOf(msg * 4));
                    textViewFats.setText(getResources().getString(R.string.content_eat_text_view_fats) + " " + String.valueOf(msg * 2));
                    textViewCarb.setText(getResources().getString(R.string.content_eat_text_view_carb) + " " + String.valueOf(msg * 3));
                }
                prevChew = msg;
            }
        });

    }
}
