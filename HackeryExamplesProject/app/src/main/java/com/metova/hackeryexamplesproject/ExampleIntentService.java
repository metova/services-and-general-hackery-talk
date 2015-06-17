package com.metova.hackeryexamplesproject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ExampleIntentService extends IntentService {

    public static final String TAG = ExampleIntentService.class.getSimpleName();
    public static final String ACTION_ADD = "action_add";
    public static final String EXTRA_ADD_PARAM = "extra_add_param";

    public ExampleIntentService() {

        super("ExampleIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "onHandleIntent");

        if(intent != null && ACTION_ADD.equals(intent.getAction())) {

            doWork(intent.getIntExtra(EXTRA_ADD_PARAM, 0));
        }
    }

    private void doWork(int param) {

        Log.i(TAG, "doWork");

        int sum = 5 + param;
    }
}
