package com.locustteam.locust;
/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Manolo Hernandez

Code History
     3/04/20 - file created by Manolo Hernandez
*/
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
      /*
      Method Name: onCreate
      Creation date: 3/6/20
      Purpose: loads the foreground service.
      Calling Arguments: n/a
      Required Files: n/a
      Return Value: n/a
      */
     @Override
     public void onCreate() {
          super.onCreate();
     }

     /*
      Method Name: onStartCommand
      Creation date: 3/6/20
     Purpose: initializes the notification bar for the foreground service
     Calling Arguments: Intent intent, int flags, int startId
     Required Files: n/a
     Return Value: int
      */
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

     /*
      Method Name: onDestroy
      Creation date: 3/6/20
      Purpose: ends the foreground service
      Calling Arguments: n/a
      Required Files: n/a
      Return Value: n/a
      */
     @Override
     public void onDestroy() {
          super.onDestroy();
     }
     @Nullable
     @Override
     public IBinder onBind(Intent intent) {
          return null;
     }

     /*
      Method Name: createNotificationChannel
      Creation date: 2/18/20
      Purpose: creates the notification channel for the foreground service
      Calling Arguments: n/a
      Required Files: n/a
      Return Value: n/a
      */
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
