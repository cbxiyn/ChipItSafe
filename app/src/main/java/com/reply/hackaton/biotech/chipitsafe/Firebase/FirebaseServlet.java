package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;


public class FirebaseServlet {
    private static final String TAG = "HTTPRequest";
    private String projectUrl = "https://us-central1-chipitsafe.cloudfunctions.net/";

    public FirebaseServlet() {

    }

    /**
     * This method sends a POST request to the "updateUserAppToken" Firebase cloud function with a
     * Firebase Application ID.
     *
     * @param data    An JSONObject containing a uid and userAppToken field
     * @param context The activity that is calling this function
     */
    public void updateUserAppToken(JSONObject data, Context context) {
        String cloudFunction = "updateUserAppToken";

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, projectUrl + cloudFunction, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Check your bookmarks
                    }
                }
        );
        RequestSingleton.getInstance(context).addToRequestQueue(jsonobj);
    }
}
