package com.authorization.uksivt_scheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.data_getter.StandardScheduler;

import java.util.List;


/**
 * Класс, содержащий логику для "activity_sub_folders".
 */
public class SubFoldersActivity extends AppCompatActivity
{
    //region Область: Поля.
    /**
     * Поле, содержащее выбранное направление.
     */
    private String course;
    //endregion
    
    //region Область: События.
    /**
     * Событие, происходящее при создании окна.
     *
     * @param savedInstanceState Последнее сохраненное состояние приложения.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_folders);
        
        course = getIntent().getStringExtra("course_name");
        
        insertButtonsToLayout(new StandardScheduler(this).getGroupsInSubFolder(course));
    }
    //endregion
    
    //region Область: Методы.
    /**
     * Метод для добавления кнопок-подпапок в список.
     */
    private void insertButtonsToLayout(List<String> subFolders)
    {
        LinearLayout layout = findViewById(R.id.sub_folders_list);
    
        for (int i = 0; i < subFolders.size(); i++)
        {
            Button button = new Button(this);
            button.setText(subFolders.get(i));
            button.setTextColor(getColor(R.color.white));
            button.setBackgroundColor(getColor(R.color.main_gray));
            button.setOnClickListener(new View.OnClickListener()
            {
                /**
                 * Событие, происходящее при нажатии на одну из кнопок с группой.
                 *
                 * @param view Элемент, вызвавщий событие.
                 */
                public void onClick(View view)
                {
                    Intent newWindow = new Intent(view.getContext(), GroupsActivity.class);
                    newWindow.putExtra("course_name", course);
                    newWindow.putExtra("subFolder_name", ((Button)view).getText());
                
                    startActivity(newWindow);
                }
            });
        
            //Почему-то, если задавать размер любым другим способом, ...
            //... текст просто исчезает, так что выбора нет:
            button.setTextSize(48);
        
            layout.addView(button);
        
            //Чтобы не добавить "пробел" в конец списка делаем проверку:
            if (i + 1 < subFolders.size())
            {
                Space space = new Space(this);
                space.setMinimumHeight(50);
            
                layout.addView(space);
            }
        }
    }
    //endregion
}
