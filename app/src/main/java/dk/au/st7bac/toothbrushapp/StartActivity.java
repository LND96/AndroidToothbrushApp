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

import dk.au.st7bac.toothbrushapp.Fragments.SettingsFragment;
import dk.au.st7bac.toothbrushapp.Login.LoginActivity;
import dk.au.st7bac.toothbrushapp.Login.RegisterActivity;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private Button startActivityButton;
    private EditText sensorIdEditText;


    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (savedInstanceState == null) {
            settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment, settingsFragment, "SETTINGS_FRAG").hide(settingsFragment)
                    .commit();
        } else {
            settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SETTINGS_FRAG");
            if (settingsFragment == null) {
                settingsFragment = new SettingsFragment();
            }
        }


        //Do this the first time the app is installed
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());
        if (!sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }


        startActivityButton = findViewById(R.id.buttonStartActivity);
        sensorIdEditText = findViewById(R.id.editTextSensorId);



        startActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sensorId = sensorIdEditText.getText().toString();
                if (sensorId.isEmpty()) {
                    Toast.makeText(StartActivity.this, "Er tom", Toast.LENGTH_SHORT).show(); //hardcoadet værdi!
                }
                else {

                    sharedPreferences.edit().putString(Constants.SETTING_SENSOR_ID_KEY, sensorId).apply();

                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }
}