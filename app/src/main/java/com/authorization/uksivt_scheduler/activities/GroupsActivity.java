package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.data_getter.StandardScheduler;
import com.authorization.uksivt_scheduler.user_data.Group;
import com.authorization.uksivt_scheduler.user_data.UserData;

import java.util.List;


/**
 * Класс, содержащий логику для работы с "activity_groups".
 * <br/> <br/>
 * Обязательные передаваемые данные:
 * <ol>
 *     <li>"course_name" — Отделение обучения;</li>
 *     <li>"subFolder_name" — Тип группы.</li>
 * </ol>
 */
public class GroupsActivity extends AppCompatActivity
{
	//region Область: Поля.
	/**
	 * Поле, содержащее текущее выбранное направление.
	 */
	private String course;

	/**
	 * Поле, содержащее текущую выбранную подпапку групп.
	 */
	private String subFolder;

	/**
	 * Поле, содержащее экземпляр класса "StandardScheduler", нужный для получения списка групп.
	 */
	private StandardScheduler standardScheduler;
	//endregion

	//region Область: События.

	/**
	 * Событие, происходящее при создании окна.
	 *
	 * @param savedInstanceState Последнее сохраненное состояние приложения.
	 */
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);

		course = getIntent().getStringExtra("course_name");
		subFolder = getIntent().getStringExtra("subFolder_name");
		standardScheduler = new StandardScheduler(this);

		insertButtonsToLayout(getGroupsListFromCourse());
	}
	//endregion

	//region Область: Внутренние методы.

	/**
	 * Метод, нужный для получения списка групп для указанного направления.
	 *
	 * @return Список групп.
	 */
	private List<String> getGroupsListFromCourse()
	{
		List<String> groups = standardScheduler.getGroupsWithFolder(course, subFolder);

		for (int i = 0; i < groups.size(); i++)
		{
			groups.set(i, groups.get(i).replace(".json", ""));
		}

		return groups;
	}

	/**
	 * Метод, нужный для вставки кнопок соответствующих групп на окно.
	 *
	 * @param groups Список групп для вставки.
	 */
	private void insertButtonsToLayout(List<String> groups)
	{
		LinearLayout layout = findViewById(R.id.groups_list);

		for (int i = 0; i < groups.size(); i++)
		{
			Button button = new Button(this);
			button.setText(groups.get(i));
			button.setTextColor(getColor(R.color.white));
			button.setBackgroundColor(getColor(R.color.main_gray));
			button.setOnClickListener(new OnClickListener()
			{
				/**
				 * Событие, происходящее при нажатии на одну из кнопок с группой.
				 *
				 * @param view Элемент, вызвавщий событие.
				 */
				public void onClick(View view)
				{
					Intent newWindow = new Intent(view.getContext(), DaySelectorActivity.class);
					newWindow.putExtra("folder", course);
					newWindow.putExtra("subFolder", subFolder);
					newWindow.putExtra("group", ((Button)view).getText());

					startActivity(newWindow);
				}
			});

			button.setTextSize(48);
			button.setOnLongClickListener(v ->
			{
				Group newFavorite = new Group(course, subFolder, button.getText().toString());
				Integer index = UserData.checkToContain(newFavorite);

				if (index == -1)
				{
					UserData.FavoritesGroups.add(new Group(course, subFolder, button.getText().toString()));
					UserData.saveFavoritesListToFile();

					Toast.makeText(this, getString(R.string.group_added_to_favorites), Toast.LENGTH_SHORT).show();
				}

				else
				{
					UserData.FavoritesGroups.remove((int)index);
					UserData.saveFavoritesListToFile();

					Toast.makeText(this, getString(R.string.group_removed_from_favorites_list), Toast.LENGTH_SHORT).show();
				}

				return false;
			});

			layout.addView(button);

			//Чтобы не добавить "пробел" в конец списка делаем проверку:
			if (i + 1 < groups.size())
			{
				Space space = new Space(this);
				space.setMinimumHeight(50);

				layout.addView(space);
			}
		}
	}
	//endregion
}
