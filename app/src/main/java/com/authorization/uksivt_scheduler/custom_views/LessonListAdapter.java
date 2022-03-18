package com.authorization.uksivt_scheduler.custom_views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Lesson;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Класс, представляющий адаптер для списка пар.
 */
public class LessonListAdapter extends BaseAdapter
{
	//region Область: Поля класса.
	/**
	 * Поле, содержащее объект, нужный для работы нестандартного списка.
	 */
	private final LayoutInflater inflater;

	/**
	 * Список с парами для заполнения.
	 */
	private final ArrayList<Lesson> lessons;
	//endregion

	//region Область: Конструкторы класса.

	/**
	 * Конструктор класса.
	 *
	 * @param context Контекст, в котором создается ListView.
	 * @param lessons Список пар для заполнения таблицы.
	 */
	public LessonListAdapter(Context context, ArrayList<Lesson> lessons)
	{
		this.lessons = lessons;

		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Конструктор класса.
	 *
	 * @param context  Контекст, в котором создается ListView.
	 * @param schedule Полное расписание на день.
	 */
	public LessonListAdapter(Context context, DaySchedule schedule)
	{
		this.lessons = schedule.lessons;

		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	//endregion

	//region Область: Реализация базового класса.

	/**
	 * Метод для получения размера списка с источником данных.
	 *
	 * @return Размер этого списка.
	 */
	@Override
	public int getCount()
	{
		return lessons.size();
	}

	/**
	 * Метод для получения нужного элемента по его индексу.
	 *
	 * @param i Индекс элемента для получения.
	 * @return Элемент по указанному индексу.
	 * @throws IndexOutOfBoundsException Указанный индекс был за пределами границ списка.
	 */
	@Override
	public Lesson getItem(int i) throws IndexOutOfBoundsException
	{
		return lessons.get(i);
	}

	/**
	 * Метод для связывания индекса с типом "long".
	 *
	 * @param i Индекс для связывания.
	 * @return Связанный с типом "long" индекс.
	 */
	@Override
	public long getItemId(int i)
	{
		return i;
	}

	/**
	 * Метод для установки внешнего вида элемента списка.
	 * <br>
	 * После установки вида, происходит наполнение данными.
	 *
	 * @param i         Индекс элемента.
	 * @param view      Элемент, вызвавший событие.
	 * @param viewGroup Родительский элемент.
	 * @return Готовый и наполненный элемент.
	 */
	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		//region Подобласть: Переменные элементов.
		Lesson data;
		ViewHolder holder;
		View toReturn = view;
		//endregion

		if (toReturn == null)
		{
			holder = new ViewHolder();
			toReturn = inflater.inflate(R.layout.lessons_element, viewGroup, false);

			//region Подобласть: Инициализация значений "holder".
			holder.background = toReturn.findViewById(R.id.template_background);
			holder.number = toReturn.findViewById(R.id.template_number);
			holder.name = toReturn.findViewById(R.id.template_name);
			holder.teacher = toReturn.findViewById(R.id.template_teacher);
			holder.place = toReturn.findViewById(R.id.template_place);
			//endregion

			toReturn.setTag(holder);
		}

		holder = (ViewHolder)toReturn.getTag();
		data = lessons.get(i);

		//region Подобласть: Установка значений полей.
		holder.background.setBackgroundColor(data.getLessonChanged() ?
		Color.rgb(139, 202, 235) : Color.rgb(255, 255, 255));

		holder.number.setText(String.format(Locale.getDefault(), "%d", data.getNumber()));
		holder.name.setText(data.getName());
		holder.teacher.setText(data.getTeacher());
		holder.place.setText(data.getPlace());
		//endregion

		return toReturn;
	}
	//endregion

	//region Область: ViewHolder.

	/**
	 * Внутренний статический класс, нужный для оптимизации кода.
	 */
	private static class ViewHolder
	{
		/**
		 * Поле, содержащее задний фон элемента.
		 */
		private ConstraintLayout background;

		/**
		 * Поле, содержащее номер пары.
		 */
		private TextView number;

		/**
		 * Поле, содержащее название пары.
		 */
		private TextView name;

		/**
		 * Поле, содержащее имя преподавателя.
		 */
		private TextView teacher;

		/**
		 * Поле, содержащее место проведения пары.
		 */
		private TextView place;
	}
	//endregion
}
