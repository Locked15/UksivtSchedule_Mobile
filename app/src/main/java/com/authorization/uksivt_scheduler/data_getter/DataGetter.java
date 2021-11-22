package com.authorization.uksivt_scheduler.data_getter;

import com.authorization.uksivt_scheduler.change_elements.ChangeElement;
import com.authorization.uksivt_scheduler.change_elements.MonthChanges;
import com.authorization.uksivt_scheduler.schedule_elements.Days;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Класс, нужный для получения документа для парса с официального сайта.
 */
public class DataGetter
{
    //region Область: Константы.
    /**
     * Внутренняя константа, содержащая путь к HTML-тегу с заменами.
     * <br>
     * Путь начинается с элемента <i>"section id="inside-page"</i>".
     */
    private static final String CHANGES_PATH_IN_HTML;

    /**
     * Внутренняя константа, содержащая неразрывный пробел.
     */
    private static final String NON_BREAKING_SPACE;

    /**
     * Внутренняя константа, содержащая шаблон создания ссылок для скачивания документов с Google Drive.
     */
    private static final String GOOGLE_DRIVE_DOWNLOAD_LINK_TEMPLATE;
    //endregion

    //region Область: Поля класса.
    /**
     * Поле, содержащее полученную веб-страницу.
     */
    private Document webPage;
    //endregion

    //region Область: Конструкторы класса.
    /**
     * Конструктор класса.
     */
    public DataGetter()
    {
        try
        {
            webPage = Jsoup.connect("https://www.uksivt.ru/zameny").get();
        }

        catch (Exception e)
        {
            System.out.println("В процессе подключения произошла ошибка.");
        }
    }

    //Статический конструктор.
    static
    {
        //region Подобласть: Инициализация константы "CHANGES_PATH_IN_HTML".
        CHANGES_PATH_IN_HTML = "section > div > div > div > div > div > div" +
        " > div > div > div > div > div > div > div > div > div > div > div";
        //endregion

        //region Подобласть: Инициализация константы "NON_BREAKING_SPACE".
        NON_BREAKING_SPACE = "\u00A0";
        //endregion

        //region Подобласть: Инициализация константы "GOOGLE_DRIVE_DOWNLOAD_LINK_TEMPLATE".
        GOOGLE_DRIVE_DOWNLOAD_LINK_TEMPLATE = "https://drive.google.com/uc?export=download&id=";
        //endregion
    }
    //endregion

    //region Область: Методы класса.
    /**
     * Метод, нужный для выделения настоящей ссылки на скачивание файла с заменами.
     *
     * @param linkToFile Оригинальная ссылка на документ с заменами.
     *
     * @return Ссылка для скачивания документа с заменами.
     */
    public String getDownloadableLinkToChangesFile(String linkToFile)
    {
        linkToFile = linkToFile.substring(0, linkToFile.lastIndexOf('/'));
        linkToFile = linkToFile.substring(linkToFile.lastIndexOf('/') + 1);

        return GOOGLE_DRIVE_DOWNLOAD_LINK_TEMPLATE + linkToFile;
    }

    /**
     * Метод, нужный для скачивания файла с заменами в указанную директорию.
     *
     * @param url Ссылка на файл для скачивания.
     * @param path Путь, куда следует скачать файл.
     *
     * @return Логическое значение, отвечающее за успех скачивания.
     */
    public Boolean downloadFileWithChanges(String url, String path)
    {
        try
        {
            FileUtils.copyURLToFile(new URL(url), new File(path));

            return true;
        }

        catch (MalformedURLException urlException)
        {
            System.out.println("Полученный URL был некорректным:\n" + urlException.getMessage());
        }

        catch (IOException ioException)
        {
            System.out.println("В процессе скачивания была обнаружена ошибка:\n" + ioException.getMessage());
        }

        return false;
    }

    /**
     * Метод для получения списка возможных дней для просмотра замен.
     * <br>
     * В этом методе описываются теги, так что проверка орфографии отключена.
     *
     * @return Список с доступными заменами месяцам.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public ArrayList<MonthChanges> getAvailableNodes()
    {
        //region Подобласть: Переменные считывания доступных замен.
        Integer i = 0;
        Integer monthCounter = 0;
        String currentMonth = "Январь";
        ArrayList<ChangeElement> changes = new ArrayList<>(30);
        ArrayList<MonthChanges> monthChanges = new ArrayList<>(10);
        //endregion

        //region Подобласть: Переменные парса веб-страницы.
        Element generalChange = webPage.getElementById("inside-page").select(CHANGES_PATH_IN_HTML).get(0);
        Elements listOfChanges = generalChange.children();
        //endregion

        for (Element element : listOfChanges)
        {
            String text = element.text();

            //Месяцы всегда находятся в теге "p", но в самом начале страницы есть ...
            //... пробел, создаваемый этим же тегом. Он содержит в себе так называемый ...
            //... неразрывный пробел, так что это тоже необходимо учитывать.
            if (element.nodeName().equals("p") && !text.contentEquals(NON_BREAKING_SPACE))
            {
                //В первой итерации программа также зайдет сюда, так что это надо учесть.
                if (!changes.isEmpty())
                {
                    monthChanges.add(new MonthChanges(currentMonth, changes));
                }

                String temp = element.text();
                currentMonth = temp.substring(0, temp.lastIndexOf(' ')).replace(NON_BREAKING_SPACE, "");

                changes = new ArrayList<>(30);

                i = 1;
            }

            //Если мы встречаем тег "table", то мы дошли до таблицы с заменами на какой-либо месяц.
            else if (element.nodeName().equals("table") && monthCounter < 2)
            {
                //В таблице первым тегом всегда идет "<thead>", определяющий заголовок, ...
                //... а вторым — "<tbody>", определяющий тело таблицы. Он нам и нужен.
                Element tableBody = element.children().get(1);
                Elements tableRows = tableBody.children();

                for (int j = 0; j < tableRows.size(); j++)
                {
                    Integer dayCounter = 0;
                    Element currentRow = tableRows.get(j);

                    //В первой строке содержатся ненужные значения, пропускаем:
                    if (j == 0)
                    {
                        continue;
                    }

                    //В иных случаях начинаем итерировать ячейки таблицы:
                    for (Element tableCell : currentRow.children())
                    {
                        //Первая ячейка, видимо для отступа, содержит неразрывный пробел, так что ...
                        //... пропускаем такую итерацию. Кроме того, если 1 число месяца выпадает ...
                        //... не на понедельник, то некоторое количество ячеек также будет пустым.
                        if (tableCell.text().equals(NON_BREAKING_SPACE))
                        {
                            continue;
                        }

                        //В некоторых ячейках (дни без замен) нет содержимого, так что учитываем это.
                        if (tableCell.children().size() < 1)
                        {
                            changes.add(new ChangeElement(i, null, Days.getValueByIndex(dayCounter)));
                        }

                        //В ином случае замены есть и нам нужно получить дочерний элемент.
                        else
                        {
                            Element link = tableCell.children().first();

                            changes.add(new ChangeElement(i, link.attr("href"),
                            Days.getValueByIndex(dayCounter)));
                        }

                        i++;
                        dayCounter++;
                    }
                }

                //Нет смысла считывать значения более чем с двух месяцев.
                monthCounter++;
            }
        }

        return monthChanges;
    }
    //endregion
}
