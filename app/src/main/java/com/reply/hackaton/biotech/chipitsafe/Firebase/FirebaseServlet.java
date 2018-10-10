package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.content.Context;

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

    public FirebaseServlet()
    {

    }
    public void updateUserAppToken(JSONObject data,Context context)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://us-central1-chipitsafe.cloudfunctions.net/updateUserAppToken";

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,data,
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
        queue.add(jsonobj);
    }
}
