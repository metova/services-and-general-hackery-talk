package com.metova.hackeryexamplesproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ExampleService extends Service {

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

        new Thread() {

            @Override
            public void run() {

                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                doWork();
            }
        }.start();

        return START_STICKY;
    }

    private void doWork() {

        Log.i(TAG, "doWork");
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
