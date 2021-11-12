package dk.au.st7bac.toothbrushapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import dk.au.st7bac.toothbrushapp.MainActivity;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;


//https://www.youtube.com/watch?v=ub4_f6ksxL0
public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Alarm"; //ændre navnet + tilføj til string

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        createChannel();
    }

    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String Message) {
        //Need a pending intent for the activity - go to activity when click on notification - https://www.youtube.com/watch?v=j6kQ9gikU-A&ab_channel=Academind
        Intent resultIntent = new Intent(ToothbrushApp.getAppContext(), MainActivity.class);
        PendingIntent resultPentingIntent = PendingIntent.getActivity(ToothbrushApp.getAppContext(), 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT); //Flag: if the intent is already existing, it will update it with new information.

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(Message)
                .setSmallIcon(R.drawable.toothbrush_icon)
                .setAutoCancel(true) //when clik on notification and open the activity, the notification is disapear
                .setContentIntent(resultPentingIntent);
    }
}
