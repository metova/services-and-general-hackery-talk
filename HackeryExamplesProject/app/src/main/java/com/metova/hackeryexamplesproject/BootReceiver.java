package com.metova.hackeryexamplesproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class BootReceiver extends WakefulBroadcastReceiver {

    public static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive");

        doWork();
        completeWakefulIntent(intent);
    }

    private void doWork() {

        Log.d(TAG, "doWork");

        // TODO: Any work needed to be done (ex: set alarms, start services, etc)
    }
}
