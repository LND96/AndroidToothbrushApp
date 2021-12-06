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

import dk.au.st7bac.toothbrushapp.Controllers.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Interfaces.IApiRepo;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

// inspiration for ApiRepo: SWMAD-01 Mobile Application Development, lecture 6, spring 2021
public class ApiRepo implements IApiRepo {

    private static final String TAG = "WebApiService";
    private RequestQueue queue;
    private final UpdateDataCtrl updateDataCtrl;
    private String sensorId;
    private final String apiSince;
    private final String apiLimit;
    private final String baseUrl;

    public ApiRepo(UpdateDataCtrl updateDataCtrl, String sensorId, String apiSince, String apiLimit,
                   String baseUrl) {
        this.updateDataCtrl = updateDataCtrl;
        this.sensorId = sensorId;
        this.apiSince = apiSince;
        this.apiLimit = apiLimit;
        this.baseUrl = baseUrl;
    }

    public void setApiSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void getTbData() {
        String url = baseUrl + sensorId + "?since=" + apiSince + "&limit=" + apiLimit;
        sendRequestForTbData(url);
    }

    private void sendRequestForTbData(String url) {
        if (queue == null) {
            queue = Volley.newRequestQueue(ToothbrushApp.getAppContext());
        }

        // get-request for api
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error message due to a number of errors like server error, connection error,
                // wrong sensor id, etc.
                Log.e(TAG, "Error getting data", error);
            }
        });

        queue.add(stringRequest);
    }

    // parses JSON objects to TbData
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

                TbData tbData = new TbData(sysId, tbVal, tbSecs, rawTelemetry, tbHb,
                        LocalDateTime.now(), 0);

               tbDataList.add(tbData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateDataCtrl.updateTbData(tbDataList);
    }
}
