package com.authorization.uksivt_scheduler.schedule_elements;

import java.util.Locale;

/**
 * Перечисление, нужное для отображения дня.
 */
public enum Days
{
    //region Область: Элементы перечисления.
    /**
     * Понедельник.
     */
    Monday,
    
    /**
     * Вторник.
     */
    Tuesday,
    
    /**
     * Среда.
     */
    Wednesday,
    
    /**
     * Четверг.
     */
    Thursday,
    
    /**
     * Пятница.
     */
    Friday,
    
    /**
     * Суббота.
     */
    Saturday,
    
    /**
     * Воскресенье.
     */
    Sunday;
    //endregion
    
    //region Область: Методы перечисления.
    //region Подобласть: Работа с индексами.
    /**
     * Метод для получения дня по указанному индексу.
     *
     * @param i Индекс по которому нужно получить значение.
     *
     * @return Значение, соответствующее данному индексу.
     *
     * @throws IndexOutOfBoundsException Введенный индекс превышает количество значений.
     */
    public static Days getValueByIndex(Integer i) throws IndexOutOfBoundsException
    {
        switch (i)
        {
            case 0:
            {
                return Monday;
            }
            
            case 1:
            {
                return Tuesday;
            }
            
            case 2:
            {
                return Wednesday;
            }
            
            case 3:
            {
                return Thursday;
            }
            
            case 4:
            {
                return Friday;
            }
            
            case 5:
            {
                return Saturday;
            }
            
            case 6:
            {
                return Sunday;
            }
            
            default:
            {
                throw new IndexOutOfBoundsException("Введен слишком большой индекс.");
            }
        }
    }
    
    /**
     * Метод для получения индекса для указанного дня.
     *
     * @param day День, индекс которого нужно получить.
     *
     * @return Значение, соответствующее данному индексу.
     *
     * @throws IndexOutOfBoundsException Введенный индекс превышает количество значений.
     */
    public static Integer getIndexByValue(Days day)
    {
        switch (day)
        {
            case Monday:
            {
                return 0;
            }
            
            case Tuesday:
            {
                return 1;
            }
            
            case Wednesday:
            {
                return 2;
            }
            
            case Thursday:
            {
                return 3;
            }
            
            case Friday:
            {
                return 4;
            }
            
            case Saturday:
            {
                return 5;
            }
            
            case Sunday:
            {
                return 6;
            }
            
            default:
            {
                throw new IllegalArgumentException();
            }
        }
    }
    //endregion
    
    //region Подобласть: Преобразования из/в строки.
    /**
     * Метод для преобразования Enum-значения в строковый вид.
     *
     * @param day Элемент перечисления Enum.
     *
     * @return Строковое представление перечисления.
     */
    public static String toString(Days day)
    {
        String newDay = "";
        
        switch (day)
        {
            case Monday:
            {
                newDay = "Monday";
                
                break;
            }
            
            case Tuesday:
            {
                newDay = "Tuesday";
                
                break;
            }
            
            case Wednesday:
            {
                newDay = "Wednesday";
                
                break;
            }
            
            case Thursday:
            {
                newDay = "Thursday";
                
                break;
            }
            
            case Friday:
            {
                newDay = "Friday";
                
                break;
            }
            
            case Saturday:
            {
                newDay = "Saturday";
                
                break;
            }
            
            case Sunday:
            {
                newDay = "Sunday";
                
                break;
            }
        }
        
        return translateToRussianLanguage(newDay);
    }
    
    /**
     * Метод для преобразования Enum-значения в строковый вид.
     *
     * @param day Элемент перечисления Enum.
     *
     * @return Строковое представление перечисления.
     */
    public static Days fromString(String day)
    {
        day = translateToOriginalLanguage(day);
        
        switch (day)
        {
            case "Monday":
            {
                return Monday;
            }
            
            case "Tuesday":
            {
                return Tuesday;
            }
            
            case "Wednesday":
            {
                return Wednesday;
            }
            
            case "Thursday":
            {
                return Thursday;
            }
            
            case "Friday":
            {
                return Friday;
            }
            
            case "Saturday":
            {
                return Saturday;
            }
            
            case "Sunday":
            {
                return Sunday;
            }
            
            default:
            {
                return null;
            }
        }
    }
    //endregion
    
    //region Подобласть: Перевод значений на русский и обратно.
    /**
     * Метод для перевода русскоязычных названий дней в оригинальный (английский) язык.
     *
     * @param day День недели на русском.
     *
     * @return День недели на английском.
     */
    public static String translateToOriginalLanguage(String day)
    {
        day = day.toLowerCase(Locale.ROOT);
        
        switch (day)
        {
            case "понедельник":
            {
                return "Monday";
            }
            
            case "вторник":
            {
                return "Tuesday";
            }
            
            case "среда":
            {
                return "Wednesday";
            }
            
            case "четверг":
            {
                return "Thursday";
            }
            
            case "пятница":
            {
                return "Friday";
            }
            
            case "суббота":
            {
                return "Saturday";
            }
            
            case "воскресенье":
            {
                return "Sunday";
            }
            
            default:
            {
                return day;
            }
        }
    }
    
    /**
     * Метод для перевода англоязычных названий дней в русский язык.
     *
     * @param day День недели на английском.
     *
     * @return День недели на русском.
     */
    public static String translateToRussianLanguage(String day)
    {
        switch (day)
        {
            case "Monday":
            {
                return "Понедельник";
            }
            
            case "Tuesday":
            {
                return "Вторник";
            }
            
            case "Wednesday":
            {
                return "Среда";
            }
            
            case "Thursday":
            {
                return "Четверг";
            }
            
            case "Friday":
            {
                return "Пятница";
            }
            
            case "Saturday":
            {
                return "Суббота";
            }
            
            case "Sunday":
            {
                return "Воскресенье";
            }
            
            default:
            {
                return day;
            }
        }
    }
    //endregion
    //endregion
}
