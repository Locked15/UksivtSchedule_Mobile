package com.authorization.uksivt_scheduler.user_data;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;


/**
 * Класс, содержащий пользовательские данные об избранных группах.
 */
public class UserData
{
	//region Область: Поля.

	/**
	 * Поле, содержащее путь к файлу с избранными группами.
	 */
	public static String fileName;

	/**
	 * Список с избранными группами.
	 */
	public static List<Group> FavoritesGroups;
	//endregion

	//region Область: Конструкторы.

	public static void initializeUserData(Context context)
	{
		List<Group> tempList;

		try
		{
			fileName = context.getFileStreamPath("Favorites.json").getPath();
			FileInputStream stream = new FileInputStream(context.getFileStreamPath("Favorites.json"));
			ObjectMapper serializer = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES).build();

			tempList = serializer.readValue(stream, new TypeReference<>()
			{
			});
		}

		catch (IOException e)
		{
			e.printStackTrace();

			tempList = new ArrayList<>(1);
		}

		FavoritesGroups = tempList;
	}
	//endregion

	//region Область: Методы.

	/**
	 * Статический метод для сохранения списка избранных групп.
	 */
	public static void saveFavoritesListToFile()
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(fileName, false);
			ObjectMapper serializer = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES).build();

			serializer.writerWithDefaultPrettyPrinter().writeValue(stream, FavoritesGroups);
		}

		catch (IOException e)
		{
			e.printStackTrace();

			Log.println(Log.ERROR, "SaveFavoritesError", "При попытке сохранить избранную группу, произошла ошибка: " + e.getMessage());
		}
	}

	/**
	 * Метод для проверки указанной группы на наличие в списке.
	 *
	 * @param checkGroup Группа, которую нужно проверить.
	 * @return Индекс этой группы в списке (-1, если группа не найдена).
	 */
	public static Integer checkToContain(Group checkGroup)
	{
		for (Group group : FavoritesGroups)
		{
			if (group.GroupName.equals(checkGroup.GroupName))
			{
				return FavoritesGroups.indexOf(group);
			}
		}

		return -1;
	}
	//endregion
}
