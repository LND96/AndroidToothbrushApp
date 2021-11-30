package dk.au.st7bac.toothbrushapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.MainActivity;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

// inspiration for NotificationHelper: https://www.youtube.com/watch?v=ub4_f6ksxL0 and https://www.youtube.com/watch?v=j6kQ9gikU-A&ab_channel=Academind
public class NotificationHelper extends ContextWrapper {

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

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

    // fires notification
    public NotificationCompat.Builder getChannelNotification(String title, String Message) {
        // open activity when notification is clicked
        Intent resultIntent = new Intent(ToothbrushApp.getAppContext(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(ToothbrushApp.getAppContext(),
                1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), Constants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(Message)
                .setSmallIcon(R.drawable.toothbrush_icon)
                .setAutoCancel(true) // when notification is clicked and activity opens, the notification should disappear
                .setContentIntent(resultPendingIntent);
    }
}
