package com.example.thebeastnotesofworld.core.notes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ToDoNote extends Note{
    private final int importance;
    private final int dayToDeadLine;


    public ToDoNote(int id, String title, String text, int importance, int dayToDeadLine, String dateOfCreate) {
        super(id, title, text, dateOfCreate);
        this.importance = importance;
        this.dayToDeadLine = dayToDeadLine;
    }

    public int getImportance() {
        return importance;
    }

    public int getDayToDeadLine() {
        return dayToDeadLine;
    }

    // Компаратор для сортировки по текущим оставшимся дням
    public static final Comparator<ToDoNote> COMPARE_BY_CURRENT_DAY_TO_DEADLINE = (o1, o2) -> {
        int result = 0;
        if (o1.calculateDayToDeadline() < o2.calculateDayToDeadline()) {
            result = -1 ;
        } else if (o1.calculateDayToDeadline() > o2.calculateDayToDeadline()) {
            result = 1;
        }
        return result;
    };


    /**
     * Вынесена логика рассчета дней до выполнения каждой заметки.
     * Берется разница между текущей датой и датой создания заметки, показывая сколько дней прошло.
     * Из поля заметки получаем сколько дней было дано на выполнение и из них вычитаются прошедшие дни.
     * И метод соответственно возвращает количество оставшихся дней
     */
    public int calculateDayToDeadline() {
        String currentDate = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        String dateOfCreate = this.getDateOfCreate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate endDate = LocalDate.parse(currentDate, formatter);
        LocalDate startDate = LocalDate.parse(dateOfCreate, formatter);
        Period period = Period.between(startDate, endDate);
        return this.getDayToDeadLine() - period.getDays();
    }
}
