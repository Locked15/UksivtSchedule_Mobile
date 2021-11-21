package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.custom_views.LessonListAdapter;
import com.authorization.uksivt_scheduler.data_getter.StandardScheduler;
import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Days;

import java.io.IOException;

/**
 * Класс, определяющий логику для "activity_schedule_viewer".
 */
public class FinalScheduleActivity extends AppCompatActivity
{
    //region Область: Поля.
    /**
     * Поле, содержащее расписание на день.
     */
    private DaySchedule schedule;
    
    /**
     * Поле, содержащее элемент, в который будут помещаться все пары.
     */
    private ListView lessonsList;
    //endregion
    
    //region Область: События.
    /**
     * Событие, происходящее при создании окна.
     *
     * @param savedInstanceState Последнее сохраненное состояние приложения.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        //TODO: Реализовать получение замен.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_viewer);
        
        Intent parent = getIntent();
        
        try
        {
            schedule = new StandardScheduler(this).getDaySchedule
            (parent.getStringExtra("folder"), parent.getStringExtra("group"),
            Days.fromString(parent.getStringExtra("day")));
        }
        
        catch (IOException e)
        {
            schedule = DaySchedule.getEmptySchedule();
        }
        
        lessonsList = (ListView)findViewById(R.id.schedule_lessons_list);
        insertLessonsToActivity(schedule);
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод для вставки расписания в окно.
     *
     * @param schedule Расписание, которое надо вставить.
     */
    private void insertLessonsToActivity(DaySchedule schedule)
    {
        TextView header = (TextView)findViewById(R.id.schedule_day_header);
        header.setText(String.format("%s%s", getString(R.string.current_day_is),
        Days.toString(schedule.day)));
        
        //Удаляем все пустые пары (почти как LINQ!):
        schedule.lessons.removeIf(lesson -> lesson.getName() == null);
    
        LessonListAdapter adapter = new LessonListAdapter(this, schedule);
        lessonsList.setAdapter(adapter);
    }
    //endregion
}
