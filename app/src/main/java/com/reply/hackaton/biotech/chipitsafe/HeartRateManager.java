package com.reply.hackaton.biotech.chipitsafe;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.movesense.mds.Mds;
import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;

public class HeartRateManager {

    private String LOG_TAG = "HeartRateManager";

    private Context currentContext;

    private Mds mMds;


    public HeartRateManager(Context c){
        currentContext = c;
    }

    public void initMds() {
        mMds = Mds.builder().build(currentContext);
    }


    public void connectToDevice(BluetoothDevice device){
        /*if (!device.isConnected()) { */
            //RxBleDevice bleDevice = getBleClient().getBleDevice(device.macAddress);
            Log.i(LOG_TAG, "Connecting to BLE device: " +  device.getAddress() /*bleDevice.getMacAddress()*/);
            mMds.connect(  device.getAddress() /*bleDevice.getMacAddress()*/,
                    new MdsConnectionListener() {

                @Override
                public void onConnect(String s) {
                    Log.d(LOG_TAG, "onConnect:" + s);
                }

                @Override
                public void onConnectionComplete(String macAddress, String serial) {
                    Log.d(LOG_TAG, "onConnectionComplete:" + macAddress);
                    /*
                    for (MyScanResult sr : mScanResArrayList) {
                        if (sr.macAddress.equalsIgnoreCase(macAddress)) {
                            sr.markConnected(serial);
                            break;
                        }
                    }
                    */
                    //mScanResArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(MdsException e) {
                    Log.e(LOG_TAG, "onError:" + e);

                    //showConnectionError(e);
                }

                @Override
                public void onDisconnect(String bleAddress) {
                    Log.d(LOG_TAG, "onDisconnect: " + bleAddress);
                    /*
                    for (MyScanResult sr : mScanResArrayList) {
                        if (bleAddress.equals(sr.macAddress))
                            sr.markDisconnected();
                    }
                    */
                    //mScanResArrayAdapter.notifyDataSetChanged();
                }
            });
        /*
        }
        else
        {
            Log.i(LOG_TAG, "Disconnecting from BLE device: " + device.macAddress);
            mMds.disconnect(device.macAddress);
        }
        */
    }






}
