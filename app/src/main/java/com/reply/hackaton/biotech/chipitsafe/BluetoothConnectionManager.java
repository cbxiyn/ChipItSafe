package com.reply.hackaton.biotech.chipitsafe;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class BluetoothConnectionManager {

    private final BluetoothConnectionManagerListener listener;

    interface BluetoothConnectionManagerListener{
        void onNewDeviceFound(BluetoothDevice bluetoothDevice);
    }

    private String LOG_TAG = "BluetoothConnectionManager";

    private BluetoothAdapter mBluetoothAdapter;
    final BluetoothManager bluetoothManager;


    private boolean scanning;
    private BluetoothLeScanner scanner;
    private Handler handler = new Handler();
    private final static int SCAN_PERIOD = 20000;


    //ArrayList<BluetoothDevice> scannedDevices = new ArrayList<BluetoothDevice>();
    List<String> scannedDevNames = new ArrayList<>();
    Map<String, BluetoothDevice> scannedDevices = new HashMap<String, BluetoothDevice>();

    public BluetoothConnectionManager(BluetoothConnectionManagerListener listener, Context context){
        this.listener = listener;
        // Initializes Bluetooth adapter.
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        scanner = mBluetoothAdapter.getBluetoothLeScanner();
    }


    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void startBleScan() {
        Log.d(LOG_TAG,"within startBLEScan");
        //scannedDevices = new ArrayList<BluetoothDevice>();
        scannedDevices = new HashMap<String,BluetoothDevice>();
        scannedDevNames = new ArrayList<String>();
        // scanning a true significa "scansione in corso"
        scanning = true;
        // avviamo la scansione da un thread secondario
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // avvio della scansione
                Log.d(LOG_TAG,"starting scan");
                scanner.startScan(leScanCallback);
            }
        });
        // l'oggetto Handler viene utilizzato per impostare un timeout
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // tempo scaduto per la scansione
                // scansione interrotta
                Log.d(LOG_TAG,"stopping scan");
                scanner.stopScan(onStopScanCallback);
                // scanning=false significa "Nessuna scansione in corso"
                scanning = false;
            }
        }, SCAN_PERIOD);
        // SCAN_PERIOD indica una durata in millisecondi
    }


    public void stopBleScan(){
        scanner.stopScan(onStopScanCallback);
    }


    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            /*
            L’oggetto di tipo ScanResult (il secondo argomento del metodo onScanResult) durante la scansione porterà i dati
            relativi ad ogni dispositivo individuato. Mediante il suo metodo getDevice() si ottiene un oggetto di tipo BluetoothDevice
            che presenta metodi da cui trarre informazioni (getAddress() per l’indirizzo e getName() per il nome associato
            all’apparecchio, tanto per fare un paio di esempi) ed altri dedicati alla connessione GATT.
            */
            Log.d(LOG_TAG,"onScanResult!");

            BluetoothDevice device = result.getDevice();

            // code from movesense


            // Process scan result here. filter movesense devices.
            if (device!=null &&
                    device.getName() != null
                    //&& device.getName().startsWith("Movesense")
                    ) {
                Log.d("BluetoothDev FOUND","scanName: " + device.getName());
                Log.d("BluetoothDev FOUND","scanAddr: " + device.getAddress());
                // replace if exists already, add otherwise
                if(!scannedDevices.containsKey(device.getAddress())) {
                    scannedDevices.put(device.getAddress(), device);
                    scannedDevNames.add(device.getName() + " - address:" + device.getAddress());
                    if (listener != null) {
                        listener.onNewDeviceFound(device);
                    }
                }

            }

        }
    };


    private ScanCallback onStopScanCallback = new ScanCallback() {
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);


            // scan not succeeded

            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.row, R.id.textViewList, scannedDevNames);
            //listView.setAdapter(arrayAdapter);
        }
    };

    /*
    API 18
    https://developer.android.com/guide/topics/connectivity/bluetooth-le#java
    private boolean mScanning;
    private Handler mHandler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }
    */

}
