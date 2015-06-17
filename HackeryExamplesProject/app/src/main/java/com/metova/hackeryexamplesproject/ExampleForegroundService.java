package com.metova.hackeryexamplesproject;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class ExampleForegroundService extends Service {

    public static final String TAG = ExampleService.class.getSimpleName();
    public static final String ACTION_STOP = "action_stop";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");

        if(intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_STOP)) {

            Log.i(TAG, ACTION_STOP);
            stopSelf();
            return START_STICKY;
        }

        startForeground();
        doWork();

        return START_STICKY;
    }

    private void startForeground() {

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Foreground service running")
                .setContentTitle("ExampleForegroundService");

        Notification notification;
        if(Build.VERSION.SDK_INT >= 16) {

            notification = builder.build();
        }
        else {

            notification = builder.getNotification();
        }

        startForeground(1, notification);
    }

    private void stopForeground() {

        stopForeground(true);
    }

    private void doWork() {

        Log.i(TAG, "doWork");


        // TODO: Perform long-running work


        stopForeground();

        finish();
    }

    private void finish() {

        Log.i(TAG, "finish");
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
