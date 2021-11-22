package com.authorization.uksivt_scheduler.data_reader;


/**
 * Класс, определяющие исключение попытки обработать документ с заменами с неправильным днем.
 */
class WrongDayInDocumentException extends Exception
{
    /**
     * Конструктор класса.
     *
     * @param message Сообщение исключения.
     */
    public WrongDayInDocumentException(String message)
    {
        //Вызываем конструктор базового класса:
        super(message);
    }
}
