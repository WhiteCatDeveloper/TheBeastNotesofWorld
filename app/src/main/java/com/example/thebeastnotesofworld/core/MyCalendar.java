package com.example.thebeastnotesofworld.core;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MyCalendar{

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int calculateDayToDeadline(Note note) {
        String currentDate = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String dateOfCreate = note.getDateOfCreate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(dateOfCreate, formatter);
        LocalDate endDate = LocalDate.parse(currentDate, formatter);
        Period period = Period.between(startDate, endDate);
        return note.getDayToDeadLine() - period.getDays();
    }

}
