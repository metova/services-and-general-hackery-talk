package com.metova.hackeryexamplesproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExampleMasterService extends Service {

    public static final String TAG = ExampleMasterService.class.getSimpleName();

    private int latchCount = 0;
    private static CountDownLatch sCountDownLatch;
    private Intent mIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");

        mIntent = intent;

        doWork();

        return START_STICKY;
    }

    private void startSlaves() {

        latchCount = 0;

        latchCount++;
        Intent slave1 = new Intent(this, ExampleSlaveService.class);
        startService(slave1);

        // TODO: Start other slaves here
    }

    private void doWork() {

        Log.i(TAG, "doWork");

        startSlaves();

        Log.i(TAG, "Creating lock: " + latchCount);
        sCountDownLatch = new CountDownLatch(latchCount);

        // Spin a thread to wait for the latch before releasing the wakelock / restarting the timer
        new Thread() {

            @Override
            public void run() {

                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                super.run();

                try {

                    boolean didCountDown = sCountDownLatch.await(60, TimeUnit.SECONDS); // If we don't return after a minute, we are possibly stuck

                    // await returns true if count reached 0, false if it timed out
                    if(!didCountDown) {

                        Log.w(TAG, "Master timed out while waiting for slaves");
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finish();
            }
        }.start();
    }

    public static void countDownLatch() {

        Log.i(TAG, "countDownLatch, null: " + Boolean.toString(sCountDownLatch == null));

        if(sCountDownLatch != null) { sCountDownLatch.countDown(); }
    }

    private void finish() {

        Log.i(TAG, "finish");

        // TODO: Set the master alarm again if you want this background work to continue

        ExampleAlarmReceiver.completeWakefulIntent(mIntent);    // release our wakelock
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
