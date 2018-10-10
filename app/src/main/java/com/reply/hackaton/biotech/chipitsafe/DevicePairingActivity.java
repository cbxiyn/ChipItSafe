package com.reply.hackaton.biotech.chipitsafe;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicePairingActivity extends AppCompatActivity
        implements BluetoothConnectionManager.BluetoothConnectionManagerListener,
        BluetoothDeviceAdapter.OnBLEDeviceClickListener
         {

    private String LOG_TAG = "DevicePairingActivity";

    DevicePairingActivity startActivityContext;
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    private BluetoothConnectionManager bluetoothConnManager;


    RecyclerView recyclerView;
    // This is the Adapter being used to display the list's data
    // SimpleCursorAdapter mAdapter;
    private BluetoothDeviceAdapter bluetoothDevAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startActivityContext = this;

        recyclerView = (RecyclerView) findViewById(R.id.listViewDemo);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("BluetoothConnection", "onclick");
                bluetoothConnManager.startBleScan();
                bluetoothDevAdapter = new BluetoothDeviceAdapter( bluetoothConnManager.scannedDevices, startActivityContext);
                recyclerView.setAdapter(bluetoothDevAdapter);
            }
        });



        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+ Permission APIs
            askPermissions();
        }
    }


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startActivityContext = this;
        //heartManager = new HeartRateManager(this);
        //heartManager.initMds();

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView = (ListView) findViewById(R.id.listViewDemo);
        listView.setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        Button clickButton = (Button) findViewById(R.id.scanButton);
        clickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("BluetoothConnection", "onclick");
                bluetoothConnManager.startBleScan();
                String [] array = {"Antonio","Giovanni","Michele","Giuseppe", "Leonardo", "Alessandro"};
                //arrayAdapter = new ArrayAdapter<String>( startActivityContext, android.R.layout.simple_list_item_1, bluetoothConnManager.scannedDevNames);
                arrayAdapter = new ArrayAdapter<String>(startActivityContext, R.layout.row, R.id.textViewList, array);
                listView.setAdapter(mAdapter);

                bluetoothConnManager.setAdapterForNewDeviceNotification(arrayAdapter);
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+ Permission APIs
            fuckMarshMallow();
        }

    }
    */



    @Override
    protected void onResume() {
        super.onResume();

        bluetoothConnManager = BluetoothConnectionManager.instanceOfBluetoothConnectionManager(this, this);

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothConnManager.getmBluetoothAdapter() == null || !bluetoothConnManager.getmBluetoothAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);


                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                        ) {
                    // All Permissions Granted

                    // Permission Denied
                    Toast.makeText(DevicePairingActivity.this, "All Permission GRANTED !! Thank You :)", Toast.LENGTH_SHORT)
                            .show();


                } else {
                    // Permission Denied
                    Toast.makeText(DevicePairingActivity.this, "One or More Permissions are DENIED Exiting App :(", Toast.LENGTH_SHORT)
                            .show();

                    finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void askPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Show Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                String message = "App need access to " + permissionsNeeded.get(0);

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        Toast.makeText(DevicePairingActivity.this, "No new Permission Required- Launching App .You are Awesome!!", Toast.LENGTH_SHORT)
                .show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DevicePairingActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onNewDeviceFound(BluetoothDevice bluetoothDevice) {
        if(bluetoothDevAdapter == null)
            bluetoothDevAdapter = new BluetoothDeviceAdapter(bluetoothConnManager.scannedDevices, this);

        bluetoothDevAdapter.addElement(bluetoothDevice);


    }

    @Override
    public void onItemClick(BluetoothDevice dev) {

        HeartRateManager.instanceOfHeartRateManager(null).setHeartRateDeviceAttemptingToConnectTo(dev);
        Intent myIntent = new Intent(this, StartActivity.class);
        startActivity(myIntent);

    }


}
