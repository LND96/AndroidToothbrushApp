package dk.au.st7bac.toothbrushapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    // ui widgets
    private Button startActivityButton;
    private EditText sensorIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());

        // if app has been run before, start MainActivity
        if (!sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            startMainActivity();
        } else {
            startActivityButton = findViewById(R.id.buttonStartActivity);
            sensorIdEditText = findViewById(R.id.editTextSensorId);

            startActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sensorId = sensorIdEditText.getText().toString();
                    if (sensorId.isEmpty()) {
                        Toast.makeText(StartActivity.this, R.string.sensorIdEmpty,
                                Toast.LENGTH_LONG).show();
                    } else {
                        sharedPreferences.edit().putString(Constants.SETTING_SENSOR_ID_KEY,
                                sensorId).apply();
                        startMainActivity();
                    }
                }
            });
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
    }
}