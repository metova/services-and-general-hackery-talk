package com.metova.hackeryexamplesproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class ExampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    @Override
    protected void onStart() {

        Button start = (Button) findViewById(R.id.start_alarm);

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
