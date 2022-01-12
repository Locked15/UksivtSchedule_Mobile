package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.change_elements.MonthChanges;
import com.authorization.uksivt_scheduler.custom_views.LessonListAdapter;
import com.authorization.uksivt_scheduler.data_getter.DataGetter;
import com.authorization.uksivt_scheduler.data_getter.StandardScheduler;
import com.authorization.uksivt_scheduler.data_reader.DataReader;
import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Days;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_viewer);
        
        Intent parent = getIntent();
        
        try
        {
            schedule = new StandardScheduler(this).getDaySchedule
            (parent.getStringExtra("folder"), parent.getStringExtra("subFolder"),
            parent.getStringExtra("group"), Days.fromString(parent.getStringExtra("day")));
        }
        
        catch (IOException e)
        {
            schedule = DaySchedule.getEmptySchedule();
        }
        
        lessonsList = findViewById(R.id.schedule_lessons_list);

        //region ! Вызов функции приведет к вызову исключения. !
        
        // initializeChanges();
        
        //endregion

        insertLessonsToActivity();
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод для получения замен для указанного дня и применения их к расписанию.
     */
    private void initializeChanges()
    {
        //В Android нельзя обращаться к сети в основном потоке.
        Thread thread = new Thread(new Runnable()
        {
            /**
             * Асинхронная функция, нужная для обращения к сети.
             */
            @Override
            public void run()
            {
                File changeFile = new File(getCacheDir() + "/Prog.docx");
                DataGetter getter = new DataGetter();
                MonthChanges changes = getter.getAvailableNodes().get(0);
                
                getter.downloadFileWithChanges(getter.getDownloadableLinkToChangesFile
                (changes.tryToFindElementByNameOfDay(schedule.day).getLinkToDocument()), changeFile.getAbsolutePath());

                //Выброс искючения:
                DataReader reader = new DataReader(changeFile.getAbsolutePath());
            }
        });

        thread.start();
    }

    /**
     * Метод для вставки расписания в окно.
     */
    private void insertLessonsToActivity()
    {
        TextView header = findViewById(R.id.schedule_day_header);
        header.setText(String.format("%s%s", getString(R.string.current_day_is),
        Days.toString(schedule.day)));
        
        //Удаляем все пустые пары (почти как LINQ!):
        schedule.lessons.removeIf(lesson -> lesson.getName() == null);
    
        LessonListAdapter adapter = new LessonListAdapter(this, schedule);
        lessonsList.setAdapter(adapter);
    }
    //endregion
}
