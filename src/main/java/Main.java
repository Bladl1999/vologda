import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getFileBackedTaskManager("taskInfo.csv");
        taskManager.createTask(new Task(0, "Уборка", "Протереть пыль", TaskStatus.NEW));
        taskManager.createTask(new Task(0, "Отдых", "Посмотреть фильм", TaskStatus.NEW));
        taskManager.createEpic(new Epic(0, "Закончить 6 спринт", "Выполнить все задания курса", TaskStatus.DONE, new ArrayList<>()));
        taskManager.createSubtask(new Subtask(0, "Закончить теорию", "Пройти все уроки спринта", TaskStatus.DONE, 2));
        taskManager.createSubtask(new Subtask(0, "Закончить практику", "Сдать ТЗ 6", TaskStatus.NEW, 2));
        taskManager.createSubtask(new Subtask(0, "Поработать над тестами", "Обновить старые и добавить новые тесты", TaskStatus.DONE, 2));
        taskManager.createEpic(new Epic(0, "Погулять", "Встретиться с друзьями в парке", TaskStatus.IN_PROGRESS, new ArrayList<>()));
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(1);
        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(6);
        taskManager.getSubtaskById(6);
        System.out.println(taskManager.getHistory());

        TaskManager taskManager2 = Managers.getFileBackedTaskManager("taskInfo.csv");
        System.out.println("Наполнение менеджеров равно: " + (taskManager.getAll().size() == taskManager2.getAll().size()));
    }
}
