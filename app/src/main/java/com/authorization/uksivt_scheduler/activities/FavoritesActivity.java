package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.custom_views.FavoritesListAdapter;
import com.authorization.uksivt_scheduler.interfaces.FavoriteGroupClickListener;
import com.authorization.uksivt_scheduler.user_data.Group;
import com.authorization.uksivt_scheduler.user_data.UserData;


/**
 * Класс, содержащий логику для "activity_favorites".
 */
public class FavoritesActivity extends AppCompatActivity implements FavoriteGroupClickListener
{
	/**
	 * Поле, содержащее адаптер для списка избранных групп.
	 */
	private FavoritesListAdapter adapter;

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
		adapter = new FavoritesListAdapter(this, UserData.FavoritesGroups, this);
		ListView favoritesList = findViewById(R.id.favorite_groups_list);

		favoritesList.setAdapter(adapter);
	}

	/**
	 * Событие нажатия на избранную группу.
	 *
	 * @param data Данные о группе.
	 */
	@Override
	public void favoriteGroupClicked(Group data)
	{
		Intent newWindow = new Intent(this, DaySelectorActivity.class);
		newWindow.putExtra("folder", data.Branch);
		newWindow.putExtra("subFolder", data.SubGroup);
		newWindow.putExtra("group", data.GroupName);

		startActivity(newWindow);
	}

	/**
	 * Событие долгого нажатия на избранную группу.
	 *
	 * @param data Данные о группе.
	 */
	@Override
	public void favoriteGroupLongClick(Group data)
	{
		UserData.FavoritesGroups.remove(data);
		UserData.saveFavoritesListToFile();
		adapter.notifyDataSetChanged();

		Toast.makeText(this, getString(R.string.group_removed_from_favorites_list),
		Toast.LENGTH_SHORT).show();
	}
}
