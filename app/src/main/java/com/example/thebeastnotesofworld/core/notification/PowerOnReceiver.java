package com.example.thebeastnotesofworld.core.notification;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class PowerOnReceiver extends BroadcastReceiver {
    //Интент мы здесь никак не используем поэтому можно просто игнорировать
    // (Если использовать его то нужна обязательная проверка на безопасность)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        MyAlarmManager alarmManager = new MyAlarmManager(context);
        alarmManager.saveStateAlarm(false);
        alarmManager.setAlarm();
    }
}