package com.no_texting_while_driving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartupIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final String PREFS_NAME = "OnOffPrefs";
        final SharedPreferences settingPrefs = context.getSharedPreferences(PREFS_NAME, 0);

        if (settingPrefs.getBoolean("ON_OFF", false)) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction("com.no_texting_while_driving.BackgroundService");
            context.startService(serviceIntent);
        }

    }
}