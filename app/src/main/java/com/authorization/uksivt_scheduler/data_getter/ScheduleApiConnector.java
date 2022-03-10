package com.authorization.uksivt_scheduler.data_getter;

import com.authorization.uksivt_scheduler.change_elements.ChangesOfDay;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Locale;


/**
 * Класс, содержащий логику для работы с Schedule API.
 */
public class ScheduleApiConnector
{
    //region Область: Поля.
    /**
     * Поле, содержащее индекс нужного дня.
     */
    public Integer dayInd;
    
    /**
     * Поле, содержащее название группы.
     */
    public String groupName;
    //endregion
    
    //region Область: Статические поля.
    /**
     * Статическое поле, содержащее базовый URL сайта с API.
     */
    private static final String baseUrl;
    
    /**
     * Статическое поле, содержащее URL путь к контроллерам по дням.
     */
    private static final String pathToDay;
    
    /**
     * Статическое поле, содержащее URL путь к контроллеру замен.
     */
    private static final String changesController;
    
    /**
     * Статическое поле, содержащее параметр индекса дня.
     */
    private static final String daySelector;
    
    /**
     * Статическое поле, содержащее параметр названия группы.
     */
    private static final String groupSelector;
    //endregion
    
    //region Область: Конструкторы класса.
    /**
     * Конструктор класса.
     *
     * @param day Индекс нужного дня (начиная с 0).
     * @param group Название нужной группы.
     */
    public ScheduleApiConnector(Integer day, String group)
    {
        dayInd = day;
        groupName = group;
    }
    
    //Статический конструктор класса.
    static
    {
        //region Подобласть: Инициализация "baseUrl'.
        baseUrl = "http://uksivtscheduleapi.azurewebsites.net";
        //endregion
        
        //region Подобласть: Инициализация "pathToDay".
        pathToDay = "/api/day/";
        //endregion
        
        //region Подобласть: Инициализация "changesController".
        changesController = "changesday?";
        //endregion
        
        //region Подобласть: Инициализация "daySelector".
        daySelector = "dayIndex=";
        //endregion
    
        //region Подобласть: Инициализация "groupSelector".
        groupSelector = "groupName=";
        //endregion
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод для получения списка замен на группу.
     *
     * @return Объект, содержащий данные о заменах на день.
     *
     * @throws IOException Подключение было сброшено.
     */
    public ChangesOfDay getChanges() throws IOException
    {
        URLConnection connection = new URL(String.format(Locale.getDefault(),"%s%s%s%s%d&%s%s",
        baseUrl, pathToDay, changesController, daySelector, dayInd, groupSelector, groupName)).openConnection();
        String result = IOUtils.toString(connection.getInputStream(), Charset.defaultCharset());
        
        ObjectMapper serializer = new ObjectMapper();
        serializer.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        
        return serializer.readValue(result, ChangesOfDay.class);
    }
    //endregion
}
