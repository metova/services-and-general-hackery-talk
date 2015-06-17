package com.metova.hackeryexamplesproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ExampleSlaveService extends Service {

    public static final String TAG = ExampleMasterService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");

        doWork();

        return START_STICKY;
    }

    private void doWork() {

        Log.i(TAG, "doWork");
        finish();
    }

    private void finish() {

        Log.i(TAG, "finish");
        ExampleMasterService.countDownLatch();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
