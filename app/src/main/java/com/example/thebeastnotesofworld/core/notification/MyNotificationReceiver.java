package com.example.thebeastnotesofworld.core.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.thebeastnotesofworld.core.notification.MyNotification;

public class MyNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new MyNotification().createNotificationIfNeed(context);
    }
}