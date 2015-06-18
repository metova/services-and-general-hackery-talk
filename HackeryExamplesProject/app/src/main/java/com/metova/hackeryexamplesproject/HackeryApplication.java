package com.metova.hackeryexamplesproject;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HackeryApplication extends Application {

    public static final String TAG = HackeryApplication.class.getSimpleName();

    private static Context sBaseContext;

    // UncaughtExceptionHandler variable
    private Thread.UncaughtExceptionHandler defaultUEH;

    // Handler listener
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            Log.e(TAG, "Uncaught crash", ex);

            Intent intent = new Intent(getApplicationBaseContext(), ExampleActivity.class);
            intent.setAction(ExampleActivity.ACTION_CRASH);

            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            intent.putExtra(ExampleActivity.EXTRA_CRASH_INFO, exceptionAsString.substring(0, 200) + "...");

            PendingIntent myActivity = PendingIntent.getActivity(getApplicationBaseContext(),
                    192837, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager;
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, myActivity);

            // TODO: You can even directly log with Crashlytics to keep default functionality (log the exception)

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);

            // re-throw critical exception further to the os (important)
            defaultUEH.uncaughtException(thread, ex);
        }
    };

    @Override
    public void onCreate() {

        super.onCreate();

        sBaseContext = getBaseContext();
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // Setup handler with our UEH
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    public static Context getApplicationBaseContext() {

        return sBaseContext;
    }
}
