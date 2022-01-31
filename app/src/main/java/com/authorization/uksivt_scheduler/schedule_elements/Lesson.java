package com.authorization.uksivt_scheduler.schedule_elements;

/**
 * Класс, предоставляющий логику для одной пары.
 */
public class Lesson implements Comparable<Lesson>
{
    //region Область: Поля.
    /**
     * Внутреннее поле, содержащее номер пары.
     */
    private Integer number;
    
    /**
     * Внутреннее поле, содержащее название предмета.
     */
    private String name;
    
    /**
     * Внутреннее поле, содержащее имя преподавателя.
     */
    private String teacher;
    
    /**
     * Внутреннее поле, содержащее название кабинета, где будет пара.
     */
    private String place;
    
    /**
     * Внутреннее поле, отвечающее за то, что пара была изменена.
     */
    private Boolean lessonChanged = false;
    //endregion
    
    //region Область: Get-Свойства.
    /**
     * Метод для получения значения поля "name".
     *
     * @return Значение поля "name".
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Метод для получения значения поля "number".
     *
     * @return Значение поля "number".
     */
    public Integer getNumber()
    {
        return number;
    }
    
    /**
     * Метод для получения значения поля "teacher".
     *
     * @return Значение поля "teacher".
     */
    public String getTeacher()
    {
        return teacher;
    }
    
    /**
     * Метод для получения значения поля "place".
     *
     * @return Значение поля "place".
     */
    public String getPlace()
    {
        return place;
    }
    
    /**
     * Метод для получения значения поля "lessonChanged".
     *
     * @return Значение поля.
     */
    public Boolean getLessonChanged()
    {
        return lessonChanged;
    }
    //endregion
    
    //region Область: Set-Свойства.
    /**
     * Метод для установки значения свойства "number".
     *
     * @param number Новое значение свойства.
     */
    public void setNumber(Integer number)
    {
        this.number = number;
    }
    
    /**
     * Метод для установки значения свойства "name".
     *
     * @param name Новое значение свойства.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Метод для установки значения свойства "teacher".
     *
     * @param teacher Новое значение свойства.
     */
    public void setTeacher(String teacher)
    {
        this.teacher = teacher;
    }
    
    /**
     * Метод для установки значения свойства "place".
     *
     * @param place Новое значение свойства.
     */
    public void setPlace(String place)
    {
        this.place = place;
    }
    
    /**
     * Метод для установки свойства "lessonChanged".
     *
     * @param lessonChanged Новое значение поля.
     */
    public void setLessonChanged(Boolean lessonChanged)
    {
        this.lessonChanged = lessonChanged;
    }
    //endregion
    
    //region Область: Конструкторы класса.
    /**
     * Конструктор класса по умолчанию.
     */
    public Lesson()
    {
    
    }
    
    /**
     * Конструктор класса для заполнения данных вручную при отсутствующей паре.
     *
     * @param number Номер пары.
     */
    public Lesson(Integer number)
    {
        this.number = number;
        name = null;
        teacher = null;
        place = null;
    }
    
    /**
     * Конструктор класса.
     *
     * @param number  Номер пары.
     * @param name    Название предмета.
     * @param teacher Преподаватель.
     * @param place   Место проведения пары.
     */
    public Lesson(Integer number, String name, String teacher, String place)
    {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.place = place;
    }
    
    /**
     * Конструктор класса.
     *
     * @param number        Номер пары.
     * @param name          Название предмета.
     * @param teacher       Преподаватель.
     * @param place         Место проведения пары.
     * @param lessonChanged Изменялась ли пара.
     */
    public Lesson(Integer number, String name, String teacher, String place, Boolean lessonChanged)
    {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.place = place;
        this.lessonChanged = lessonChanged;
    }
    //endregion
    
    //region Область: Реализация интерфейса.
    /**
     * Метод, нужный для сравнения объектов.
     * <br><br>
     * Реализация интерфейса Comparable.
     *
     * @param o Объект для сравнения.
     *
     * @return Результат сравнения.
     */
    @Override
    public int compareTo(Lesson o)
    {
        if (number > o.number)
        {
            return 1;
        }
        
        else if (number.equals(o.number))
        {
            return 0;
        }
        
        return -1;
    }
    //endregion
}
