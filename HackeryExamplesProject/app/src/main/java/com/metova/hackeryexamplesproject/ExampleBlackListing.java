package com.metova.hackeryexamplesproject;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

public class ExampleBlackListing extends Service {

    public static final String TAG = ExampleBlackListing.class.getSimpleName();

    private String[] blocked = {"com.metova.skycop", "com.bookgrabbr.app"};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {

            @Override
            public void run() {

                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                try {

                    checkWhiteList();
                    finish();
                }
                catch (Exception e) {
                    Log.e(TAG, "Exception trying to run force quit module", e);
                }
            }
        }.start();

        return START_STICKY;
    }

    private void checkWhiteList() {

        Log.i(TAG, "Checking for white-listed applications");

        if(Build.VERSION.SDK_INT >= 21) {

            Log.i(TAG, "New method still needs to be discovered on Lollipop+ devices");
            return;
        }

        for(String block : blocked) {

            String target = getTargetProcess(block);

            if(target != null) {

                // Start the blocking intent
                Intent mainIntent = new Intent(Intent.ACTION_MAIN);
                mainIntent.addCategory(Intent.CATEGORY_HOME);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {

                    startActivity(mainIntent);
                    Log.d(TAG, "Sending intent to block app: " + block);
                } catch (Exception e) {

                    Log.e(TAG, "No activity found for intent", e);
                }
            }
            else {

                Log.i(TAG, "Target app not found!");
            }
        }
    }

    private String getTargetProcess(String toBlock) {

        ActivityManager am = (ActivityManager) this .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if(taskInfo != null && !taskInfo.isEmpty()) {

            if(toBlock.equals(taskInfo.get(0).topActivity.getPackageName())) {

                return toBlock;
            }
        }

        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startAlarm() {

        Log.i(TAG, "startAlarm");

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, ExampleBlackListing.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 300, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 2000, pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 2000, pendingIntent);
        }
    }

    private void finish() {

        Log.i(TAG, "finish");
        startAlarm();
        stopSelf();
    }
}
