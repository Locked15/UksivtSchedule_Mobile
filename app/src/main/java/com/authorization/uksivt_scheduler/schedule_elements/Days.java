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
	 * @return Значение, соответствующее данному индексу.
	 * @throws IndexOutOfBoundsException Введенный индекс превышает количество значений.
	 */
	public static Days getValueByIndex(Integer i) throws IndexOutOfBoundsException
	{
		return switch (i)
		{
			case 0 -> Monday;
			case 1 -> Tuesday;
			case 2 -> Wednesday;
			case 3 -> Thursday;
			case 4 -> Friday;

			case 5 -> Saturday;
			case 6 -> Sunday;

			default -> throw new IllegalArgumentException("Введенн некорректный индекс!");
		};
	}

	/**
	 * Метод для получения индекса для указанного дня.
	 *
	 * @param day День, индекс которого нужно получить.
	 * @return Значение, соответствующее данному индексу.
	 * @throws IndexOutOfBoundsException Введенный индекс превышает количество значений.
	 */
	public static Integer getIndexByValue(Days day)
	{
		return switch (day)
		{
			case Monday -> 0;
			case Tuesday -> 1;
			case Wednesday -> 2;
			case Thursday -> 3;
			case Friday -> 4;

			case Saturday -> 5;
			case Sunday -> 6;
		};
	}
	//endregion

	//region Подобласть: Преобразования из/в строки.

	/**
	 * Метод для преобразования Enum-значения в строковый вид.
	 *
	 * @param day Элемент перечисления Enum.
	 * @return Строковое представление перечисления.
	 */
	public static String toString(Days day)
	{
		return switch (day)
		{
			case Monday -> translateToRussianLanguage("Monday");
			case Tuesday -> translateToRussianLanguage("Tuesday");
			case Wednesday -> translateToRussianLanguage("Wednesday");
			case Thursday -> translateToRussianLanguage("Thursday");
			case Friday -> translateToRussianLanguage("Friday");

			case Saturday -> translateToRussianLanguage("Saturday");
			case Sunday -> translateToRussianLanguage("Sunday");
		};
	}

	/**
	 * Метод для преобразования Enum-значения в строковый вид.
	 *
	 * @param day Элемент перечисления Enum.
	 * @return Строковое представление перечисления.
	 */
	public static Days fromString(String day)
	{
		day = translateToOriginalLanguage(day);

		return switch (day)
		{
			case "Monday" -> Monday;
			case "Tuesday" -> Tuesday;
			case "Wednesday" -> Wednesday;
			case "Thursday" -> Thursday;
			case "Friday" -> Friday;

			case "Saturday" -> Saturday;
			case "Sunday" -> Sunday;

			default -> null;
		};
	}
	//endregion

	//region Подобласть: Перевод значений на русский и обратно.

	/**
	 * Метод для перевода русскоязычных названий дней в оригинальный (английский) язык.
	 *
	 * @param day День недели на русском.
	 * @return День недели на английском.
	 */
	public static String translateToOriginalLanguage(String day)
	{
		day = day.toLowerCase(Locale.ROOT);

		return switch (day)
		{
			case "понедельник" -> "Monday";
			case "вторник" -> "Tuesday";
			case "среда" -> "Wednesday";
			case "четверг" -> "Thursday";
			case "пятница" -> "Friday";

			case "суббота" -> "Saturday";
			case "воскресенье" -> "Sunday";

			default -> day;
		};
	}

	/**
	 * Метод для перевода англоязычных названий дней в русский язык.
	 *
	 * @param day День недели на английском.
	 * @return День недели на русском.
	 */
	public static String translateToRussianLanguage(String day)
	{
		day = day.toLowerCase(Locale.ROOT);

		return switch (day)
		{
			case "monday" -> "Понедельник";
			case "tuesday" -> "Вторник";
			case "wednesday" -> "Среда";
			case "thursday" -> "Четверг";
			case "friday" -> "Пятница";

			case "saturday" -> "Суббота";
			case "sunday" -> "Воскресенье";

			default -> day;
		};
	}
	//endregion
	//endregion
}
