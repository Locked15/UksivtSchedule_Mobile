package com.authorization.uksivt_scheduler.user_data;

import android.content.Context;

import com.authorization.uksivt_scheduler.R;

import org.joda.time.DateTime;


/**
 * Класс, представляющий одну группу.
 */
public class Group
{
	//region Область: Поля.

	/**
	 * Поле, содержащее отделение группы.
	 */
	public String Branch;

	/**
	 * Поле, содержащее принадлежность группы.
	 */
	public String SubGroup;

	/**
	 * Поле, содержащее название группы.
	 */
	public String GroupName;
	//endregion

	//region Область: Конструкторы.

	/**
	 * Конструктор класса по умолчанию.
	 * <br/>
	 * Нужен для работы сериализатора.
	 */
	@SuppressWarnings("unused")
	public Group()
	{

	}

	/**
	 * Конструктор класса.
	 *
	 * @param branch    Отделение обучения.
	 * @param subGroup  Подгруппа (например: 'П', 'БД', 'ЗИО').
	 * @param groupName Название группы.
	 */
	public Group(String branch, String subGroup, String groupName)
	{
		Branch = branch;
		SubGroup = subGroup;
		GroupName = groupName;
	}
	//endregion

	//region Область: Методы.

	/**
	 * Метод для расчета номера курса у указанной группы.
	 *
	 * @return Числовое значение — номер курса.
	 */
	public Integer calculateCourse()
	{
		Integer course = 1;
		DateTime currentTime = DateTime.now();
		currentTime = currentTime.getMonthOfYear() < 9 ? currentTime.plusYears(-1) : currentTime;

		while (course <= 4 && !GroupName.contains(Integer.toString(currentTime.getYear()).substring(2)))
		{
			currentTime = currentTime.plusYears(-1);

			++course;
		}

		return course;
	}

	/**
	 * Метод для получения "красивого" названия отделения, а не технического.
	 *
	 * @param context Контекст приложения для получения строк из ресурсов.
	 * @return Красивое название отделения.
	 */
	public String getPrettyBranchName(Context context)
	{
		return switch (Branch)
		{
			case "law" -> context.getString(R.string.law_course);
			case "general" -> context.getString(R.string.general_course);
			case "economy" -> context.getString(R.string.economy_course);

			case "technical" -> context.getString(R.string.technical_course);
			default -> context.getString(R.string.informatics_course);
		};
	}
	//endregion
}
