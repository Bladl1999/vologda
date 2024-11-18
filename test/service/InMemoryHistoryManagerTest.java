package service;

import model.Epic;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    private static HistoryManager historyManager;

    @BeforeEach
    void beforeAll() {
        historyManager = new InMemoryHistoryManager();
    }


    @Test
        // Проверка работоспособности добавления задачи в список просмотренных
    void addFirstTaskInHistory() {
        historyManager.add(new Task(10, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW));
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
        // Проверка того, что ограничения на уникальные задачи больше нет и при добавлении 11 задачи, она будет добавлена
    void shouldSavedMoreThenTenTasks() {
        historyManager.add(new Task(1, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW));
        historyManager.add(new Task(2, "Сделать уборку", "Помыть пол", TaskStatus.DONE));
        historyManager.add(new Epic(3, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW, new ArrayList<Integer>()));
        historyManager.add(new Epic(4, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW, new ArrayList<Integer>()));
        historyManager.add(new Task(5, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW));
        historyManager.add(new Task(6, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW));
        historyManager.add(new Task(7, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW));
        historyManager.add(new Task(8, "Зайти в магазин", "Купить молоко и хлеб", TaskStatus.NEW));
        historyManager.add(new Task(9, "Сделать уборку", "Помыть пол", TaskStatus.DONE));
        historyManager.add(new Task(10, "Сделать уборку", "Помыть пол", TaskStatus.DONE));
        historyManager.add(new Task(11, "Почитать", "Прочитать 10 страниц", TaskStatus.IN_PROGRESS));
        Assertions.assertEquals(11, historyManager.getHistory().size());
    }

}