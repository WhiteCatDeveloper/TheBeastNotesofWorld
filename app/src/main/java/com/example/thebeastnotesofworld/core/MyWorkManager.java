package com.example.thebeastnotesofworld.core;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
        builder.setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFY_ID, builder.build());
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
        int result;
        if (dayEnd == 0 && deadLine == 0) result = 0;
        else if (dayEnd > 0 && deadLine == 0) result = 1;
        else if (dayEnd == 0 && deadLine > 0) result = 2;
        else result = 3;
        return result;
    }
}
