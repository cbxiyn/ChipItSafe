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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class NFCFragment extends Fragment {

    public static final String TAG = "NFCFragment";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public LinearLayout linearLayout;

    public NFCFragment() {
        // Required empty public constructor
    }

    public static NFCFragment newInstance() {
        NFCFragment fragment = new NFCFragment();;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nfc, container, false);
        linearLayout = (LinearLayout) v.findViewById(R.id.fragment_nfc_linearLayout);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void handleIntent(Intent intent) {
        String action = intent.getAction();

        Log.d(TAG,"handleIntent: "+action);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            //Vanessa enters here
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(linearLayout).execute(tag);

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
                    new NdefReaderTask(linearLayout).execute(tag);
                    break;
                }
            }
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("file_name.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }



}

class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private static final String TAG = "NdefReaderTask";
    private LinearLayout view;

    public NdefReaderTask(LinearLayout l){
        view = l;
    }

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
            Log.d(TAG,"NFC result:"+result);
            try {

                JSONObject obj = new JSONObject(result);
                populateUI(obj);

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            }
        }
    }


    private void populateUI(JSONObject jsonObject){

        TextView nameTextView = (TextView)view.findViewById(R.id.nfc_content_name);
        TextView lastnameTextView = (TextView)view.findViewById(R.id.nfc_content_lastname);
        TextView fcodeTextView = (TextView)view.findViewById(R.id.nfc_content_fiscalCode);
        TextView bloodTextView = (TextView)view.findViewById(R.id.nfc_content_blood);
        TextView birthDateTextView = (TextView)view.findViewById(R.id.nfc_content_birthDate);
        TextView allergiesTextView = (TextView)view.findViewById(R.id.nfc_content_allergies);
        TextView deseasesTextView = (TextView)view.findViewById(R.id.nfc_content_deseas);
        TextView therapyTextView = (TextView)view.findViewById(R.id.nfc_content_threapy);

        try {
            nameTextView.setText(jsonObject.getString("Name"));
            lastnameTextView.setText(jsonObject.getString("Lastname"));
            fcodeTextView.setText(jsonObject.getString("FiscalCode"));
            bloodTextView.setText(jsonObject.getString("BloodGroup"));
            birthDateTextView.setText(jsonObject.getString("BithDate"));
            allergiesTextView.setText(jsonObject.getString("Allergies"));
            deseasesTextView.setText(jsonObject.getString("Relevent_deseases"));
            therapyTextView.setText(jsonObject.getString("Therapy"));

        } catch (JSONException e) {
            Log.e(TAG, "PopulateUI"+e.getMessage());
        }


    }
}
