package com.example.nowshad.project300.another;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nowshad.project300.R;
import com.example.nowshad.project300.activity.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nowshad on 12/17/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationCompat.Builder notification;
    private static final int uniqueID=123456;
    private static final String TAG=MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG,"From "+remoteMessage.getFrom());

        if(remoteMessage.getData().size()>0)
        {
            Log.i(TAG,"Message details "+remoteMessage.getData());
        }

        if(remoteMessage.getNotification()!=null)
        {
            Log.i(TAG,"Message body "+remoteMessage.getNotification());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String body)
    {
        notification=new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        final Uri notificationSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setSound(notificationSound);
        notification.setTicker("Hey, I'm reached!");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Firebase Notification");
        notification.setContentText(body);

        Intent intent=new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
        intent.putExtra("notificationDetails",body);
        PendingIntent pendingIntent=PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueID,notification.build());

    }
}
