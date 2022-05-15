package com.example.thebeastnotesofworld.core.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;


public class MyAlarmManager {

    public void setAlarm (Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);

        if (checkIsSetAlarm()) {
            Calendar notifyTime = Calendar.getInstance();
            notifyTime.set(Calendar.HOUR_OF_DAY, 9);
            notifyTime.set(Calendar.MINUTE,0);
            notifyTime.set(Calendar.SECOND,0);

            Intent intent = new Intent(context, MyNotificationReceiver.class);
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC, notifyTime.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
            saveStateAlarm(true);
        }


    }


    private SharedPreferences sharedPreferences;
    private static final String KEY_IS_SET_ALARM = "key_is_set_alarm";
    private static final String SHARED_NAME = "ALARM";


    private boolean checkIsSetAlarm() {
        if (sharedPreferences.contains(KEY_IS_SET_ALARM)) {
            return sharedPreferences.getBoolean(KEY_IS_SET_ALARM, false);
        }
        return true;
    }


    //package-private для доступа из PowerOnReceiver
    void saveStateAlarm(Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_SET_ALARM, value);
        editor.apply();
    }

}
