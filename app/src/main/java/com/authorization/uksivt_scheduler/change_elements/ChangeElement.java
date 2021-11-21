package com.authorization.uksivt_scheduler.change_elements;

import androidx.annotation.NonNull;

import com.authorization.uksivt_scheduler.schedule_elements.Days;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;


/**
 * Класс, представляющий одну замену.
 * <br>
 * Условная замена для одного тега <i>"a"</i>.
 */
public class ChangeElement implements Comparable<ChangeElement>
{
    //region Область: Поля класса.
    /**
     * Поле, содержащее день месяца для отображения.
     */
    Integer dayOfMonth;
    
    /**
     * Поле, содержащее ссылку на документ с заменами.
     */
    String linkToDocument;
    
    /**
     * Поле, содержащее название дня для замен.
     */
    Days currentDay;
    //endregion
    
    //region Область: Get-Свойства.
    /**
     * Метод для получения поля "dayOfMonth".
     *
     * @return Значение поля.
     */
    public Integer getDayOfMonth()
    {
        return dayOfMonth;
    }
    
    /**
     * Метод для получения поля "linkToDocument".
     *
     * @return Значение поля.
     */
    public String getLinkToDocument()
    {
        return linkToDocument;
    }
    
    /**
     * Метод для получения поля "currentDay".
     *
     * @return Значение поля.
     */
    public Days getCurrentDay()
    {
        return currentDay;
    }
    //endregion
    
    //region Область: Set-Свойства.
    /**
     * Метод для установки значения поля "dayOfMonth".
     *
     * @param dayOfMonth Новое значение поля.
     */
    public void setDayOfMonth(Integer dayOfMonth)
    {
        this.dayOfMonth = dayOfMonth;
    }
    
    /**
     * Метод для установки значения поля "linkToDocument".
     *
     * @param linkToDocument Новое значение поля.
     */
    public void setLinkToDocument(String linkToDocument)
    {
        this.linkToDocument = linkToDocument;
    }
    
    /**
     * Метод для установки значения поля "currentDay".
     *
     * @param currentDay Новое значение поля.
     */
    public void setCurrentDay(Days currentDay)
    {
        this.currentDay = currentDay;
    }
    //endregion
    
    //region Область: Конструкторы.
    /**
     * Конструктор по умолчанию.
     */
    public ChangeElement()
    {
    
    }
    
    /**
     * Конструктор класса.
     *
     * @param dayOfMonth Текущее число месяца.
     * @param linkToDocument Ссылка на документ.
     * @param currentDay Текущий день недели.
     */
    public ChangeElement(Integer dayOfMonth, String linkToDocument, Days currentDay)
    {
        this.dayOfMonth = dayOfMonth;
        this.linkToDocument = linkToDocument;
        this.currentDay = currentDay;
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод, нужный для проверки текущего дня на наличие доступного файла с заменами.
     *
     * @return Логическое значение, отвечающее за доступность замен.
     */
    public Boolean haveChanges()
    {
        //Для удобства вынесем эту проверку в отдельный метод.
        return linkToDocument != null;
    }
    
    /**
     * Метод, нужный для проверки соответствия недели и полученного элемента с заменами.
     * <br>
     * Так как менять экземпляры классов в методах этих классов нельзя, ...
     * ... он просто возвращает экземпляр этого класса.
     *
     * @return Экземпляр класса ChangeElement.
     */
    public ChangeElement checkWeek()
    {
        DateTime start = new DateTime().dayOfWeek().setCopy(DateTimeConstants.MONDAY);
        DateTime end = new DateTime().dayOfWeek().setCopy(DateTimeConstants.SUNDAY);
        
        if (dayOfMonth >= start.getDayOfMonth() && dayOfMonth <= end.getDayOfMonth() + 1)
        {
            return this;
        }
        
        else
        {
            return new ChangeElement();
        }
    }
    
    /**
     * Реализация интерфейса.
     * <br>
     * Метод для сравнения двух объектов. Нужен для работы сортировки.
     *
     * @param o Второй объект для сравнения.
     *
     * @return Результат сравнения объектов.
     */
    @Override
    public int compareTo(ChangeElement o)
    {
        if (this.dayOfMonth > o.dayOfMonth)
        {
            return 1;
        }
        
        else if (this.dayOfMonth.equals(o.dayOfMonth))
        {
            return 0;
        }
        
        return -1;
    }
    
    /**
     * Метод для получения строкового представления объекта.
     *
     * @return Строковое представление объекта.
     */
    @NonNull
    @Override
    public String toString()
    {
        return "\nChangeElement: \n" +
        "DayOfMonth = " + dayOfMonth + ";\n" +
        "LinkToDocument = " + linkToDocument + ";\n" +
        "CurrentDay = " + Days.toString(currentDay) + ".\n";
    }
    
    /**
     * Дополнительный метод для получения строкового представления.
     *
     * @param append Строка, которую следует добавить в вывод.
     *
     * @return Строковое представление объекта.
     */
    public String toString(String append)
    {
        return "\n" + append + "ChangeElement: \n" +
        append + "DayOfMonth = " + dayOfMonth + ";\n" +
        append + "LinkToDocument = " + linkToDocument + ";\n" +
        append + "CurrentDay = " + Days.toString(currentDay) + ".\n";
    }
    //endregion
}
