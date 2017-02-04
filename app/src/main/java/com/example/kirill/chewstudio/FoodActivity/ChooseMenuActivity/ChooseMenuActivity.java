package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity;

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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kirill.chewstudio.R;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.ChangeGadgetActivity;
import com.example.kirill.chewstudio.SettingsActivity.GadgetActivity.GadgetActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ChooseMenuActivity extends AppCompatActivity {
    public static final int REQUEST_SET_GADGET = 1;
    public static final String REQUEST_FLAG_GADGET = "flag";
    private static final int SCAN_PERIOD = 1000;
    private static final int SCAN_STEP = 25;
    private final static UUID SERVICE_MY_CHARACTERISTIC = UUID.fromString("00001524-1212-efde-1523-785feabcd123");
    final static UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private ListView listView;
    private List<Dish> dishList;
    private List<Dish> dishListCopy;
    private ListViewAdapter mAdapter;

    private SharedPreferences preferences;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String macAddress;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);
        initComponents();
    }

    private void initComponents() {
        this.preferences = getSharedPreferences(GadgetActivity.BLUETOOTH_PREFERENCES, Context.MODE_PRIVATE);

        BluetoothManager mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (!mBluetoothAdapter.isEnabled())                                                         // Если Bluetooth запрешен, то
        {
//            btAdapterService.setBluetoothState(BluetoothAdapterService.ON);

                                                           // Установить флаг запроса
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);         // Создать намерение для открытия новой активити
                startActivity(enableBtIntent);                          // Запустить окно системной активити с запросом разрешения Bluetooth

        }

        initToolbar();
        initFloatingsActionButton();
        initListView();
    }

    private void initFloatingsActionButton() {
        Button fab = (Button) findViewById(R.id.button_start);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.contains(GadgetActivity.PREFERENCE_DEVICE_ADDRESS) && preferences.contains(GadgetActivity.PREFERENCE_DEVICE_NAME)) {
                    macAddress = preferences.getString(GadgetActivity.PREFERENCE_DEVICE_ADDRESS, "");
                    name = preferences.getString(GadgetActivity.PREFERENCE_DEVICE_NAME, "");
                    Log.d("action button", "enter");
                    scanLeDevice();
                }
                else {
                    //Toast.makeText(ChooseMenuActivity.this, "Устройство в списке не авыбрано!!! Тут я буду предлагать выбрать устройство", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChooseMenuActivity.this, ChangeGadgetActivity.class);
                    intent.putExtra(REQUEST_FLAG_GADGET, REQUEST_SET_GADGET);
                    startActivityForResult(intent, REQUEST_SET_GADGET);
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mBluetoothGatt != null)
            this.mBluetoothGatt.disconnect();
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
                    printViewRate("Подключение прошло успешно. Начинаем кушать!!!");
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
            printViewRate("Жевание: " + val);
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
                Toast.makeText(ChooseMenuActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
