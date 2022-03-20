package com.authorization.uksivt_scheduler.schedule_elements;

import org.joda.time.DateTime;


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
	 * <br/>
	 * Нужен для сериализации, поэтому установлено подавление предупреждения о неиспользовании.
	 */
	@SuppressWarnings("unused")
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
	//endregion

	//region Область: Реализация интерфейса.

	/**
	 * Метод, нужный для сравнения объектов.
	 * <br><br>
	 * Реализация интерфейса Comparable.
	 *
	 * @param o Объект для сравнения.
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

	//region Область: Методы.

	/**
	 * Метод для расчета времени первой половины пары.
	 *
	 * @param dayInd Индекс дня.
	 * @param groupName Название группы.
	 * @return Строковое представление времени первой половины пары.
	 */
	public String calculateFirstLessonPartTime(Integer dayInd, String groupName)
	{
		//Обычные дни.
		if (dayInd != 5)
		{
			switch (number)
			{
				case 0:
					return "7:50 — 9:20";

				case 1:
					return "9:30 — 10:15";

				case 2:
					if (checkToFirstCourse(groupName))
					{
						return "11:15 — 12:00";
					}

					else if (checkToSecondCourse(groupName))
					{
						return "11:15 — 12:00";
					}

					else
					{
						return "12:00 — 12:45";
					}

				case 3:
					return "13:35 — 14:20";

				case 4:
					return dayInd == 2 ? "16:10 — 17:30" : "15:20 — 16:50";

				case 5:
					return dayInd == 2 ? "17:40 — 18:50" : "17:00 — 18:20";

				default:
					return dayInd == 2 ? "19:00 — 20:10" : "18:30 — 19:50";
			}
		}

		//Суббота.
		else
		{
			return switch (number)
			{
				case 0 -> "8:00 — 9:20";

				case 1 -> "9:30 — 10:50";

				case 2 -> "11:00 — 12:20";

				case 3 -> "12:30 — 13:50";

				case 4 -> "14:00 — 15:20";

				case 5 -> "15:30 — 16:50";

				default -> "17:00 — 18:20";
			};
		}
	}

	/**
	 * Метод для расчета времени второй половины пары, если это время существует.
	 *
	 * @param dayInd Индекс дня.
	 * @param groupName Название группы.
	 * @return Строковое представление времени второй половины пары, если оно существует.
	 */
	public String calculateSecondLessonPartTimeIfItExist(Integer dayInd, String groupName)
	{
		if (dayInd != 5)
		{
			switch (number)
			{
				case 1:
					return "10:20 — 11:05";

				case 2:
					if (checkToFirstCourse(groupName))
					{
						return "12:45 — 13:30";
					}

					else if (checkToSecondCourse(groupName))
					{
						return "12:05 — 12:25";
					}

					else
					{
						return "12:45 — 13:30";
					}

				case 3:
					return "14:25 — 15:10";
			}
		}

		return "";
	}

	// region Подобласть: Группа методов на проверку принадлежности.

	/**
	 * Метод для проверки группы на принадлежность к первому курсу.
	 *
	 * @param groupName Название группы.
	 * @return Их принадлежность к первому курсу.
	 */
	private Boolean checkToFirstCourse(String groupName)
	{
		DateTime currentTime = DateTime.now();

		return checkStartDateAndGroupName(groupName, currentTime);
	}

	/**
	 * Метод для проверки группы на принадлежность ко второму курсу.
	 *
	 * @param groupName Название группы.
	 * @return Их принадлежность ко второму курсу.
	 */
	private Boolean checkToSecondCourse(String groupName)
	{
		DateTime currentTime = DateTime.now();
		currentTime = currentTime.plusYears(-1);

		return checkStartDateAndGroupName(groupName, currentTime);
	}

	/**
	 * Вынесенный метод для проверки групп на принадлежность к какому-либо курсу.
	 *
	 * @param groupName Название группы.
	 * @param currentTime Текущее время (определяет то, на какую принадлежность нужно проверять).
	 *
	 * @return Значение, отвечающее за принадлежность к тому или иному курсу.
	 */
	private Boolean checkStartDateAndGroupName(String groupName, DateTime currentTime)
	{
		if (currentTime.getMonthOfYear() < 9)
		{
			currentTime = currentTime.plusYears(-1);
		}

		String yearPath = Integer.toString(currentTime.getYear() % 100);
		return groupName.contains(yearPath);
	}
	//endregion
	//endregion
}
