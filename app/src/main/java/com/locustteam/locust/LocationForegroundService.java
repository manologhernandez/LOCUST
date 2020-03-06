package com.locustteam.locust;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LocationForegroundService extends Service {

     String CHANNEL_ID = "LocationService";
     @Override
     public void onCreate() {
          super.onCreate();
     }
     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
          createNotificationChannel();
          Intent notificationIntent = new Intent(this, MainActivity.class);
          PendingIntent pendingIntent = PendingIntent.getActivity(this,
                  0, notificationIntent, 0);
          Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                  .setSmallIcon(R.drawable.location_icon)
                  .setContentTitle("SOS Running")
                  .setContentText("Locust is currently tracking your location in the background.")
                  .setStyle(new NotificationCompat.BigTextStyle()
                          .bigText("Locust is currently tracking your location in the background."))
                  .setContentIntent(pendingIntent)
                  .build();
          startForeground(1, notification);
          //do heavy work on a background thread
          //stopSelf();
          return START_NOT_STICKY;
     }
     @Override
     public void onDestroy() {
          super.onDestroy();
     }
     @Nullable
     @Override
     public IBinder onBind(Intent intent) {
          return null;
     }

     private void createNotificationChannel() {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               NotificationChannel serviceChannel = new NotificationChannel(
                       CHANNEL_ID,
                       "Location Tracking Running",
                       NotificationManager.IMPORTANCE_DEFAULT
               );
               NotificationManager manager = getSystemService(NotificationManager.class);
               manager.createNotificationChannel(serviceChannel);
          }
     }

}
