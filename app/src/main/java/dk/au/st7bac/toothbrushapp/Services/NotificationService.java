package dk.au.st7bac.toothbrushapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.st7bac.toothbrushapp.R;

// a started service runs forever, until it stops

public class NotificationService extends Service {

    //for logging
    private static final String TAG="NotificationService";          //for logging
    public static final String SERVICE_CHANNEL = "serviceChannel";  //Notification channel name
    public static final int NOTIFICATION_ID = 42;                   //Notification id


    ExecutorService executorService; //it is put in the top because it can be a little expensive.

    private boolean started = false;    //flag
    private final int sleepTime = 20000;      //sleep time in milliseconds

    public NotificationService() {

    }

    //Called ones in the live time of the service
    @Override
    public void onCreate() {
        super.onCreate();
    }


    //intent is used to start the service (in MainActivity)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //check for Android version - whether we need to create a notification channel (from Android 0 and up, API 26)
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Tandbørstning mangler", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, SERVICE_CHANNEL);

        //måske ikke det bedste symbol her...
        Notification notification = notificationBuilder
                .setContentTitle("ALARM-TANDBØRSTNING MANGLER")
                .setSmallIcon(R.drawable.toothbrush_icon)
                .build();

        //will promore this Service to af foreground service. It require the notification to be set.
        startForeground(NOTIFICATION_ID, notification);

        //this method starts recursive background work
        doBackgroundStuff();

        //returning START_STICKY will make the Service restart again eventually if it gets killed off.
        return START_STICKY;
    }

    private void doBackgroundStuff() { //do infinit loop
        if(!started) {
            started = true;
            doSleepLoop();
        }
    }


    private void doSleepLoop() { //when the method is called the service shoud sleep.
        if (executorService == null) { //only make one in the lifetime.
            //use executorService (asynch processing)
            executorService = Executors.newSingleThreadExecutor(); //only run in one thread.
        }

        executorService.submit(new Runnable() { //Runnable --> its like a thread
            @Override
            public void run() {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: EROOR", e); //logs at the error level
                }

                if (started) {
                    doSleepLoop();
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Only used when it is a bound service.
    }


}