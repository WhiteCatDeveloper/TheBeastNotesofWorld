package com.example.thebeastnotesofworld.core.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.db.WorkingInDB;
import com.example.thebeastnotesofworld.core.notes.ToDoNote;
import com.example.thebeastnotesofworld.view.activity.ToDoNotesActivity;

import java.util.ArrayList;

public class MyNotification {
    private static final String CHANNEL_ID = "My channel";
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Счетчик дел, показывает нужно ли уведомление и определяет что показать пользователю.
    // Значение устанавливается метом countNeedNotification() при вызове конструктора класса
    private int dayEnd = 0;
    private int deadLine = 0;

    public void createNotificationIfNeed(Context context) {
        countNeedNotification(context);
            if (dayEnd != 0 || deadLine != 0) {
                setNotification(context);
            }
    }


    // Создает уведомление
    private void setNotification(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID);
        String title = "ЕСТЬ СРОЧНЫЕ ДЕЛА!";
        createNotificationChannel(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(getTextNotification())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    // Создает намерение, определяющее что произойдет по нажатию на уведомление
    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, ToDoNotesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
            CharSequence name = "my_channel";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }

    // Возвращает нужный текст для заметки в зависимости от ситуации с задачами
    @NonNull
    private String getTextNotification () {
        String s = "Просроченые задачи: " + dayEnd;
        String s1 = "Выполнить сегодня: " + deadLine;
        if (dayEnd > 0 && deadLine == 0) return s;
        else if (dayEnd == 0 && deadLine > 0) return s1;
        else return s + ". " + s1;
    }


    // Проходит все текущие задачи в БД и устанавливает значения у переменных dayEnd и deadline
    private void countNeedNotification (Context context) {
        int whatShow = getWhatShowNotification(context);
        ArrayList<ToDoNote> list = new WorkingInDB().getToDoNotes(context, null);
        for (ToDoNote note : list) {
            if (note.getImportance() >= whatShow) {
                if (note.calculateDayToDeadline() < 1) dayEnd++;
                else if (note.calculateDayToDeadline() == 1) deadLine++;
            }
        }
    }

    private int getWhatShowNotification(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("WHAT_SHOW_NOTIFICATION")) {
            return sharedPreferences.getInt("WHAT_SHOW_NOTIFICATION", 0);
        } else return 0;
    }
}
