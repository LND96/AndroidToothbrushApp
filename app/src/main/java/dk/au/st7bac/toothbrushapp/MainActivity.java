package dk.au.st7bac.toothbrushapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService();
    }


    private void startService() {
        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        startService(notificationServiceIntent);
    }


    private void stopService() {
        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        stopService(notificationServiceIntent);
    }

    @Override
    protected void onDestroy() {
        stopService();
        super.onDestroy();
    }
}