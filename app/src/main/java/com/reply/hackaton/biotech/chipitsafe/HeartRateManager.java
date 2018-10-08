package com.reply.hackaton.biotech.chipitsafe;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.movesense.mds.Mds;
import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsResponseListener;
import com.movesense.mds.MdsSubscription;

public class HeartRateManager implements MdsNotificationListener{

    private String LOG_TAG = "HeartRateManager";

    private Context currentContext;

    private Mds mMds;


    private BluetoothDevice heartRateDevice;
    private String hearRateDeviceSerial;

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

            final BluetoothDevice deviceAttemptingToConnectTo = device;

            mMds.connect(  device.getAddress() /*bleDevice.getMacAddress()*/,
                    new MdsConnectionListener() {


                /**
                * Called when Mds / Whiteboard link-layer connection (BLE) has been succesfully established
                *
                */
                @Override
                public void onConnect(String s) {
                    Log.d(LOG_TAG, "onConnect:" + s);

                }

                /**
                * Called when the full Mds / Whiteboard connection has been succesfully established
                *
                */
                @Override
                public void onConnectionComplete(String macAddress, String serial) {
                    Log.d(LOG_TAG, "onConnectionComplete:devSerial" + serial);
                    heartRateDevice = deviceAttemptingToConnectTo;
                    hearRateDeviceSerial = serial;
                    subscribeToHeartECGNotifications();
                    //getDeviceInfo();
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

                /**
                * Called when Mds connect() call fails with error
                *
                */
                @Override
                public void onError(MdsException e) {
                    Log.e(LOG_TAG, "onError:" + e);

                    //showConnectionError(e);
                }

                /**
                * Called when Mds connection disconnects (e.g. device out of range)
                *
                */
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


    /*

    MDS REST API

The MDS library exposes the REST api on the Movesense devices via the following methods:

    public void get(@NonNull String uri, String contract, MdsResponseListener callback);
    public void put(@NonNull String uri, String contract, MdsResponseListener callback);
    public void post(@NonNull String uri, String contract, MdsResponseListener callback);
    public void delete(@NonNull String uri, String contract, MdsResponseListener callback);

    public MdsSubscription subscribe(@NonNull String uri, String contract, MdsNotificationListener listener);

    GET, PUT, POST, DELETE

    The first four of the methods are familiar from the internet REST services.
    The contract contains the outgoing data and URI in JSON format,
    and the response and possible errors are returned using the callback interface:

    */

    private final String SCHEME_PREFIX = "suunto://";

    public void getDeviceInfo(){
        //String uri = SCHEME_PREFIX + device.connectedSerial + "/Info";
        String uri = SCHEME_PREFIX + hearRateDeviceSerial + "/Info";
        //final Context ctx = currentContext;
        mMds.get(uri, null, new MdsResponseListener() {
            /**
             * Called when Mds operation has been succesfully finished
             *
             * @param data Response in String format
             */
            @Override
            public void onSuccess(String data) {
                Log.i(LOG_TAG, "Device " + hearRateDeviceSerial+ " /info request succesful: " + data);
                // Display info in alert dialog
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Device info:")
                        .setMessage(s)
                        .show();
                */
                // The onSuccess() gets the result code and the returned data as a JSON string.

            }

            /**
             * Called when Mds operation failed for any reason
             *
             * @param e Object containing the error
             */
            @Override
            public void onError(MdsException e) {
                Log.e(LOG_TAG, "Device " + hearRateDeviceSerial + " /info returned error: " + e);
            }
        });
    }


    /*
    Subscribe

    The fifth method subscribe() is a special one in the Movesense system.
    Using that method the application can subscribe to receive notifications from a service that
    provides them (such as accelerometer in the Movesense sensor).

    To subscribe to notifications one can call subscribe as follows:

    MdsSubscription subscription =
            mds.subscribe("suunto://MDS/EventListener",
                "{\"Uri\": \"" + deviceSerialNumber + "/" + uriToSubscribeTo + "\"}",
                callback); // MdsNotificationListener callback class

    E.g. if the uriToSubsribeTo is set to Meas/Acc/13,
    the application would receive the updates from the Movesense sensor's accelerometer
    measurements with 13Hz sampling rate.
    The notifications (in JSON formatted string) and errors are returned to the callback
    object given in the call:

    public interface MdsNotificationListener {
        void onNotification(String data);

        void onError(MdsException error);
    }

    NOTE: After the application is done with the notifications it should call the unsubscribe()
    methods in the MdsSubscription object that was returned from the call to subscribe().
    */
    MdsSubscription subscription;
    public void subscribeToHeartECGNotifications(){
        Log.d("subscribeToHeartECGNot", "doing..");
        //String uri = SCHEME_PREFIX + hearRateDeviceSerial + "/Meas/ECG";
        String uriToSubscribeTo = "Meas/ECG/125";//Meas/Acc/13
        subscription =
                mMds.subscribe("suunto://MDS/EventListener",
                        "{\"Uri\": \"" + hearRateDeviceSerial + "/" + uriToSubscribeTo + "\"}",
                        this); // MdsNotificationListener callback class




    }

    public void unsubscribeToHeartECGNotifications(){
        Log.d("unsubscribeToHrtECGNot", "doing..");

    }


    @Override
    public void onNotification(String s) {
        Log.e("ECGNOT", s);

    }

    @Override
    public void onError(MdsException e) {

    }
}
