package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.change_elements.ChangesOfDay;
import com.authorization.uksivt_scheduler.custom_views.LessonListAdapter;
import com.authorization.uksivt_scheduler.data_getter.ScheduleApiConnector;
import com.authorization.uksivt_scheduler.data_getter.StandardScheduler;
import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Days;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


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
     * Поле, содержащее замены на выбранный день.
     * <br/>
     * Нужно для возможности вывода информации о заменах.
     */
    private ChangesOfDay changes;
    
    /**
     * Поле, содержащее элемент, в который будут помещаться все пары.
     */
    private ListView lessonsList;
    
    /**
     * Поле, содержащее поток, в котором будет происходить получение замен.
     * <br/>
     * Нужно для отмены операции в случае выхода с данного окна.
     */
    private Thread changesThread;
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
        
        //Обработка смены ориентации экрана.
        if (savedInstanceState != null)
        {
            try
            {
                ObjectMapper serializer = new ObjectMapper();
                serializer.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);
                
                lessonsList = findViewById(R.id.schedule_lessons_list);
                schedule = serializer.readValue(savedInstanceState.getString("schedule"), DaySchedule.class);
                
                insertLessonsToActivity();
                return;
            }
            
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        
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
        insertLessonsToActivity();
        
        //region Подобласть: Получение замен.
        changesThread = new Thread(() ->
        {
            runOnUiThread(() -> Toast.makeText(this,
            getString(R.string.changes_receiving_begin), Toast.LENGTH_SHORT).show());
            
            try
            {
                DaySchedule newSchedule = initializeChanges(parent.getStringExtra("group"),
                Days.getIndexByValue(Days.fromString(parent.getStringExtra("day"))));
                
                if (!changes.getChangesFound())
                {
                    runOnUiThread(() -> Toast.makeText(this,
                    getString(R.string.there_is_no_available_changes_file), Toast.LENGTH_SHORT).show());
                }
                
                else if (newSchedule.equals(schedule))
                {
                    runOnUiThread(() -> Toast.makeText(this,
                    getString(R.string.there_is_no_changes), Toast.LENGTH_SHORT).show());
                }
                
                else
                {
                    runOnUiThread(() ->
                    {
                        Toast.makeText(this,
                        getString(R.string.changes_received_successfully), Toast.LENGTH_SHORT).show();
                        
                        schedule = newSchedule;
                        insertLessonsToActivity();
                    });
                }
            }
            
            catch (InterruptedIOException exception)
            {
                runOnUiThread(() -> Toast.makeText(this,
                getString(R.string.changes_receiving_interrupted), Toast.LENGTH_SHORT).show());
            }
            
            catch (IOException exception)
            {
                runOnUiThread(() -> Toast.makeText(this,
                getString(R.string.changes_get_exception), Toast.LENGTH_SHORT).show());
            }
    
            try
            {
                TextView header = findViewById(R.id.schedule_day_header);
                SimpleDateFormat formatter = new SimpleDateFormat("(dd.MM.yyyy!)", Locale.getDefault());
                formatter.setTimeZone(TimeZone.getTimeZone("UTC+00:00"));
                
                runOnUiThread(() ->
                    header.setText(String.format(Locale.getDefault(), "%s %s", header.getText(),
                    formatter.format(changes.getChangesDate())))
                );
            }
    
            catch (NullPointerException e)
            {
                e.fillInStackTrace();
            }
        });
        
        changesThread.start();
        //endregion
    }
    
    /**
     * Событие, происходящее при смене данного окна на любое другое.
     * <br/>
     * Прерывает процесс получения замен.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        //Так как смена ориентации уничтожит поток, необходимо это обработать:
        if (changesThread != null)
        {
            changesThread.interrupt();
        }
    }
    
    /**
     * Событие, происходящее в момент уничтожения окна.
     * <br/>
     * Нужно для сохранения данных в Bundle.
     *
     * @param outState Сохраняемое состояние.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        ObjectMapper serializer = new ObjectMapper();
        serializer.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);
    
        try
        {
            outState.putString("schedule", serializer.writeValueAsString(schedule));
        }
        
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }
    //endregion
    
    //region Область: Методы.
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
    
    /**
     * Метод для получения замен для указанного дня и применения их к расписанию.
     *
     * @param groupName Название нужной группы.
     * @param day       Индекс дня.
     *
     * @return Расписание на день с учетом замен.
     */
    private DaySchedule initializeChanges(String groupName, Integer day) throws IOException
    {
        ScheduleApiConnector connector = new ScheduleApiConnector(day, groupName);
        changes = connector.getChanges();
        
        
        //Если замен нет, то нужно вернуть базовое расписание для корректного сравнения объектов.
        if (changes.getNewLessons().size() == ChangesOfDay.DefaultChanges.getNewLessons().size())
        {
            return schedule;
        }
        
        return schedule.mergeChanges(changes.getNewLessons(), changes.getAbsoluteChanges());
    }
    //endregion
}
