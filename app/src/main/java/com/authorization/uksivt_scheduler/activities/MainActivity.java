package com.authorization.uksivt_scheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.data_getter.ScheduleApiConnector;
import com.authorization.uksivt_scheduler.user_data.UserData;

import org.joda.time.DateTime;

import java.io.IOException;


/**
 * Класс, содержащий логику для стартового окна.
 */
public class MainActivity extends AppCompatActivity
{
	//region Область: События запуска.

	/**
	 * Событие, происходящее при запуске приложения.
	 *
	 * @param savedInstanceState Сохраненное состояние приложения.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Элемент оптимизации использования API:
		new Thread(() ->
		{
			UserData.initializeUserData(this);

			try
			{
				new ScheduleApiConnector(DateTime.now().dayOfWeek().get() - 1, "19П-3").getChanges();
			}

			catch (IOException e)
			{
				Log.println(Log.ERROR, "SchedulePreUploadError", e.getMessage());
			}
		}).start();
	}
	//endregion

	//region Область: Обработчики событий.

	/**
	 * Событие, происходящее при нажатии на кнопку избранных групп.
	 *
	 * @param sender Объект, вызвавший событие.
	 */
	public void goToFavoritesList(View sender)
	{
		Intent newWindow = new Intent(this, FavoritesActivity.class);

		startActivity(newWindow);
	}

	/**
	 * Событие, происходящее при нажатии на одну из кнопок с курсом.
	 * <br>
	 * Перенаправляет пользователя к меню выбора группы, относящейся к этому курсу.
	 *
	 * @param sender Элемент, вызвавший событие.
	 */
	public void oneOfButtonsAreClicked(View sender)
	{
		//region Подобласть: Переменные с ID кнопок.
		Integer law = R.id.law_course_button;
		Integer general = R.id.general_course_button;
		Integer economy = R.id.economy_course_button;
		Integer technical = R.id.technical_course_button;
		Integer programming = R.id.programming_course_button;
		//endregion

		//region Подобласть: Прочие переменные.
		String course = "";
		Button currentSender = (Button)sender;
		//endregion

		//region Подобласть: Проверка на нажатую кнопку.
		if (law.equals(currentSender.getId()))
		{
			course = "law";
		}

		else if (general.equals(currentSender.getId()))
		{
			course = "general";
		}

		else if (economy.equals(currentSender.getId()))
		{
			course = "economy";
		}

		else if (technical.equals(currentSender.getId()))
		{
			course = "technical";
		}

		else if (programming.equals(currentSender.getId()))
		{
			course = "programming";
		}

		else
		{
			Toast.makeText(this, getString(R.string.error_occurred),
			Toast.LENGTH_SHORT).show();
		}
		//endregion

		Intent newWindow = new Intent(this, SubFoldersActivity.class);
		newWindow.putExtra("course_name", course);

		startActivity(newWindow);
	}
	//endregion
}
