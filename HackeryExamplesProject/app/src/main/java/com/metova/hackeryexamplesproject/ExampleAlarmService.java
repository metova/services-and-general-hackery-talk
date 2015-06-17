package com.metova.hackeryexamplesproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ExampleAlarmService extends Service {

    public static final String TAG = ExampleAlarmService.class.getSimpleName();

    private Intent mIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");

        mIntent = intent;

        doWork();

        return START_STICKY;
    }

    private void doWork() {

        Log.i(TAG, "doWork");
        finish();
    }

    private void finish() {

        Log.i(TAG, "finish");

        ExampleAlarmReceiver.completeWakefulIntent(mIntent);    // release our wakelock
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
