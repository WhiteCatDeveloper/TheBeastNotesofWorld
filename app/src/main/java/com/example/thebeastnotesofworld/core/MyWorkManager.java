package com.example.thebeastnotesofworld.core;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.notes.ToDoNote;
import com.example.thebeastnotesofworld.view.activity.ToDoNotesActivity;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * В этом классе устанавливаются запланированные уведомления о задачах, у которых либо вышло время,
 * либо текущий день - последний. Уведомления устанавливаются на определенное время благодаря
 * работе WorkManager
 */

public class MyWorkManager extends Worker {

    // Идентификатор канала
    private static final String CHANNEL_ID = "My channel";
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Счетчик дел, показывает нужно ли уведомление и определяет что показать пользователю.
    // Значение устанавливается метом countNeedNotification() при вызове конструктора класса
    private int dayEnd = 0;
    private int deadLine = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        countNeedNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setWorkManager() {
        int hourOfTheDay = 10; // When to run the job
        int repeatInterval = 1; // In days

        long flexTime = calculateFlex(hourOfTheDay, repeatInterval);

        Constraints myConstraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(MyWorkManager.class,
                        repeatInterval, TimeUnit.DAYS,
                        flexTime, TimeUnit.MILLISECONDS)
                        .setConstraints(myConstraints)
                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(CHANNEL_ID,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private long calculateFlex(int hourOfTheDay, int periodInDays) {

        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        if (cal2.getTimeInMillis() < cal1.getTimeInMillis()) {
            // Add the worker periodicity.
            cal2.setTimeInMillis(cal2.getTimeInMillis() + TimeUnit.DAYS.toMillis(periodInDays));
        }

        long delta = (cal2.getTimeInMillis() - cal1.getTimeInMillis());

        return (Math.max(delta, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS));
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Result doWork() {
        if (dayEnd != 0 || deadLine != 0) {
            setNotification();
        }
        return Result.success();
    }

    // Создает уведомление
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        String title = "ЕСТЬ СРОЧНЫЕ ДЕЛА!";
        createNotificationChannel();
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(getTextNotification())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    // Создает намерение, определяющее что произойдет по нажатию на уведомление
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), ToDoNotesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my_channel";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Возвращает нужный текст для заметки в зависимости от ситуации с задачами
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getTextNotification () {
        String s = "Просроченые задачи: " + dayEnd;
        String s1 = "Выполнить сегодня: " + deadLine;
        if (dayEnd > 0 && deadLine == 0) return s;
        else if (dayEnd == 0 && deadLine > 0) return s1;
        else return s + ". " + s1;
    }


    // Проходит все текущие задачи в БД и устанавливает значения у переменных dayEnd и deadline
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void countNeedNotification () {
        ArrayList<ToDoNote> list = new WorkingInDB().getToDoNotes(getApplicationContext(), null);
        for (ToDoNote note : list) {
            if (note.calculateDayToDeadline() < 1) dayEnd++;
            else if (note.calculateDayToDeadline() == 1) deadLine++;
        }
    }
}
