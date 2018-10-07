package com.reply.hackaton.biotech.chipitsafe;

import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

public class StartActivity extends AppCompatActivity {



    private final static int REQUEST_ENABLE_BT = 1;

    private BluetoothConnectionManager bluetoothConnManager;
    private HeartRateManager heartManager;
    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bluetoothConnManager = new BluetoothConnectionManager(this);
        //heartManager = new HeartRateManager(this);
        //heartManager.initMds();

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        ListView listView = (ListView) findViewById(R.id.devices_list_view);
        listView.setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);


        // For the cursor adapter, specify which columns go into which views
        //String[] fromColumns = {"dev name", "mac", "serial"};
        //int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        //mAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1, null,fromColumns, toViews, 0);
        //arrayAdapter = new ArrayAdapter<String>(this,listView,null/*R.id.textView*/,bluetoothConnManager.scannedDevNames);
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                bluetoothConnManager.scannedDevNames );
        listView.setAdapter(mAdapter);
        //setListAdapter(mAdapter);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        //getLoaderManager().initLoader(0, null, this);

        //findViewById(R.id.scanButton)

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is available on the device and it is enabled. If not,
// displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothConnManager.getmBluetoothAdapter() == null || !bluetoothConnManager.getmBluetoothAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    public void onClick(View view) {
        bluetoothConnManager.startBleScan();
    }

    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
    */

}
