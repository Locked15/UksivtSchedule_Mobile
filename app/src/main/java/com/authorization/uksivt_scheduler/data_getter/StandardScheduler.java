package com.authorization.uksivt_scheduler.data_getter;

import android.content.Context;
import android.content.res.AssetManager;

import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Days;
import com.authorization.uksivt_scheduler.schedule_elements.WeekSchedule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Класс, нужный для работы с базовым расписанием.
 */
public class StandardScheduler
{
    //region Область: Поля.
    private final AssetManager manager;
    //endregion
    
    //region Область: Конструкторы.
    /**
     * Конструктор класса.
     *
     * @param context Контекст приложения для получения ассетов.
     */
    public StandardScheduler(Context context)
    {
        //Получаем и задаем ассеты:
        manager = context.getAssets();
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод, нужный для получения списка групп в указанной директории.
     *
     * @param folder Название директории для сканирования.
     *
     * @return Список групп.
     */
    public List<String> getGroupsWithFolder(String folder)
    {
        try
        {
            return Arrays.asList(manager.list(folder));
        }
        
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Метод для получения оригинального расписания.
     *
     * @param folder    Директория, в которой находится расписание нужной группы.
     * @param groupName Название нужной группы.
     *
     * @return Оригинальное расписание указанной группы.
     *
     * @throws IOException При считывании расписания произошла ошибка.
     */
    public WeekSchedule getSchedule(String folder, String groupName) throws IOException
    {
        ObjectMapper serializer = new ObjectMapper();
       
        return serializer.readValue(manager.open(getFullPath(folder, groupName)),
        WeekSchedule.class);
    }
    
    /**
     * Метод для получения оригинального расписания на один день.
     *
     * @param folder Директория, в которой находится расписание нужной группы.
     * @param groupName Название нужной группыю
     * @param day Нужный день.
     *
     * @return Расписание на один день.
     *
     * @throws IOException При считывании расписания произошла ошибка.
     */
    public DaySchedule getDaySchedule(String folder, String groupName, Days day) throws IOException
    {
        WeekSchedule temp = getSchedule(folder, groupName);
        
        return temp.getDays().get(Days.getIndexByValue(day));
    }
    //endregion
    
    //region Область: Внутренние методы.
    /**
     * Метод для получения полного пути к файлу с расписанием.
     *
     * @param folder Направление.
     * @param group Название группы.
     *
     * @return Полный путь к файлу с расписанием.
     */
    private String getFullPath(String folder, String group)
    {
        return folder + '/' + group + ".json";
    }
    //endregion
}
