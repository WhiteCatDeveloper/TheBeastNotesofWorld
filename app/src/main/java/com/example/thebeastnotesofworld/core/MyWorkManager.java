package com.example.thebeastnotesofworld.core;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.thebeastnotesofworld.R;

import java.util.ArrayList;

/**
 * В этом классе устанавливаются запланированные уведомления о задачах, у которых либо вышло время,
 * либо текущий день - последний. Уведомления устанавливаются на определенное время благодаря
 * работе WorkManager
 */

public class MyWorkManager extends Worker {

    public MyWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Идентификатор канала
    private static final String CHANNEL_ID = "My channel";
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Result doWork() {
        if (countNeedNotification() != 0) {
            Log.e("WMERR", "sucsessful");
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
        String text = getTextNotification();
        createNotificationChannel();
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFY_ID, builder.build());
        Log.e("WMERR", "" + text);

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getTextNotification () {
        String s = "Есть просроченые задачи!";
        String s1 = "Есть задачи которые нужно сделать сегодня!";
        if (countNeedNotification() == 1) {
            return s;
        } else if (countNeedNotification() == 2) {
            return s1;
        } else return s + "\n" + s1;
    }


    /**
     * Проходит все текущие задачи в БД и возвращает значение в зависимости от наличия (отсутствия)
     * задач нуждающихся в уведомлении.
     * 0 - таких задач нет (в уведомлении нет необходимости)
     * 1 - Есть просроченые задачи
     * 2 - Есть задачи срок которых заканчивается сегодня
     * 3 - Есть просроченые задачи и задачи, срок которых заканчивается сегодня
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private int countNeedNotification () {
        ArrayList<ToDoNote> list = new WorkingInDB().getToDoNotes(getApplicationContext(), null);
        int dayEnd = 0;
        int deadLine = 0;
        for (ToDoNote note : list) {
            if (note.calculateDayToDeadline() < 1) dayEnd++;
                else if (note.calculateDayToDeadline() == 1) deadLine++;
        }
        Log.e("WMERR", "" + dayEnd + ", " + deadLine);
        int result;
        if (dayEnd == 0 && deadLine == 0) result = 0;
        else if (dayEnd > 0 && deadLine == 0) result = 1;
        else if (dayEnd == 0 && deadLine > 0) result = 2;
        else result = 3;
        return result;
    }
}
