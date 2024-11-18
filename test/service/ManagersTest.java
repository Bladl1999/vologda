package service;

import model.Epic;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ManagersTest {

    @Test
        // Проверка того, что всегда возвращается рабочий экземпляр менеджера задач
    void getFileBackedTaskManager() {
        TaskManager taskManager = Managers.getFileBackedTaskManager("taskInfoTest.csv");
        taskManager.createEpic(new Epic(
                1, "Отдохнуть", "Провести свободное время по максимуму",
                TaskStatus.NEW, new ArrayList<Integer>())
        );
        Assertions.assertNotNull(taskManager.getAllEpics());
    }

    @Test
        // Проверка того, что всегда возвращается рабочий экземпляр менеджера задач
    void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.createEpic(new Epic(
                1, "Отдохнуть", "Провести свободное время по максимуму",
                TaskStatus.NEW, new ArrayList<Integer>())
        );
        Assertions.assertNotNull(taskManager.getAllEpics());
    }

    @Test
        // Проверка того, что всегда возвращается рабочий экземпляр менеджера истории просмотров
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Epic(
                1, "Отдохнуть", "Провести свободное время по максимуму",
                TaskStatus.NEW, new ArrayList<Integer>())
        );
        Assertions.assertNotNull(historyManager.getHistory().size());
    }

}