package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.change_elements.ChangesOfDay;
import com.authorization.uksivt_scheduler.custom_views.LessonListAdapter;
import com.authorization.uksivt_scheduler.data_getter.ScheduleApiConnector;
import com.authorization.uksivt_scheduler.data_getter.StandardScheduler;
import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Days;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Класс, определяющий логику для "activity_schedule_viewer".
 */
public class FinalScheduleActivity extends AppCompatActivity
{
	//region Область: Поля.
	/**
	 * Поле, содержащее расписание на день.
	 */
	private DaySchedule schedule;

	/**
	 * Поле, содержащее замены на выбранный день.
	 * <br/>
	 * Нужно для возможности вывода информации о заменах.
	 */
	private ChangesOfDay changes;

	/**
	 * Поле, содержащее элемент, в который будут помещаться все пары.
	 */
	private ListView lessonsList;

	/**
	 * Поле, отвечающее за прерывание потока с получением замен.
	 * <br/>
	 * Так как нормального способа прерывать потоки в Java нет, приходится использовать "флаги" состояния.
	 */
	private Boolean changesThreadIsInterrupted = false;
	//endregion

	//region Область: События.
	//region Подобласть: Группа методов "onCreate".

	/**
	 * Событие, происходящее при создании окна.
	 *
	 * @param savedInstanceState Последнее сохраненное состояние приложения.
	 */
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_viewer);
		Intent parent = getIntent();

		//Обработка смены ориентации экрана.
		if (savedInstanceState != null)
		{
			if (tryToRestoreStateFromBundle(savedInstanceState))
			{
				return;
			}
		}

		initializeScheduleFieldWithOriginalValues(parent);
		initializeScheduleViewAndStartChangesReceiving(parent);
	}

	/**
	 * Инициализирует значения поля "schedule" оригинальными значениями (расписание из ассетов, без учета замен).
	 *
	 * @param parent Родительское окно.
	 */
	private void initializeScheduleFieldWithOriginalValues(Intent parent)
	{
		try
		{
			schedule = new StandardScheduler(this).getDaySchedule
			(parent.getStringExtra("folder"), parent.getStringExtra("subFolder"),
			parent.getStringExtra("group"), Days.fromString(parent.getStringExtra("day")));
		}

		catch (IOException e)
		{
			schedule = DaySchedule.getEmptySchedule();
		}
	}

	/**
	 * Инициализирует элемент списка с парами (вставляет оригинальные пары) и запускает метод получения замен.
	 *
	 * @param parent Родительское окно.
	 */
	private void initializeScheduleViewAndStartChangesReceiving(Intent parent)
	{
		lessonsList = findViewById(R.id.schedule_lessons_list);

		insertLessonsToActivity();
		startChangesThread(parent);
	}

	/**
	 * Позволяет восстановить состояние из объекта типа "Bundle".
	 * <br/>
	 * В первую очередь — после смены ориентации экрана.
	 *
	 * @return Успешность восстановления из сохраненной копии.
	 */
	private Boolean tryToRestoreStateFromBundle(Bundle savedInstanceState)
	{
		try
		{
			ObjectMapper serializer = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES).build();

			lessonsList = findViewById(R.id.schedule_lessons_list);
			schedule = serializer.readValue(savedInstanceState.getString("schedule"), DaySchedule.class);
			insertLessonsToActivity();

			return true;
		}

		catch (Exception exception)
		{
			exception.printStackTrace();

			return false;
		}
	}
	//endregion

	/**
	 * Событие, происходящее при смене данного окна на любое другое.
	 * <br/>
	 * Прерывает процесс получения замен.
	 */
	@Override
	protected void onPause()
	{
		super.onPause();

		changesThreadIsInterrupted = true;
	}

	/**
	 * Событие, происходящее в момент уничтожения окна.
	 * <br/>
	 * Нужно для сохранения данных в Bundle.
	 *
	 * @param outState Сохраняемое состояние.
	 */
	@Override
	public void onSaveInstanceState(@NonNull Bundle outState)
	{
		super.onSaveInstanceState(outState);
		ObjectMapper serializer = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES).build();

		try
		{
			outState.putString("schedule", serializer.writeValueAsString(schedule));
		}

		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
	}
	//endregion

	//region Область: Методы.

	/**
	 * Метод для вставки расписания в окно.
	 */
	private void insertLessonsToActivity()
	{
		TextView header = findViewById(R.id.schedule_day_header);
		header.setText(String.format("%s%s", getString(R.string.current_day_is),
		Days.toString(schedule.day)));

		//Удаляем все пустые пары (почти как LINQ!):
		schedule.lessons.removeIf(lesson -> lesson.getName() == null);

		LessonListAdapter adapter = new LessonListAdapter(this, getIntent().getStringExtra("group"), schedule);
		lessonsList.setAdapter(adapter);
	}

	//region Подобласть: Группа методов "startChangesThread".

	/**
	 * Метод, нужный для запуска потока замен, который обратывает замены.
	 * <br/>
	 * Представляет собой ту же логику, которая ранее была представлена в событии onCreate(), но теперь она вынесена в отдельный метод.
	 */
	private void startChangesThread(Intent parent)
	{
		new Thread(() ->
		{
			runOnUiThread(() -> Toast.makeText(this, getString(R.string.changes_receiving_begin),
			Toast.LENGTH_SHORT).show());

			DaySchedule newSchedule = initializeChangesFieldAndReturnMergedSchedule(parent.getStringExtra("group"),
			Days.getIndexByValue(Days.fromString(parent.getStringExtra("day"))));

			//Делаем проверку на значение замен (чтобы в случае отсутствия ответа от API этот блок не был вызван).
			if (changes != null)
			{
				assertChangesAndInsertIfProperly(newSchedule);
				insertCurrentScheduleTimeToHeader();
			}

			//Делаем проверку на прерванность потока (почему-то способ с обработкой исключения так и не сработал).
			if (changesThreadIsInterrupted)
			{
				runOnUiThread(() -> Toast.makeText(this,
				getString(R.string.changes_receiving_interrupted), Toast.LENGTH_SHORT).show());
			}
		}).start();
	}

	/**
	 * Проверяет полученные значения замен и, если они есть, вставляет их в текущее окно.
	 *
	 * @param newSchedule Финальное (объединенное с заменами) расписание.
	 */
	private void assertChangesAndInsertIfProperly(DaySchedule newSchedule)
	{
		if (!changes.getChangesFound())
		{
			runOnUiThread(() -> Toast.makeText(this,
			getString(R.string.there_is_no_available_changes_file), Toast.LENGTH_SHORT).show());
		}

		else if (newSchedule != null && newSchedule.equals(schedule))
		{
			runOnUiThread(() -> Toast.makeText(this,
			getString(R.string.there_is_no_changes), Toast.LENGTH_SHORT).show());
		}

		else
		{
			runOnUiThread(() ->
			{
				schedule = newSchedule;

				if (!changesThreadIsInterrupted)
				{
					insertLessonsToActivity();

					Toast.makeText(this, getString(R.string.changes_received_successfully), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * Вставляет полученное значение даты от API в заголовок окна.
	 * <br/>
	 * Этот метод — это вынесенная логика из startChangesThread(). Это сделано для удобства понимания кода.
	 */
	private void insertCurrentScheduleTimeToHeader()
	{
		TextView header = findViewById(R.id.schedule_day_header);
		SimpleDateFormat formatter = new SimpleDateFormat("(dd.MM.yyyy!)", Locale.getDefault());
		formatter.setTimeZone(TimeZone.getTimeZone("UTC+00:00"));

		runOnUiThread(() -> header.setText(String.format(Locale.getDefault(), "%s %s", header.getText(),
		formatter.format(changes.getChangesDate()))));
	}
	//endregion

	/**
	 * Метод для получения замен для указанного дня и их слияния с полем с расписанием.
	 *
	 * @param groupName Название нужной группы.
	 * @param day       Индекс дня.
	 * @return Расписание на день с учетом замен.
	 */
	private DaySchedule initializeChangesFieldAndReturnMergedSchedule(String groupName, Integer day)
	{
		try
		{
			changes = new ScheduleApiConnector(day, groupName).getChanges();
		}

		catch (IOException e)
		{
			runOnUiThread(() -> Toast.makeText(this, getString(R.string.changes_get_exception), Toast.LENGTH_SHORT).show());

			return null;
		}

		//Если замен нет, то нужно вернуть базовое расписание для корректного сравнения объектов.
		if (changes.getNewLessons().size() == ChangesOfDay.DefaultChanges.getNewLessons().size())
		{
			return schedule;
		}

		return schedule.mergeChanges(changes.getNewLessons(), changes.getAbsoluteChanges());
	}
	//endregion
}
