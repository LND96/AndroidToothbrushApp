package dk.au.st7bac.toothbrushapp.Repositories;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

//test

// DENNE KLASSE ER INSPIRERET AF MAD LEKTION 6
public class ApiRepo {

    private static final String TAG = "WebApiService";

    private RequestQueue queue;

    private UpdateDataCtrl updateDataCtrl;

    public ApiRepo(UpdateDataCtrl updateDataCtrl) {
        this.updateDataCtrl = updateDataCtrl;
    }

    public void getTbData() {
        String baseUrl = "https://dmjljzkaec.execute-api.eu-west-1.amazonaws.com/default/tbapi/v1";
        String url = baseUrl + "/system/tm/c4d1574b-d1ce-43da-84df-f54fe5e09ba9?since=20211013&limit=100"; // hardcoded værdi - skal sendes med metode
        sendRequestForTbData(url);
    }

    private void sendRequestForTbData(String url) {
        if (queue == null) { // overvej singleton pattern i stedet for lazy loading
            queue = Volley.newRequestQueue(ToothbrushApp.getAppContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // onResponse er synkroniseret til UI tråden, så vi må gerne opdatere UI her
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

                JSONObject tmDataObj = jsonArray.getJSONObject(i).getJSONObject("TMData");
                String S = tmDataObj.getString("S");
                JSONObject sObj = new JSONObject(S);
                String msgData = sObj.getString("MsgData");
                JSONObject msgDataObj = new JSONObject(msgData);
                int tbVal = msgDataObj.getInt("tbval");
                double tbSecs = msgDataObj.getDouble("tbsecs");
                String rawTelemetry = msgDataObj.getString("RawTelemetry");
                int tbHb = msgDataObj.getInt("tbhb");


                TbData tbData = new TbData(sysId, tbVal, tbSecs, rawTelemetry, tbHb, LocalDateTime.now(), 0); // LocalDateTime kræver API level 26

               tbDataList.add(tbData);
            }
        } catch (JSONException e) {
            e.printStackTrace(); // skal der gøres noget andet ved exception?
        }

        updateDataCtrl.updateTbData(tbDataList);                      // hvad hvis listen er tom?

    }
}
