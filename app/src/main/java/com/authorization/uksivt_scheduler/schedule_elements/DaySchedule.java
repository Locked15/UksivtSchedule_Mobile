package com.authorization.uksivt_scheduler.schedule_elements;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


/**
 * Класс, предоставляющий логику для расписания одного конкретного дня.
 */
public class DaySchedule
{
	//region Область: Поля класса.
	/**
	 * Поле, содержащее день, который описывается.
	 */
	public Days day;

	/**
	 * Поле, содержащее пары, проводимые в этот день.
	 */
	public ArrayList<Lesson> lessons;
	//endregion

	//region Область: Конструктор класса.

	/**
	 * Конструктор класса по умолчанию.
	 */
	public DaySchedule()
	{

	}

	/**
	 * Конструктор класса.
	 *
	 * @param day     День недели.
	 * @param lessons Пары этого дня.
	 */
	public DaySchedule(Days day, ArrayList<Lesson> lessons)
	{
		this.day = day;
		this.lessons = lessons;
	}
	//endregion

	//region Область: Методы.

	/**
	 * Метод, позволяющий слить оригинальное расписание и замены для него.
	 *
	 * @param changes         Замены.
	 * @param absoluteChanges Замены на весь день?
	 * @return Объединенное расписание с заменами.
	 */
	public DaySchedule mergeChanges(ArrayList<Lesson> changes, Boolean absoluteChanges)
	{
		//Чтобы избежать проблем (замены на более ранние пары, чем есть), нужно создать ...
		//... "полный" шаблон со всеми 7 парами, чтобы индексы и номера пар ...
		//... были синхронизированы.
		ArrayList<Lesson> mergedSchedule = getEmptySchedule().lessons;

		if (absoluteChanges)
		{
			changes.forEach(lesson -> lesson.setLessonChanged(true));

			//Чтобы избавиться от возможных проблем со ссылками в будущем, ...
			//... создаем новый объект:
			return new DaySchedule(day, fillEmptyLessons(changes));
		}

		//region Подобласть: Подготовка пустого расписания.
		for (int i = 0; i < lessons.size(); i++)
		{
			for (int j = 0; j < mergedSchedule.size(); j++)
			{
				if (lessons.get(i).getNumber().equals(mergedSchedule.get(j).getNumber()))
				{
					mergedSchedule.set(j, lessons.get(i));
				}
			}
		}
		//endregion

		for (Lesson change : changes)
		{
			Integer lessonIndex = change.getNumber();

			if (change.getName().toLowerCase(Locale.ROOT).equals("нет"))
			{
				change.setName(null);
			}

			mergedSchedule.set(lessonIndex, change);
			mergedSchedule.get(lessonIndex).setLessonChanged(true);
		}

		mergedSchedule.removeIf(element -> element.getName() == null);
		Collections.sort(mergedSchedule);
		return new DaySchedule(this.day, mergedSchedule);
	}

	/**
	 * Если замены на весь день, то возвращаемое значение содержит только замены.
	 * Чтобы добавить пустые пары, используется этот метод.
	 *
	 * @param lessons Расписание замен.
	 * @return Расписание замен с заполнением.
	 */
	private ArrayList<Lesson> fillEmptyLessons(ArrayList<Lesson> lessons)
	{
		for (int i = 0; i < 7; i++)
		{
			Boolean missing = true;

			for (Lesson lesson : lessons)
			{
				if (lesson.getNumber() == i)
				{
					missing = false;

					break;
				}
			}

			if (missing)
			{
				lessons.add(new Lesson(i, null, null, null));
			}
		}

		//Добавленные "пустые" пары находятся в конце списка, так что ...
		//... мы сортируем их в порядке номера пар:
		Collections.sort(lessons);
		return lessons;
	}

	/**
	 * Метод, возвращающий строковое представление объекта.
	 *
	 * @return Строковое представление отправленного объекта.
	 */
	@NonNull
	@Override
	public String toString()
	{
		StringBuilder toReturn = new StringBuilder(Days.toString(day) + ":\n" +
		"{");

		for (Lesson lesson : lessons)
		{
			toReturn.append("\n\t{").append("\n\t\tНомер пары: ").append(lesson.getNumber())
			.append("\n\t\t").append("Название пары: ").append(lesson.getName()).append("\n\t\t")
			.append("Кабинет: ").append(lesson.getPlace()).append("\n\t\t")
			.append("Преподаватель: ").append(lesson.getTeacher()).append("\n\t}");
		}

		toReturn.append("\n}");

		return toReturn.toString();
	}
	//endregion

	//region Область: Статические методы.

	/**
	 * Метод для получения дня с "пустым" расписанием.
	 *
	 * @return Пустое расписание.
	 */
	public static DaySchedule getEmptySchedule()
	{
		ArrayList<Lesson> lessons = new ArrayList<>(7);
		lessons.add(new Lesson(0));
		lessons.add(new Lesson(1));
		lessons.add(new Lesson(2));
		lessons.add(new Lesson(3));
		lessons.add(new Lesson(4));
		lessons.add(new Lesson(5));
		lessons.add(new Lesson(6));

		return new DaySchedule(Days.Sunday, lessons);
	}
	//endregion
}
