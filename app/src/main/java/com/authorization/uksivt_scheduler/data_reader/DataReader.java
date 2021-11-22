package com.authorization.uksivt_scheduler.data_reader;

import com.authorization.uksivt_scheduler.schedule_elements.DaySchedule;
import com.authorization.uksivt_scheduler.schedule_elements.Days;
import com.authorization.uksivt_scheduler.schedule_elements.Lesson;
import com.authorization.uksivt_scheduler.schedule_elements.WeekSchedule;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * Класс, предоставляющий логику для получения данных из Excel-файла.
 */
public class DataReader
{
    //region Область: Поля работы с документами.
    /**
     * Внутреннее поле, содержащее объект, нужный для работы с docx-файлом.
     */
    private XWPFDocument document;
    //endregion

    //region Область: Константы.
    /**
     * Внутренняя константа, содержащая значение пустой ячейки таблицы Word.
     * <br>
     * В отличие от Excel, пустые ячейки в Word действительно пустые.
     */
    private static final String WORD_TABLE_EMPTY_CELL_VALUE;
    //endregion

    //region Область: Конструкторы класса.
    /**
     * Конструктор класса.
     *
     * @param pathToChangingFile Путь к файлу с заменами.
     */
    public DataReader(String pathToChangingFile)
    {
        try
        {
            FileInputStream wordDocument = new FileInputStream(pathToChangingFile);

            document = new XWPFDocument(wordDocument);
        }

        catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    //Статический конструктор класса.
    static
    {
        //region Инициализация константы "WORD_TABLE_EMPTY_CELL_VALUE":
        WORD_TABLE_EMPTY_CELL_VALUE = "";
        //endregion
    }
    //endregion

    //region Область: Обработка документа Word.
    /**
     * Метод для получения расписания на день с учетом замен.
     *
     * @return Расписание на день, учитывающее замены.
     *
     * @throws WrongDayInDocumentException При обработке документа обнаружилось несоответствие дней.
     */
    public DaySchedule getDayScheduleWithChanges(WeekSchedule schedule, Days day) throws WrongDayInDocumentException
    {
        //region Область: Переменные оригинальных значений.
        String groupName = schedule.getGroupName();
        DaySchedule originalSchedule = schedule.getDays().get(Days.getIndexByValue(day));
        //endregion

        //region Подобласть: Переменные для проверки групп "На Практику".
        String onPractiseString = "";
        StringBuilder technicalString = new StringBuilder();
        //endregion

        //region Подобласть: Переменные для составления измененного расписания.
        Integer cellNumber;
        String possibleNumbs;
        Lesson currentLesson;
        Boolean cycleStopper = false;
        Boolean changesListen = false;
        Boolean absoluteChanges = false;
        ArrayList<Lesson> newLessons = new ArrayList<>(1);
        //endregion

        //region Подобласть: Список с параграфами.
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        //endregion

        //Самым последним параграфом идет имя исполнителя, поэтому его игнорируем:
        for (int i = 0; i < paragraphs.size() - 1; i++)
        {
            String text;

            //Пятым параграфом идет название дня и недели. Проверяем корректность:
            if (i == 5)
            {
                text = paragraphs.get(i).getText().toLowerCase(Locale.ROOT);

                if (!text.contains(Days.toString(day).toLowerCase(Locale.ROOT)))
                {
                    throw new WrongDayInDocumentException("День отправленного расписания и документа с заменами не совпадают.");
                }
            }

            //Первыми идут параграфы с инициалами администрации, игнорируем:
            else if (i > 5)
            {
                text = paragraphs.get(i).getText();

                technicalString.append(text);
            }
        }

        //Порой бывает так, что замен нет, а вот перераспределение кабинетов - да, ...
        //... поэтому такой случай надо обработать:
        if (technicalString.toString().contains("– на практике"))
        {
            //Однако замены "на практику" всегда идут сверху, так что их индекс всегда 0.
            onPractiseString = Arrays.asList(technicalString.toString().split("– на практике")).get(0);
        }

        //Проверяем участие проверяемой группы на "практику":
        if (onPractiseString.toLowerCase(Locale.ROOT).contains(groupName.toLowerCase(Locale.ROOT)))
        {
            return DaySchedule.getOnPractiseSchedule(originalSchedule.day);
        }

        //Если группа НЕ на практике, то начинаем проверять таблицу с заменами:
        else
        {
            Iterator<XWPFTable> tables = document.getTablesIterator();

            while (tables.hasNext())
            {
                XWPFTable currentTable = tables.next();
                List<XWPFTableRow> rows = currentTable.getRows();

                //Порой в документе бывает несколько таблиц (пример: замены на 17.11.2020), ...
                //... и тогда таблица с заменами идет второй.
                if (!tableIsCorrect(currentTable))
                {
                    continue;
                }

                for (XWPFTableRow row : rows)
                {
                    cellNumber = 0;
                    possibleNumbs = "";
                    currentLesson = new Lesson();
                    List<XWPFTableCell> cells = row.getTableCells();

                    for (XWPFTableCell cell : cells)
                    {
                        String text = cell.getText();
                        String lowerText = text.toLowerCase(Locale.ROOT);

                        //Перед проведением всех остальных проверок, ...
                        //... необходимо выполнить проверку на пустое содержимое ячейки:
                        if (text.equals(WORD_TABLE_EMPTY_CELL_VALUE))
                        {
                            cellNumber++;

                            continue;
                        }

                        //Если мы встретили ячейку, содержащую название ...
                        //... нужной группы начинаем считывание замен:
                        if (lowerText.equals(groupName.toLowerCase(Locale.ROOT)))
                        {
                            changesListen = true;

                            //Если замены по центру, то они на весь день:
                            absoluteChanges = cellNumber != 0;
                        }

                        //Если мы встречаем название другой группы во время чтения замен, ...
                        //... то прерываем цикл:
                        else if (changesListen && !lowerText.equals(groupName.toLowerCase(Locale.ROOT)) &&
                        (cellNumber == 0 || cellNumber == 3))
                        {
                            cycleStopper = true;

                            break;
                        }

                        //В ином случае мы продолжаем считывать замены, ...
                        //... ориентируясь на текущий номер ячейки:
                        else if (changesListen)
                        {
                            switch (cellNumber)
                            {
                                //Во второй ячейке находится номер пары:
                                case 1:
                                {
                                    possibleNumbs = text;

                                    break;
                                }

                                //В пятой ячейке название новой пары:
                                case 4:
                                {
                                    currentLesson.setName(text);

                                    break;
                                }

                                //В шестой - имя преподавателя:
                                case 5:
                                {
                                    currentLesson.setTeacher(text);

                                    break;
                                }

                                //В седьмой - место проведения пары:
                                case 6:
                                {
                                    currentLesson.setPlace(text);

                                    break;
                                }
                            }
                        }

                        cellNumber++;
                    }

                    if (changesListen && !possibleNumbs.equals(""))
                    {
                        newLessons.addAll(expandPossibleLessons(possibleNumbs, currentLesson));
                    }

                    //После прерывания первого цикла, прерываем и второй:
                    if (cycleStopper)
                    {
                        break;
                    }
                }
            }
        }

        //После обработки документа необходимо проверить полученные результаты, ...
        //... ведь у группы, возможно, вообще нет замен:
        if (newLessons.isEmpty())
        {
            return originalSchedule;
        }

        return originalSchedule.mergeChanges(newLessons, absoluteChanges);
    }

    /**
     * Метод, позволяющий раскрыть сокращенную запись номеров пар в полный вид.
     *
     * @param value Сокращенный (возможно) вид записи номеров пар.
     * @param lesson Пара, которая должна быть проведена.
     *
     * @return Полный вид пар.
     */
    private ArrayList<Lesson> expandPossibleLessons(String value, Lesson lesson)
    {
        String[] splatted = value.split(",");
        ArrayList<Lesson> toReturn = new ArrayList<>(1);

        //Чтобы преобразовать строку в число необходимо избавиться от пробелов:
        for (int i = 0; i < splatted.length; i++)
        {
            splatted[i] = splatted[i].replace(" ", "");
        }

        for (String s : splatted)
        {
            toReturn.add(new Lesson(Integer.parseInt(s), lesson.getName(), lesson.getTeacher(),
            lesson.getPlace()));
        }

        return toReturn;
    }
    //endregion

    //region Область: Внутренние методы.
    /**
     * Метод, нужный для проверки таблицы на то, является ли таблица таблицей с заменами.
     *
     * @return Логическое значение, отвечающее за корректность таблицы.
     */
    private Boolean tableIsCorrect(XWPFTable table)
    {
        String temp = table.getText().toLowerCase(Locale.ROOT);

        //В таблице, содержащей сами замены всегда есть подобные значения в её оглавлении, ...
        //... но на всякий случай здесь есть возможность замены некоторых оглавлений:
        return temp.contains("группа") && (temp.contains("заменяемая дисциплина") ||
        temp.contains("заменяемый преподаватель")) && (temp.contains("заменяющая дисциплина") ||
        temp.contains("заменяющий преподаватель")) && temp.contains("ауд");
    }
    //endregion
}
