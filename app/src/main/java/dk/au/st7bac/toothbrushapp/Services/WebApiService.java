package dk.au.st7bac.toothbrushapp.Services;
import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

// DENNE KLASSE ER INSPIRERET AF MAD LEKTION 6
public class WebApiService {

    private static final String TAG = "WebApiService";

    private RequestQueue queue;

    public WebApiService() {

    }

    public void getTbData() {
        String url = "https://dmjljzkaec.execute-api.eu-west-1.amazonaws.com/default/tbapi/v1/system/tm/c4d1574b-d1ce-43da-84df-f54fe5e09ba9?since=20210922052144638405&limit=10"; // hardcoded værdi
        sendRequestForTbData(url);
    }

    private void sendRequestForTbData(String url) {
        if (queue == null) {
            queue = Volley.newRequestQueue(ToothbrushApp.getAppContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse" + response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error getting data", error);
            }
        });

        queue.add(stringRequest);
    }


    private void parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        // her skal tilføjes til tooth brush data
    }
}
