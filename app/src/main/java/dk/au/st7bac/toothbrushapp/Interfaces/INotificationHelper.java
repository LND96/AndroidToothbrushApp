package dk.au.st7bac.toothbrushapp.Interfaces;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

public interface INotificationHelper {

    NotificationManager getManager();

    NotificationCompat.Builder getChannelNotification(String title, String Message);
}
