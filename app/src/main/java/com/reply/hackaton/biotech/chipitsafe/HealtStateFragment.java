package com.reply.hackaton.biotech.chipitsafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsResponseListener;
import com.reply.hackaton.biotech.chipitsafe.graphics.MyHeartShape;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class HealtStateFragment extends Fragment implements MdsResponseListener, MdsNotificationListener {

    static public final String TAG = "HealtStateFragment";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private HeartRateManager heartRateManager;
    private NfcAdapter mNfcAdapter;

    private TextView deviceNameTV;
    private ProgressBar progressBar;
    private TextView parametersTV;
    private MyHeartShape heartCustomView;

    public HealtStateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HealtStateFragment newInstance() {
        HealtStateFragment fragment = new HealtStateFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        heartRateManager = HeartRateManager.instanceOfHeartRateManager();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(getActivity(), "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Log.d(TAG,"NFC is disabled");
        } else {
            Log.d(TAG,"NFC is enabled");
        }

        //handleIntent(getIntent());

    }

    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_healt_state, container, false);
        deviceNameTV = (TextView) v.findViewById(R.id.deviceName);
        parametersTV = (TextView) v.findViewById(R.id.hearthParamsTV);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        heartCustomView = new MyHeartShape(getActivity());
        heartCustomView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
        FrameLayout innerHeartFrameLayout = (FrameLayout) v.findViewById(R.id.heartLayout);
        innerHeartFrameLayout.addView(heartCustomView);

        //
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        setupForegroundDispatch(getActivity(),mNfcAdapter);

    }

    @Override
    public void onPause() {
        stopForegroundDispatch(getActivity(),mNfcAdapter);
        super.onPause();
    }


    private void getDeviceInfo(){}

    public ProgressBar getProgressBar(){
        progressBar.setVisibility(View.VISIBLE); //to show
        return progressBar;
    }

    public void startDisplayingContents(){
        Toast.makeText(getActivity(), "LOADING CONTENTS.....", Toast.LENGTH_SHORT).show();
        updateDeviceInfo();
    }

    public void updateDeviceInfo(){

        heartRateManager.getDeviceInfo(this);
        heartRateManager.subscribeToHeartRateNotifications(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    /**
     * Called when Mds operation has been succesfully finished
     *
     * @param data Response in String format
     */
    @Override
    public void onSuccess(String data) {
        Log.i(TAG, "Device /info request successful: " + data);
        // Display info in alert dialog

        // The onSuccess() gets the result code and the returned data as a JSON string.
        try {
            Log.e("DEVICEINFO", data);
            JSONObject jObj = new JSONObject(data);
            String devName = jObj.getJSONObject("Content").getString("productName");
            Log.e("DEVICEINFO", devName);
            deviceNameTV.setText(devName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when Mds operation failed for any reason
     *
     * @param e Object containing the error
     */
    @Override
    public void onError(MdsException e) {
        Log.e(TAG, "Device /info returned error: " + e);
    }


    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        //adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }




    // MDS SUBSCRIPTIONS
    @Override
    public void onNotification(String s) {
        Log.e("BPS", s);
        try {

            JSONObject hrData = new JSONObject(s).getJSONObject("Body");
            int average = hrData.getInt("average");
            parametersTV.setText("Average: "+average+"\n");
            // Retrieve number array from JSON object.
            JSONArray array = hrData.optJSONArray("rrData");

            // Deal with the case of a non-array value.
            if (array == null) {
                /*...*/
            }

            // Create an int array to accomodate the numbers.
            int[] hrSamples = new int[array.length()];

            // Extract numbers from JSON array.
            for (int i = 0; i < array.length(); ++i) {
                hrSamples[i] = array.optInt(i);
                parametersTV.setText(parametersTV.getText().toString() + hrSamples[i] + "-");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        //adapter.disableForegroundDispatch(activity);
    }

}

class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private static final String TAG = "NdefReaderTask";
    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            Log.e(TAG, "NDEF is not supported by this Tag");
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            //mTextView.setText("Read content: " + result);
        }
    }
}