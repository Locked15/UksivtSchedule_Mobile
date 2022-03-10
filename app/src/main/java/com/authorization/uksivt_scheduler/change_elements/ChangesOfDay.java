package com.authorization.uksivt_scheduler.change_elements;

import com.authorization.uksivt_scheduler.schedule_elements.Lesson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


/**
 * Класс, представляющий полученные от API данные о заменах.
 */
public class ChangesOfDay
{
    //region Область: Поля.
    /**
     * Были ли вообще найдены замены на указанную дату.
     */
    private Boolean changesFound;
    
    /**
     * Абсолютные ли замены.
     */
    private Boolean absoluteChanges;
    
    /**
     * Дата, на которую предназначены замены.
     */
    private Date changesDate;
    
    /**
     * Список с новыми парами.
     */
    private ArrayList<Lesson> newLessons;
    
    /**
     * Статическое поле, содержащее "пустые" замены на день.
     */
    public static final ChangesOfDay DefaultChanges;
    //endregion
    
    //region Область: Get-свойства.
    /**
     * Get-свойство для поля "changesFound".
     *
     * @return Значение поля.
     */
    public Boolean getChangesFound()
    {
        return changesFound;
    }
    
    /**
     * Get-свойство для поля "absoluteChanges".
     *
     * @return Значение поля.
     */
    public Boolean getAbsoluteChanges()
    {
        return absoluteChanges;
    }
    
    /**
     * Get-свойство для поля "changesDate".
     *
     * @return Значение поля.
     */
    public Date getChangesDate()
    {
        return changesDate;
    }
    
    /**
     * Get-свойство для поля "newLessons".
     *
     * @return Значение поля.
     */
    public ArrayList<Lesson> getNewLessons()
    {
        return newLessons;
    }
    //endregion
    
    //region Область: Set-свойства.
    /**
     * Set-свойство для поля "changesFound".
     *
     * @param changesFound Новое значение поля.
     */
    public void setChangesFound(Boolean changesFound)
    {
        this.changesFound = changesFound;
    }
    
    /**
     * Set-свойство для поля "absoluteChanges".
     *
     * @param absoluteChanges Новое значение поля.
     */
    public void setAbsoluteChanges(Boolean absoluteChanges)
    {
        this.absoluteChanges = absoluteChanges;
    }
    
    /**
     * Set-свойство для поля "changesDate".
     *
     * @param changesDate Новое значение поля.
     */
    public void setChangesDate(Date changesDate)
    {
        this.changesDate = changesDate;
    }
    
    /**
     * Set-свойство для поля "newLessons".
     *
     * @param newLessons Новое значение поля.
     */
    public void setNewLessons(ArrayList<Lesson> newLessons)
    {
        this.newLessons = newLessons;
    }
    //endregion
    
    //region Область: Конструкторы класса.
    /**
     * Конструктор класса по умолчанию.
     */
    public ChangesOfDay()
    {
        changesFound = false;
        absoluteChanges = false;
        this.newLessons = new ArrayList<>(0);
    }
    
    /**
     * Конструктор класса.
     *
     * @param absoluteChanges Абсолютные ли замены (на весь день)?
     * @param newLessons      Список с новыми парами.
     */
    public ChangesOfDay(Boolean absoluteChanges, ArrayList<Lesson> newLessons)
    {
        this.absoluteChanges = absoluteChanges;
        this.newLessons = newLessons;
    }
    
    /**
     * Конструктор класса.
     *
     * @param changesFound    Найден ли элемент с заменами на выбранную дату?
     * @param absoluteChanges Абсолютные ли замены (на весь день)?
     * @param newLessons      Список с новыми парами.
     */
    public ChangesOfDay(Boolean changesFound, Boolean absoluteChanges, ArrayList<Lesson> newLessons)
    {
        this.changesFound = changesFound;
        this.absoluteChanges = absoluteChanges;
        this.newLessons = newLessons;
    }
    
    /**
     * Конструктор класса.
     *
     * @param changesFound    Найдены ли элемент с заменами на выбранную дату?
     * @param absoluteChanges Абсолютные ли замены (на весь день)?
     * @param changesDate     Дата замен.
     * @param newLessons      Список с новыми парами.
     */
    public ChangesOfDay(Boolean changesFound, Boolean absoluteChanges, Date changesDate, ArrayList<Lesson> newLessons)
    {
        this.changesFound = changesFound;
        this.absoluteChanges = absoluteChanges;
        this.changesDate = changesDate;
        this.newLessons = newLessons;
    }
    
    //Статический конструктор класса.
    static
    {
        DefaultChanges = new ChangesOfDay(false, new ArrayList<>(0));
        DefaultChanges.changesFound = false;
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Переопределение метода для сравнения объектов.
     *
     * @param o Объект, с которым идет сравнение.
     *
     * @return Логическое значение равенства объектов.
     */
    @Override
    public boolean equals(Object o)
    {
        ChangesOfDay changes = (ChangesOfDay)o;
        
        if (this == o)
        {
            return true;
        }
        
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        
        return Objects.equals(absoluteChanges, changes.absoluteChanges) &&
        Objects.equals(newLessons, changes.newLessons);
    }
    
    /**
     * Метод для получения хэш-кода объекта.
     *
     * @return Хэш-код объекта.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(absoluteChanges,
        newLessons);
    }
    //endregion
}
