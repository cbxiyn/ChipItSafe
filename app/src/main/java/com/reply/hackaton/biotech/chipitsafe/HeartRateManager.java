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

    /*
    MOVESENSE APIs
    https://bitbucket.org/suunto/movesense-device-lib
     */


    private String LOG_TAG = "HeartRateManager";

    private Context currentContext;

    private Mds mMds;


    private BluetoothDevice heartRateDevice;
    private String hearRateDeviceSerial;

    public void confirmHeartRateDevice(){
        heartRateDevice = deviceAttemptingToConnectTo;
    }

    public void setHeartRateDeviceAttemptingToConnectTo(BluetoothDevice dev){
        deviceAttemptingToConnectTo = dev;
    }

    public BluetoothDevice getDeviceAttemptingToConnectTo() {
        return deviceAttemptingToConnectTo;
    }

    public void setHeartRateDeviceSerial(String serialNr){
        hearRateDeviceSerial = serialNr;
    }

    private static HeartRateManager heartRateManager = null;

    private HeartRateManager(){

    }

    public static HeartRateManager instanceOfHeartRateManager() {
        if(heartRateManager == null) heartRateManager = new HeartRateManager();

        return heartRateManager;
    }

    public void initMds(Context c) {
        currentContext = c;
        mMds = Mds.builder().build(currentContext);

    }


    private BluetoothDevice deviceAttemptingToConnectTo=null;

    public boolean isConnectedToDevice = false;

    public void connectToDevice(MdsConnectionListener listener){
        if(deviceAttemptingToConnectTo == null)
            Log.e(LOG_TAG, "null deviceAttemptingToConnectTo reference");
        /*if (!device.isConnected()) { */
            //RxBleDevice bleDevice = getBleClient().getBleDevice(device.macAddress);
            Log.i(LOG_TAG, "Connecting to BLE device: " +  deviceAttemptingToConnectTo.getAddress() /*bleDevice.getMacAddress()*/);

            mMds.connect(  deviceAttemptingToConnectTo.getAddress(), listener);
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

    public void getDeviceInfo(MdsResponseListener listener){
        //String uri = SCHEME_PREFIX + device.connectedSerial + "/Info";
        String uri = SCHEME_PREFIX + hearRateDeviceSerial + "/Info";
        //final Context ctx = currentContext;
        mMds.get(uri, null, listener);
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
    MdsSubscription ECGsubscription;
    public void subscribeToECGNotifications(MdsNotificationListener listener){
        Log.d("subscribeToHeartECGNot", "doing..");
        //String uri = SCHEME_PREFIX + hearRateDeviceSerial + "/Meas/ECG";
        /*
        Apparently ECG, like Magn and Acc but unlike HR, needs to have the sample rate included in the path
        in order to return data. I fixed the issue by changing the API path from Meas/ECG to Meas/ECG/125,
        125 being the default sample rate that the android app uses.
         */
        String uriToSubscribeTo = "Meas/ECG/125";//Meas/Acc/13
        ECGsubscription =
                mMds.subscribe("suunto://MDS/EventListener",
                        "{\"Uri\": \"" + hearRateDeviceSerial + "/" + uriToSubscribeTo + "\"}",
                        listener); // MdsNotificationListener callback class




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




    MdsSubscription HRsubscription;
    public void subscribeToHeartRateNotifications(MdsNotificationListener listener){
        Log.d("subscribeToHeartRateNot", "doing..");
        String uriToSubscribeTo = "Meas/HR";//Meas/Acc/13
        ECGsubscription =
                mMds.subscribe("suunto://MDS/EventListener",
                        "{\"Uri\": \"" + hearRateDeviceSerial + "/" + uriToSubscribeTo + "\"}",
                        listener); // MdsNotificationListener callback class




    }

    public void unsubscribeAll(){
        if(HRsubscription!=null)HRsubscription.unsubscribe();
        if(ECGsubscription!=null)ECGsubscription.unsubscribe();
    }
}
