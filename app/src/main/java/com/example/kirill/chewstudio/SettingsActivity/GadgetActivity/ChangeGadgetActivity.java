package com.example.kirill.chewstudio.SettingsActivity.GadgetActivity;

import android.app.Activity;
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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity.ChooseMenuActivity;
import com.example.kirill.chewstudio.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

public class ChangeGadgetActivity extends AppCompatActivity
{

    private SharedPreferences preferences;
    // Ключи и константы для ListView списка устройств и его SimpleAdapter
    final static String IDT_LIST_NAME = "name";                                                     // Текстовый ключ поля имени устройства
    final static String IDT_LIST_ADDRESS = "address";                                               // Текстовый ключ поля адреса устройства
    final static String IDT_LIST_RSSI = "rssi";                                                     // Текстовый ключ поля мощности принимаемого сигнала устройства
    final static String IDT_LIST_ICON = "icon";                                                     // Текстовый ключ поля иконки мощности

    final static String[] LST_FROM = { IDT_LIST_NAME,                                               // Массиив имен атрибутов, из каких полей HashMap будут читаться данные
            IDT_LIST_ADDRESS,
            IDT_LIST_RSSI,
            IDT_LIST_ICON };

    final static int[] LST_TO = { R.id.device_name,                                                 // Массив ID View-компонентов, в которые будут записываться данные
            R.id.device_address,
            R.id.device_rssi,
            R.id.img_rssi };


//    final static String CHEW_SERVICE1 = "00001800-0000-1000-8000-00805f9b34fb";                     // Стринг с идентификатором сервиса жуйфона
//    final static String CHEW_SERVICE2 = "00001801-0000-1000-8000-00805f9b34fb";
//    final static String CHEW_SERVICE3 = "00001523-1212-efde-1523-785feabcd123";



    // Один дескриптор на несколько сервисов сразу (или даже на все?)
    // (базовая часть для всех дескрипторов и стандартных сервисов одинаковая?)
    final static UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    final static UUID BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    final static UUID BATTERY_LEVEL_CHARACTERISTIC = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");

    final static UUID GENERIC_ATTRIBUTE_SERVICE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    final static UUID SERVICE_CHANGED_CHARACTERISTIC = UUID.fromString("00002A05-0000-1000-8000-00805f9b34fb");

//    final static UUID SERVICE_MY_CHARACTERISTIC = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb");

    final static UUID SERVICE_MY_CHARACTERISTIC = UUID.fromString("00001524-1212-efde-1523-785feabcd123");


    // Элементы экрана
    static ProgressBar pbProgressBar;                                                               // Лучше просто поменять ссылку на экземпляр Activity при новом открытии (повороте экрана)
    static ListView lvMain;






    //    static Boolean fWarmStart = false;                                                              // Флаг горячего запуска активити
    static Boolean fEnableBTRequest = false;                                                        // Флаг наличия запроса к системному активити на активацию Bluetooth

    //----------------------------------------------------------------------------------------------
    //
    //                                     Константы
    //
    //----------------------------------------------------------------------------------------------

    static final int REQUEST_ENABLE_BT = 1;
    static final int SCAN_PERIOD = 1000;                                                            // Время поиска устройств - 5 секунд
    static final int SCAN_STEP = 25;                                                                // Шаг обновления ProgressBar - 25 мс
    // (SCAN_PERIOD должен быть кратен SCAN_STEP


//    static final String DEVICE_NAME = "HMSoft"; //display name for Grove BLE

    //----------------------------------------------------------------------------------------------




    BluetoothDevice mDevice;            //external BLE device (Grove BLE module)

    Timer mTimer;



    public class LeListEntry extends HashMap<String, Object>                                        // Класс производное от HashMap, описывающий одну запись ListView для списка устройств
    {
        // Конструктор с параметрами
        public LeListEntry(String name, String address, int rssi)
        {
            super();                                                                                // Вызов конструктора суперкласса

            int img = R.drawable.signal_strength_bar_0;                                             // Выбор иконки мощности в зависимости от параметра rssi

            if (rssi > -90)
                img = R.drawable.signal_strength_bar_1;
            if (rssi > -85)
                img = R.drawable.signal_strength_bar_2;
            if (rssi > -80)
                img = R.drawable.signal_strength_bar_3;
            if (rssi > -70)
                img = R.drawable.signal_strength_bar_4;
            if (rssi > -60)
                img = R.drawable.signal_strength_bar_5;

            super.put(IDT_LIST_NAME, name);                                                         // Заносим запись в поле имени устройства
            super.put(IDT_LIST_ADDRESS, address);                                                   // Заносим запись в поле адреса устройства
            super.put(IDT_LIST_RSSI, Integer.toString(rssi) + " dBm");                              // Заносим запись в поле мощности устройства
            super.put(IDT_LIST_ICON, img);                                                          // Заносим запись в поле иконки мощности
        }
    }

    public class ExtBluetoothDevice                                                                 // Класс содержащий класс BluetoothDevice, а так же дополнительные данные (rssi и т.д.)
    {                                                                                               // (расширить базовый класс BluetoothDevice невозможно, поэтому он помещен внутрь нашего класса)
        public BluetoothDevice device;
        public int rssi;

        public ExtBluetoothDevice(BluetoothDevice device, int rssi)
        {
            this.device = device;
            this.rssi = rssi;
        }

    }




    // массивы данных
    String[] texts1 = { "Chew-Mini", "Chew-Fon", "sometext 3",
            "sometext 4", "sometext 5", "sometext 6" };
    String[] texts2 = { "ED-56-45-23-5A", "12:34:EА:23:A6", "текст 3",
            "текст 4", "текст 5", "текст 6" };



    //----------------------------------------------------------------------------------------------
    //
    //                                     Статические данные
    //
    //----------------------------------------------------------------------------------------------
    static BluetoothAdapter mBluetoothAdapter = null;                                               // Ссылка на Bluetooth-адаптер
    // Используется статическая переменная изначально инициализированная в null,
    // дабы отличать первый запуск приложения и перезапуск с уже найденным Bluetooth
    // Так же используется, как флаг холодного запуска активити

    static BluetoothGatt mBluetoothGatt = null;                                                     // Дескриптор GATT-сервера
    static BluetoothGattService mBluetoothGattService = null;                                       // Дескриптор выбранного GATT-сервиса

    static List<ExtBluetoothDevice> mDevices = new ArrayList<ExtBluetoothDevice>();                 // Список найденных Bluetooth LE устройств

    static ArrayList<LeListEntry> leListEntries = new ArrayList<LeListEntry>();                               // Массив-список Bluetooth LE устройств для ListView

    static SimpleAdapter sAdapter;                                                                  // Адаптер для ListView списка Bluetooth LE устройств



    //**********************************************************************************************
    //
    //                Метод onCreate() вызывается при создании активити
    //
    //**********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);                                                         // Стандартные функции при создании активити
        setContentView(R.layout.activity_change_gadget);

        //this.initToolbar();
        preferences = getSharedPreferences(GadgetActivity.BLUETOOTH_PREFERENCES, Context.MODE_PRIVATE);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

//        getSupportActionBar().setTitle("Chew Studio");                                              // Меняем надпись в ActionBar
//        getSupportActionBar().

        Log.w("BLE01", " ");
//        Log.w("BLE01", "On create");

        Log.w("BLE01", "Adapter = " + Boolean.toString(mBluetoothAdapter != null));

        // Инициализируем указатели на элементы интерфейса

        pbProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        lvMain = (ListView) findViewById(R.id.listView1);



//--------------------------------------------------------------------------------------------------


        if (mBluetoothAdapter == null)                                                              // Если первый запуск программы, то
        {                                                                                           //
                                                 // печатаем текст

            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))         // Если Bluetooth LE не поддерживается на нашем устройстве, то
            {
                Toast.makeText(this, "BLE not supported on this device", Toast.LENGTH_SHORT).show();// Выводим тост "BLE not supported on this device",
                finish();                                                                           // и закрываем активити
                return;
            }

            statusUpdate("BLE поддерживается на этом устройстве");

            final BluetoothManager mBluetoothManager =                                              // Получить ссылку на Bluetooth Manager
                    (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);

            mBluetoothAdapter = mBluetoothManager.getAdapter();                                     // Инициализируем Bluetooth Adapter

            if (mBluetoothAdapter == null)                                                          // Проверяем поддержку Bluetooth LE на этом устройстве
            {
                Toast.makeText(this, "BLE not supported on this device", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }


            sAdapter = new SimpleAdapter(this, leListEntries, R.layout.item_devicelist_layout,             // Создаем статический адаптер для ListView списка устройств
                    LST_FROM, LST_TO);                                         //


        }
        else                                                                                        // Если же повторный запуск активити, то
        {
            if (savedInstanceState == null)                                                         // Если не было сохраненного состояния интерфейса,
            {                                                                                       // то
                                                             // печатаем текст 'Горячий запуск'

                statusUpdate("BLE поддерживается на этом устройстве");
            }
        }



//--------------------------------------------------------------------------------------------------


        leListEntries.clear();                                                                      // Очищаем список устройств для ListView

        lvMain.setAdapter(sAdapter);                                                                // Присваиваем списку устройств адаптер

        constructLeLister();                                                                        // Создаем список устройств для ListView из статического списка блютус устройств mDevices

        lvMain.setVisibility(View.VISIBLE);                                                         // Проявить ListView на экране



//-------------------------------------------------------------------------------------------------- Настроить обработку нажатия на элемент ListView

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id)
            {
                statusUpdate("Clicked pos:" + position +
                        ", name:" + mDevices.get(position).device.getName());

                Toast.makeText(getApplicationContext(), mDevices.get(position).device.getName(),
                        Toast.LENGTH_SHORT).show();


                BluetoothDevice device = mDevices.get(position).device;
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString(GadgetActivity.PREFERENCE_DEVICE_ADDRESS, device.getAddress());
                edit.putString(GadgetActivity.PREFERENCE_DEVICE_NAME, device.getName());
                edit.apply();

                Bundle extras = getIntent().getExtras();
                if(extras != null && extras.getInt(ChooseMenuActivity.REQUEST_FLAG_GADGET) == 1){
                    setResult(RESULT_OK);
                    finish();
                }

            }

        });


        lvMain.setVisibility(View.VISIBLE);


        Log.w("BLE01", "ListVisible");

        mDevices.clear();
        constructLeLister();                                                                        // Создаем список устройств для ListView из статического списка блютус устройств mDevices
        scanLeDevice();
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


    //**********************************************************************************************
    //
    //                Восстановить состояние пользовательского интерфейса
    //
    //  Метод вызывается сразу после onStart() в том случае, если в savedInstanceState было
    //  записано предыдущее состояние пользовательского интерфейса
    //
    //**********************************************************************************************
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстанавливаем состояние пользовательского интерфейса

        //noinspection ResourceType                                                                 // Указание компилятору не ругаться на несоответствие типов
        lvMain.setVisibility(savedInstanceState.getInt("LISTVIEW_STATE_KEY"));

        Log.w("BLE01", "onRestore");

    }

    //**********************************************************************************************
    //
    //                   Сохранить состояние пользовательского интерфейса
    //
    //  Метод вызывается перед onStop() в том случае, если пользователь не закрыл приложение
    //  собственноручно (т.е. вызывается при повороте экрна, нажатии Home и т.д.)
    //
    //**********************************************************************************************
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        // Сохраняем состояние пользовательского интерфейса

        savedInstanceState.putInt("LISTVIEW_STATE_KEY", lvMain.getVisibility());

        Log.w("BLE01", "onSave");
    }

    //**********************************************************************************************
    //
    //                      Метод вызываемый перд закрытием приложения
    //
    //**********************************************************************************************
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.w("BLE01", "On destroy");
    }


    //**********************************************************************************************
    //
    //                  Вызов onResume() при готовности активити к показу
    //
    //**********************************************************************************************
    @Override
    protected void onResume()
    {
        super.onResume();

//        Log.w("BLE01", "onResume");

        if (!mBluetoothAdapter.isEnabled())                                                         // Если Bluetooth запрешен, то
        {
//            btAdapterService.setBluetoothState(BluetoothAdapterService.ON);

            if (!fEnableBTRequest)                                                                  // Если запрос на активацию Bluetooth еще не создан, то
            {
                fEnableBTRequest = true;                                                            // Установить флаг запроса
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);         // Создать намерение для открытия новой активити
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);                          // Запустить окно системной активити с запросом разрешения Bluetooth
            }
        }

        // сюда доходит сразу, не ожидая ответа пользователя на включение БТ. Т.е. параллельно.

    }


    //**********************************************************************************************
    //
    //            Ответ от системного активити с запросом разрешения включения Bluetooth
    //
    //**********************************************************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == REQUEST_ENABLE_BT))                                                     // Если пришел ответ от активити запроса разрешения Bluetooth, то
        {
            fEnableBTRequest = false;                                                               // Снять флаг запроса

            if (resultCode == Activity.RESULT_CANCELED)                                             // Если пользователь не разрешил Bluetooth, то
            {
                Toast.makeText(this, "Bluetooth disabled", Toast.LENGTH_SHORT).show();              // Вывести тост "Bluetooth disabled"
                finish();                                                                           // завершить программу
                return;
            }
            else if (resultCode == Activity.RESULT_OK)                                              // Иначе, если пользователь разрешил Bluetooth, то
            {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();               // Вывести тост "Bluetooth enabled"
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    //**********************************************************************************************


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        // Выводим диалоговое окно на экран

        lvMain.setVisibility(View.VISIBLE);


        Log.w("BLE01", "ListVisible");

        mDevices.clear();
        constructLeLister();                                                                        // Создаем список устройств для ListView из статического списка блютус устройств mDevices
        scanLeDevice();


        return super.onOptionsItemSelected(item);
    }

    //**********************************************************************************************
    //
    //                              Поиск Bluetooth LE устройств
    //
    // Замечание: Работает в фоновом потоке, вызывая для обновления функции UI-потока
    //
    //**********************************************************************************************
    private void scanLeDevice()
    {
        new Thread()
        {
            @Override
            public void run()
            {

                Log.w("BLE01", "startLeScan");

                statusUpdate("Start LE Scan");

                mBluetoothAdapter.startLeScan(mLeScanCallback);                                     // Начать сканирование Bluetooth LE устройств
                // (Не может быть запущен дважды. Пока не будет остановлен через stopLeScan,
                // заново не запустится)

                for (int i=0; i<=SCAN_PERIOD; i+=SCAN_STEP)                                          // Цикл на SCAN_PERIOD/SCAN_STEP шагов
                {
                    progressBarUpdate((i * 100) / SCAN_PERIOD);

                    try                                                                             // Заснуть на SCAN_STEP миллисекунд
                    {                                                                               //
                        Thread.sleep(SCAN_STEP);                                                    //
                    }                                                                               //
                    catch (InterruptedException e)                                                  //
                    {                                                                               //
                        e.printStackTrace();                                                        //
                    }                                                                               //
                }

                Log.w("BLE01", "stopLeScan");

                statusUpdate("Stop LE Scan");

                mBluetoothAdapter.stopLeScan(mLeScanCallback);                                      // Остановить сканирование Bluetooth LE устройств

            }
        }.start();
    }

    //**********************************************************************************************
    //
    //                              Колбек для startLeScan
    //              Вызывается при нахождении новго Bluetooth LE устройства
    //
    // Замечание: Работает в фоновом потоке, вызывая для обновления функции UI-потока
    //
    //**********************************************************************************************
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord)
        {

            Log.w("BLE01", "onLeScan device: " + device.getName() + " rssi:" + rssi);

            if (device.getName() != null)
                statusUpdate("Found device " + device.getName() + " rssi: " + rssi);

            leDeviceListUpdate(new ExtBluetoothDevice(device, rssi));

        }
    };


    //**********************************************************************************************
    //
    //                           Метод вывода текстовых сообщений
    //
    //          Может вызываться из любых потоков, т.к. запускается в потоке UI
    //
    //**********************************************************************************************
    private void statusUpdate(final String msg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                Log.w("BLE01", msg);

            }
        });
    }

    //**********************************************************************************************
    //
    //                      Обновление информации о статусе соединения
    //
    //           Может вызываться из любых потоков, т.к. запускается в потоке UI
    //
    //  Входные данные: msg   - сообщение для строки статуса
    //                  color - цвет фона строки статуса
    //
    //**********************************************************************************************
    private void changeConnectStatusText(final String msg, final int color)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

            }
        });
    }

    //**********************************************************************************************
    //
    //                           Метод обновления ProgressBar
    //
    //          Может вызываться из любых потоков, т.к. запускается в потоке UI
    //
    //**********************************************************************************************
    private void progressBarUpdate(final int Progress)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbProgressBar.setProgress(Progress);
            }
        });
    }




    //**********************************************************************************************
    //
    //               Добавить Bluetooth LE устройство в список найденных устройств
    //
    //  Замечание: Запускается из любого потока, выполняется в UI-потоке
    //
    //**********************************************************************************************
    private void leDeviceListUpdate(final ExtBluetoothDevice extdevice)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//              Log.w("BLE01", "Dataset Changed");

                if ((extdevice.device.getName() != null) &&                                         // Если устройство с ненулевым полем Name,
                        (mDevices.indexOf(extdevice) == -1))                                            // и отсутствует в нашем списке, то
                {


                    for (ExtBluetoothDevice idevice : mDevices)                                     // Цикл перечисления всех записей в mDevices
                        if (idevice.device.equals(extdevice.device))                                // Если добавляемое устройство уже имеется
                        {                                                                           // в поле device списка mDevice, то
//                            statusUpdate("obj duplicated: " + extdevice.device);
                            return;                                                                 // Выйти
                        }
                    // Замечание: нельзя сравнивать целиком обьекты extdevice, т.к. будет сравниваться
                    // лишь адрес обьектов, который всегда разный. Тогда как при сравнении обьекта device
                    // (тип BluetoothDevice) сравниваются сетевые (MAC) адреса устройств

                    mDevices.add(extdevice);                                                        // Добавить устройство в список

                    leListEntries.add(new LeListEntry(extdevice.device.getName(),                   // Добавить устройство в ListView
                            extdevice.device.getAddress(),                //
                            extdevice.rssi));                             //

                    sAdapter.notifyDataSetChanged();                                                // Обновить ListView
                }
            }
        });
    }


    //**********************************************************************************************
    //
    //               Составить ListView со списком Bluetooth LE устройств
    //
    //**********************************************************************************************
    private void constructLeLister()
    {
        leListEntries.clear();                                                                      // Очищаем список устройств для ListView

        for (int i = 0; i < mDevices.size(); i++)
        {
            leListEntries.add(new LeListEntry(mDevices.get(i).device.getName(),                     // Имя устройства
                    mDevices.get(i).device.getAddress(),                                            // Адрес устройства
                    mDevices.get(i).rssi));                                                         // Мощность сигнала устройства
        }

        sAdapter.notifyDataSetChanged();

    }


    //**********************************************************************************************
    //
    //                           Соединение с Bluetooth LE устройством
    //
    // Замечание: Работает в фоновом потоке, вызывая для обновления функции UI-потока
    //
    //**********************************************************************************************
    private void connectLeDevice(final BluetoothDevice device)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                Log.w("BLE01", "connect");

                statusUpdate("Connect");


                mBluetoothGatt = device.connectGatt(getApplicationContext(), false, mGattCallback);


            }
        }.start();
    }


    //**********************************************************************************************
    //
    //                              Колбек для connectLeDevice
    //                     Вызывается при соединении с GATT сервером
    //
    // Замечание: Работает в фоновом потоке, вызывая для обновления функции UI-потока
    //
    //**********************************************************************************************
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
                    statusUpdate("Connected to GATT, searching for services ");

                    mBluetoothGatt.discoverServices();
                }
                else if (newState == BluetoothProfile.STATE_DISCONNECTED)
                {
                    // На телефоне Samsung S4 mini при разрыве связи попадает сюда
                    //changeConnectStatusText("NOT CONNECTED", R.color.colorAlert);

                    Log.w("BLE01", "Disconnected from GATT server");
                    statusUpdate("Disconnected from GATT server");
                }
            }
            else
            {
                //changeConnectStatusText("NOT CONNECTED", R.color.colorAlert);

                statusUpdate("GATT connection error: " + status);                                   // Обычно происходит, если Bluetooth-устройство не отвечает (выключено),
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

                statusUpdate("Services discovered:");

                for(BluetoothGattService gattService : gattServices) {
                    statusUpdate("> " + gattService.getUuid());


                    List<BluetoothGattCharacteristic> gattCharacteristics =
                            gattService.getCharacteristics();

                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
                    {
                        int flag = gattCharacteristic.getProperties();

                        if (SERVICE_MY_CHARACTERISTIC.equals(gattCharacteristic.getUuid()))
                        {
                            statusUpdate("found! " + BluetoothGattCharacteristic.PROPERTY_NOTIFY);
                            if ((gattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
                            {

                                mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);

                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(
                                        CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);

                                statusUpdate(" desc = " + descriptor);


                                if (descriptor != null)
                                {
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                                    statusUpdate(" res: " + mBluetoothGatt.writeDescriptor(descriptor));

                                    //changeConnectStatusText("CONNECTED", R.color.colorDarkGreen);

                                }


                            }
                        }

//                            mBluetoothGatt.readCharacteristic(gattCharacteristic);                      // Прочитать характеристику (нельзя читать несколько подряд, пока предыдущие не отработал?)

                        try                                                                             //
                        {                                                                               //
                            statusUpdate(" -> " + gattCharacteristic.getUuid());
//                            statusUpdate("    " + gattCharacteristic.getValue());
                            statusUpdate("    " + flag);


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
                statusUpdate("onServicesDiscovered received: " + status);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                statusUpdate("Readed");
                statusUpdate(">> " + characteristic.getUuid());

                statusUpdate("   " + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));

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

            }
        });

    }



}
