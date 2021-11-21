package com.authorization.uksivt_scheduler.schedule_elements;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Класс, предоставляющий логику для расписания одного конкретного дня.
 */
public class DaySchedule
{
    //region Область: Поля класса.
    /**
     * Поле, содержащее день, который описывается.
     */
    public Days day;
    
    /**
     * Поле, содержащее пары, проводимые в этот день.
     */
    public ArrayList<Lesson> lessons;
    //endregion
    
    //region Область: Конструктор класса.
    public DaySchedule()
    {
    
    }
    
    /**
     * Конструктор класса.
     *
     * @param day     День недели.
     * @param lessons Пары этого дня.
     */
    public DaySchedule(Days day, ArrayList<Lesson> lessons)
    {
        this.day = day;
        this.lessons = lessons;
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод, возвращающий строковое представление объекта.
     *
     * @return Строковое представление отправленного объекта.
     */
    @NonNull
    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder(Days.toString(day) + ":\n" +
        "{");
        
        for (Lesson lesson : lessons)
        {
            toReturn.append("\n\t{").append("\n\t\tНомер пары: ").append(lesson.getNumber())
            .append("\n\t\t").append("Название пары: ").append(lesson.getName()).append("\n\t\t")
            .append("Кабинет: ").append(lesson.getPlace()).append("\n\t\t")
            .append("Преподаватель: ").append(lesson.getTeacher()).append("\n\t}");
        }
        
        toReturn.append("\n}");
        
        return toReturn.toString();
    }
    //endregion
    
    //region Область: Статические методы.
    /**
     * Метод для получения дня с "пустым" расписанием.
     *
     * @return Пустое расписание.
     */
    public static DaySchedule getEmptySchedule()
    {
        ArrayList<Lesson> lessons = new ArrayList<>(7);
        lessons.add(new Lesson(0));
        lessons.add(new Lesson(1));
        lessons.add(new Lesson(2));
        lessons.add(new Lesson(3));
        lessons.add(new Lesson(4));
        lessons.add(new Lesson(5));
        lessons.add(new Lesson(6));
        
        return new DaySchedule(Days.Sunday, lessons);
    }
    //endregion
}
