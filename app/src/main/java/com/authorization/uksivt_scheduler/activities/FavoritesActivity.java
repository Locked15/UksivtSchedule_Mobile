package com.authorization.uksivt_scheduler.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.custom_views.FavoritesListAdapter;
import com.authorization.uksivt_scheduler.user_data.Group;
import com.authorization.uksivt_scheduler.user_data.UserData;

import java.util.List;


/**
 * Класс, содержащий логику для "activity_favorites".
 */
public class FavoritesActivity extends AppCompatActivity
{
	/**
	 * Событие создания окна.
	 *
	 * @param savedInstanceState Последнее сохраненное состояние окна.
	 */
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		insertFavoritesToActivity();
	}

	/**
	 * Метод для вставки избранных групп в окно.
	 */
	private void insertFavoritesToActivity()
	{
		FavoritesListAdapter adapter = new FavoritesListAdapter(this, UserData.FavoritesGroups);
		ListView favoritesList = findViewById(R.id.favorite_groups_list);

		favoritesList.setAdapter(adapter);
	}
}
