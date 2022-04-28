package com.example.thebeastnotesofworld.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;


public class MyAlarmManager {



    public void setAlarm (Context context) {
        Calendar notifyTime =
        notifyTime.set(Calendar.HOUR_OF_DAY,12);
        notifyTime.set(Calendar.MINUTE,0);
        notifyTime.set(Calendar.SECOND,0);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,notifyTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }




    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
