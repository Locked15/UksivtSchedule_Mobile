package com.authorization.uksivt_scheduler.change_elements;

import com.authorization.uksivt_scheduler.schedule_elements.Days;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Класс, представляющий определение замен за месяц.
 */
public class MonthChanges
{
    //region Область: Поля.
    /**
     * Поле, содержащее текущий месяц.
     */
    String currentMonth;
    
    /**
     * Поле, содержащее список с заменами за месяц.
     */
    ArrayList<ChangeElement> changes;
    //endregion
    
    //region Область: Get-Свойства.
    /**
     * Метод для получения значения поля "currentMonth".
     *
     * @return Текущее значение поля.
     */
    public String getCurrentMonth()
    {
        return currentMonth;
    }
    
    /**
     * Метод для получения значения поля "changes".
     *
     * @return Текущее значение поля.
     */
    public ArrayList<ChangeElement> getChanges()
    {
        return changes;
    }
    //endregion
    
    //region Область: Set-Свойства.
    /**
     * Метод для установки значения поля "currentMonth".
     *
     * @param currentMonth Новое значение поля.
     */
    public void setCurrentMonth(String currentMonth)
    {
        this.currentMonth = currentMonth;
    }
    
    /**
     * Метод для установки значения поля "changes".
     *
     * @param changes Новое значение поля.
     */
    public void setChanges(ArrayList<ChangeElement> changes)
    {
        this.changes = changes;
    }
    //endregion
    
    //region Область: Конструкторы.
    /**
     * Конструктор по умолчанию.
     */
    public MonthChanges()
    {
    
    }
    
    /**
     * Конструктор класса.
     *
     * @param currentMonth Текущий месяц.
     * @param changes Список с заменами по дням.
     */
    public MonthChanges(String currentMonth, ArrayList<ChangeElement> changes)
    {
        this.currentMonth = currentMonth;
        this.changes = changes;
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод, выполняющий поиск по заменам текущего месяца и возвращающем день по указанному числу.
     * <br>
     * Если для указанного дня замены отсутствуют или недоступны, возвращает <i>"null"</i>.
     *
     * @param day Число месяца для поиска дня с заменами.
     *
     * @return Замены на указанный день.
     */
    public ChangeElement tryToFindElementByNumberOfDay(Integer day)
    {
        for (ChangeElement element : changes)
        {
            if (element.getDayOfMonth().equals(day) && element.haveChanges())
            {
                return element;
            }
        }
        
        return null;
    }
    
    /**
     * Метод, выполняющий поиск по заменам текущего месяца и возвращающем день с указанным названием дня.
     * <br>
     * Если такой день не найден, будет возвращен <i>"null"</i>.
     *
     * @param day Название дня для поиска.
     *
     * @return Элемент замены с указанным днем.
     */
    public ChangeElement tryToFindElementByNameOfDay(Days day)
    {
        //Чтобы начать искать с конца месяца, переворачиваем список:
        Collections.reverse(changes);
        
        for (ChangeElement element : changes)
        {
            if (element.getCurrentDay().equals(day) && element.haveChanges())
            {
                return element;
            }
        }
        
        return null;
    }
    
    /**
     * Метод для получения строкового представления объекта.
     *
     * @return Строковое представление.
     */
    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("\nНовый месяц: \n" +
        "CurrentMonth = " + currentMonth + ";\n" +
        "Changes:" +
        "\n{");
        
        for (ChangeElement change : changes)
        {
            toReturn.append(change.toString("\t"));
        }
        
        toReturn.append("}");
        
        return toReturn.toString();
    }
    //endregion
}
