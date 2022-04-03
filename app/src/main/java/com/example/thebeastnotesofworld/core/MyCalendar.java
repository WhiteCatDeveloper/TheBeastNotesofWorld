package com.example.thebeastnotesofworld.core;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


/**
 * Вынесена логика рассчета дней до выполнения каждой заметки.
 * Берется разница между текущей датой и датой создания заметки, показывая сколько дней прошло.
 * Из поля заметки получаем сколько дней было дано на выполнение и из них вычитаются прошедшие дни.
 * И метод соответственно возвращает количество оставшихся дней
 */
public class MyCalendar{

    // К сожалению работает только с api>=26
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int calculateDayToDeadline(Note note) {
        String currentDate = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        String dateOfCreate = note.getDateOfCreate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(dateOfCreate, formatter);
        LocalDate endDate = LocalDate.parse(currentDate, formatter);
        Period period = Period.between(startDate, endDate);
        return note.getDayToDeadLine() - period.getDays();
    }

}
