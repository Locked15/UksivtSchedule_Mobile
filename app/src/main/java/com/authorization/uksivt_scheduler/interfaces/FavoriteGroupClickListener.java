package com.authorization.uksivt_scheduler.interfaces;

import com.authorization.uksivt_scheduler.user_data.Group;


/**
 * Интерфейс, представляющий контракт на реализацию прослушивателей нажатий на избранные группы.
 */
public interface FavoriteGroupClickListener
{
	/**
	 * Событие нажатия на избранную группу.
	 *
	 * @param data Данные о группе.
	 */
	void favoriteGroupClicked(Group data);

	/**
	 * Событие долгого нажатия на избранную группу.
	 *
	 * @param data Данные о группе.
	 */
	void favoriteGroupLongClick(Group data);
}
