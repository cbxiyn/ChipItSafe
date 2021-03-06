package com.reply.hackaton.biotech.chipitsafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import com.reply.hackaton.biothech.chipitsafe.tools.SimulationConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

interface OnNFCCompleted{
    void onNFCCompleted(String result);
}
public class NFCFragment extends Fragment implements OnNFCCompleted {


    public static final String TAG = "NFCFragment";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public LinearLayout linearLayout;
    private Intent pendingIntent = null;

    public NFCFragment() {}

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
        Log.d(TAG,"OnCreateView");
        View v = inflater.inflate(R.layout.fragment_nfc, container, false);
        linearLayout = (LinearLayout) v.findViewById(R.id.fragment_nfc_linearLayout);
        populateUI(getSharedPreferences());

        if (pendingIntent != null){
            Log.d(TAG,"pendingIntent NOT null");
            handleIntent(pendingIntent);
            pendingIntent = null;
        }


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
                if(linearLayout!=null) {
                    new NdefReaderTask(this).execute(tag);
                } else {
                    pendingIntent = intent;
                }

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
                    new NdefReaderTask(this).execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void onNFCCompleted(String result) {
        Log.d(TAG,"NFC result:"+result);
        try {
            JSONObject obj = new JSONObject(result);
            populateUI(obj);
            putInSharedPreferences(result);
        } catch (Throwable t) {
            Log.e(TAG, "Could not parse malformed JSON: \"" + result + "\"");
        }
    }

    private void populateUI(JSONObject jsonObject){

        TextView nameTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_name);
        TextView lastnameTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_lastname);
        TextView fcodeTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_fiscalCode);
        TextView bloodTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_blood);
        TextView birthDateTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_birthDate);
        TextView allergiesTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_allergies);
        TextView deseasesTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_deseas);
        TextView therapyTextView = (TextView)linearLayout.findViewById(R.id.nfc_content_threapy);

        try {
            nameTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_NAME));
            lastnameTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_LASTNAME));
            fcodeTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_FCODE));
            bloodTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_BLOODGROUP));
            birthDateTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_BIRTH_DATE));
            allergiesTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_ALLERGIES));
            deseasesTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_DEASEASES));
            therapyTextView.setText(jsonObject.getString(SimulationConstants.NFC_KEY_THERAPY));

        } catch (JSONException e) {
            Log.e(TAG, "PopulateUI"+e.getMessage());
        }


    }

    private void putInSharedPreferences(String result){
        SharedPreferences pref = this.getContext().getSharedPreferences(SimulationConstants.SHARED_PREFERNCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        try {
            JSONObject jsonObject = new JSONObject(result);
            editor.putString(SimulationConstants.NFC_KEY_NAME, jsonObject.getString(SimulationConstants.NFC_KEY_NAME));
            editor.putString(SimulationConstants.NFC_KEY_LASTNAME, jsonObject.getString(SimulationConstants.NFC_KEY_LASTNAME));
            editor.putString(SimulationConstants.NFC_KEY_FCODE, jsonObject.getString(SimulationConstants.NFC_KEY_FCODE));
            editor.putString(SimulationConstants.NFC_KEY_BLOODGROUP, jsonObject.getString(SimulationConstants.NFC_KEY_BLOODGROUP));
            editor.putString(SimulationConstants.NFC_KEY_BIRTH_DATE, jsonObject.getString(SimulationConstants.NFC_KEY_BIRTH_DATE));
            editor.putString(SimulationConstants.NFC_KEY_ALLERGIES, jsonObject.getString(SimulationConstants.NFC_KEY_ALLERGIES));
            editor.putString(SimulationConstants.NFC_KEY_DEASEASES, jsonObject.getString(SimulationConstants.NFC_KEY_DEASEASES));
            editor.putString(SimulationConstants.NFC_KEY_THERAPY, jsonObject.getString(SimulationConstants.NFC_KEY_THERAPY));
            editor.apply();

        } catch (Exception e){
            Log.e(TAG, "putInSharedPreferences"+e.getMessage());
        }

    }

    private JSONObject getSharedPreferences(){

        SharedPreferences pref = this.getContext().getSharedPreferences(SimulationConstants.SHARED_PREFERNCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(SimulationConstants.NFC_KEY_NAME,pref.getString(SimulationConstants.NFC_KEY_NAME,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_LASTNAME,pref.getString(SimulationConstants.NFC_KEY_LASTNAME,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_FCODE,pref.getString(SimulationConstants.NFC_KEY_FCODE,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_BLOODGROUP,pref.getString(SimulationConstants.NFC_KEY_BLOODGROUP,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_BIRTH_DATE,pref.getString(SimulationConstants.NFC_KEY_BIRTH_DATE,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_ALLERGIES,pref.getString(SimulationConstants.NFC_KEY_ALLERGIES,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_DEASEASES,pref.getString(SimulationConstants.NFC_KEY_DEASEASES,"- - -"));
            jsonObject.put(SimulationConstants.NFC_KEY_THERAPY,pref.getString(SimulationConstants.NFC_KEY_THERAPY,"- - -"));
        } catch (Exception e){
            Log.e(TAG, "getSharedPreferences"+e.getMessage());

        }
        return jsonObject;
    }
}

class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private static final String TAG = "NdefReaderTask";
    private OnNFCCompleted listener;

    public NdefReaderTask(OnNFCCompleted l){
        listener = l;
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
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            listener.onNFCCompleted(result);
        }
    }

}
