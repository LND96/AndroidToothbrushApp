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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dk.au.st7bac.toothbrushapp.Model.Repository;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

//test

// DENNE KLASSE ER INSPIRERET AF MAD LEKTION 6
public class WebApiService {

    private static final String TAG = "WebApiService";

    private RequestQueue queue;

    private Repository repository;

    public WebApiService() {
        repository = Repository.getInstance();
    }

    public void getTbData() {
        String baseUrl = "https://dmjljzkaec.execute-api.eu-west-1.amazonaws.com/default/tbapi/v1";
        String url = baseUrl + "/system/tm/c4d1574b-d1ce-43da-84df-f54fe5e09ba9?since=20210922052144638405&limit=10"; // hardcoded værdi
        sendRequestForTbData(url);
    }

    private void sendRequestForTbData(String url) {
        if (queue == null) { // overvej singleton pattern i stedet for lazy loading
            queue = Volley.newRequestQueue(ToothbrushApp.getAppContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // onResponse er synkroniseret til UI tråden, så vi må gerne opdatere UI her
                Log.d(TAG, "onResponse" + response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Fejl kan ske pga. serverfejl, connectionfejl, mm, så her vil man gerne håndtere fejlene på en god måde og fortælle brugeren hvad fejlen er
                Log.e(TAG, "Error getting data", error);
            }
        });

        queue.add(stringRequest);
    }


    private void parseJson(String json) {

        ArrayList<TbData> tbDataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject sysIdObj = jsonArray.getJSONObject(i).getJSONObject("SysId");
                String sysId = sysIdObj.getString("S");
                JSONObject dateTimeObj = jsonArray.getJSONObject(i).getJSONObject("SystemDateTime");
                String dateTime = dateTimeObj.getString("S");

                JSONObject tmDataObj = jsonArray.getJSONObject(i).getJSONObject("TMData");
                String S = tmDataObj.getString("S");
                JSONObject sObj = new JSONObject(S);
                String msgData = sObj.getString("MsgData");
                JSONObject msgDataObj = new JSONObject(msgData);
                int tbVal = msgDataObj.getInt("tbval");
                double tbSecs = msgDataObj.getDouble("tbsecs");

                TbData tbData = new TbData(sysId, dateTime, tbVal, tbSecs);

                tbDataList.add(tbData);
            }
        } catch (JSONException e) {
            e.printStackTrace(); // skal der gøres noget andet ved exception?
        }

        repository.setTbData(tbDataList); // hvad hvis listen er tom?

    }
}
