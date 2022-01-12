package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.schedule_elements.Days;

import java.util.Dictionary;
import java.util.Hashtable;


/**
 * Класс, представляющий логику для "activity_day_selector".
 */
public class DaySelectorActivity extends AppCompatActivity
{
    //region Область: Поля.
    /**
     * Внутреннее поле, содержащее путь к нужной директории с группой.
     */
    private String path;
    
    /**
     * Внутреннее поле, содержащее название подпапки с принадлежностью группы.
     */
    private String subFolder;
    
    /**
     * Внутреннее поле, содержащее название нужной группы.
     */
    private String groupName;
    
    /**
     * Внутренее поле, содержащее словарь с днями и кнопками им соответствующими.
     */
    private Dictionary<Button, Days> possibleDaysToSelect;
    //endregion
    
    //region Область: События запуска окна.
    /**
     * Событие, происходящее при создании окна.
     *
     * @param savedInstanceState Последнее сохраненное состояние приложения.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_selector);
        
        initializeFields();
    }
    
    /**
     * Внутренний метод, нужный для инициализации значения полей.
     */
    private void initializeFields()
    {
        //region Подобласть: Инициализация словаря с днями.
        possibleDaysToSelect = new Hashtable<>(7);
        
        possibleDaysToSelect.put((Button)findViewById(R.id.days_monday_button), Days.Monday);
        possibleDaysToSelect.put((Button)findViewById(R.id.days_tuesday_button), Days.Tuesday);
        possibleDaysToSelect.put((Button)findViewById(R.id.days_wednesday_button), Days.Wednesday);
        possibleDaysToSelect.put((Button)findViewById(R.id.days_thursday_button), Days.Thursday);
        possibleDaysToSelect.put((Button)findViewById(R.id.days_friday_button), Days.Friday);
        possibleDaysToSelect.put((Button)findViewById(R.id.days_saturday_button), Days.Saturday);
        possibleDaysToSelect.put((Button)findViewById(R.id.days_sunday_button), Days.Sunday);
        //endregion
        
        //region Подобласть: Инициализация полей группы.
        path = getIntent().getStringExtra("folder");
        subFolder = getIntent().getStringExtra("subFolder");
        groupName = getIntent().getStringExtra("group");
        //endregion
    }
    //endregion
    
    //region Область: Прочие события.
    /**
     * Событие, происходящее при нажатии на одну из кнопок.
     *
     * @param view Элемент, вызвавший событие.
     */
    public void oneOfButtonsAreClicked(View view)
    {
        Days selectedDay = possibleDaysToSelect.get(view);
        
        if (selectedDay == null)
        {
            Toast.makeText(this, getString(R.string.error_occurred),
            Toast.LENGTH_LONG).show();
            
            return;
        }
    
        //Вообще, можно было сделать это через сериализацию, ...
        //... но, как я думаю, здесь в этом нет особого смысла.
        Intent newWindow = new Intent(this, FinalScheduleActivity.class);
        newWindow.putExtra("folder", path);
        newWindow.putExtra("subFolder", subFolder);
        newWindow.putExtra("group", groupName);
        newWindow.putExtra("day", Days.toString(selectedDay));
        
        startActivity(newWindow);
    }
    //endregion
}
