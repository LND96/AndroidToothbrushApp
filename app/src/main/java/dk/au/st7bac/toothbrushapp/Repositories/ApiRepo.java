package dk.au.st7bac.toothbrushapp.Repositories;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

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
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

//test

// DENNE KLASSE ER INSPIRERET AF MAD LEKTION 6
public class ApiRepo {

    // constants
    private static final String TAG = "WebApiService";

    private RequestQueue queue;

    private final UpdateDataCtrl updateDataCtrl;

    private String sensorId;
    private String apiSince;
    private String apiLimit;

    private String sensorIDText;

    public ApiRepo(UpdateDataCtrl updateDataCtrl, String sensorId, String apiSince, String apiLimit) {
        this.updateDataCtrl = updateDataCtrl;
        this.sensorId = sensorId;
        this.apiSince = apiSince;
        this.apiLimit = apiLimit;
    }

    public void setApiLimit(String apiLimit) {
        this.apiLimit = apiLimit;
    }

    public void setApiSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void getTbData() {

        String baseUrl = "https://dmjljzkaec.execute-api.eu-west-1.amazonaws.com/default/tbapi/v1/system/tm/";
        String url = baseUrl + sensorId + "?since=" + apiSince + "&limit=" + apiLimit;
        sendRequestForTbData(url);
    }

    private void sendRequestForTbData(String url) {
        if (queue == null) { // overvej singleton pattern i stedet for lazy loading
            queue = Volley.newRequestQueue(ToothbrushApp.getAppContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // onResponse er synkroniseret til UI tråden, så vi må gerne opdatere UI her
                /*
                if (response.equals("[]")) {



                    //pupup window - https://www.youtube.com/watch?v=e3WfylNHHC4
                    AlertDialog.Builder dialogBox = new AlertDialog.Builder();
                    dialogBox.setTitle("Indtast Sensor ID");

                    final EditText sensorID = new EditText(ToothbrushApp.getAppContext());
                    sensorID.setInputType(InputType.TYPE_CLASS_TEXT);
                    dialogBox.setView(sensorID);

                    dialogBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sensorIDText =sensorID.getText().toString();
                            if (sensorIDText.isEmpty()) {
                                Toast.makeText(ToothbrushApp.getAppContext(), "Indtast Sensor ID", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ToothbrushApp.getAppContext(), "Sensor ID er: " + sensorIDText, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    dialogBox.show();
                } else {

                }

                 */
                parseJson(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Fejl kan ske pga. serverfejl, connectionfejl, mm, så her vil man gerne håndtere fejlene på en god måde og fortælle brugeren hvad fejlen er
                // fejlbesked om at sensor id formentlig er forkert
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

        updateDataCtrl.updateTbData(tbDataList); // check op på hvad der sker hvis api er forkert...

    }
}
