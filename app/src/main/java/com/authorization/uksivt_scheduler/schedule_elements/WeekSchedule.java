package com.authorization.uksivt_scheduler.schedule_elements;

import java.util.ArrayList;


/**
 * Класс, представляющий логику для недельного расписания группы.
 */
public class WeekSchedule
{
	//region Область: Поля.
	/**
	 * Поле, содержащее название группы.
	 */
	private String groupName;

	/**
	 * Поле, содержащее расписание группы.
	 */
	private ArrayList<DaySchedule> days;
	//endregion

	//region Область: Свойства поля "groupName".

	/**
	 * Get-свойство для поля groupName.
	 *
	 * @return Значение поля "groupName".
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * Set-свойство для поля "groupName".
	 *
	 * @param newValue Новое значение поля.
	 */
	public void setGroupName(String newValue)
	{
		groupName = newValue;
	}
	//endregion

	//region Область: Свойства поля "days".

	/**
	 * Get-свойство для поля "days".
	 *
	 * @return Значение поля "days".
	 */
	public ArrayList<DaySchedule> getDays()
	{
		return days;
	}

	/**
	 * Set-свойство для поля "days".
	 *
	 * @param days Новое значение для поля.
	 */
	public void setDays(ArrayList<DaySchedule> days)
	{
		this.days = days;
	}
	//endregion

	//region Область: Конструкторы.

	/**
	 * Конструктор класса по-умолчанию.
	 */
	public WeekSchedule()
	{

	}

	/**
	 * Конструктор класса.
	 *
	 * @param groupName Название группы.
	 * @param days      Расписание группы.
	 */
	public WeekSchedule(String groupName, ArrayList<DaySchedule> days)
	{
		this.days = days;
		this.groupName = groupName;
	}
	//endregion
}
