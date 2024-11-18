package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    //private static File tempFile;
//    @Override
//    protected FileBackedTaskManager createManager() {
//        FileBackedTaskManager manager = new FileBackedTaskManager(Paths.get("taskInfoTest.csv"));
//        return manager;
//
//    }

    @Test
    void testSaveAndLoadEmptyFile() {
        Assertions.assertEquals(0, taskManager.getSavePath().length());
        int allTasksBeforeSaving = taskManager.getAll().size();
        taskManager.save();
        taskManager.init();
        int allTasksAfterLoading = taskManager.getAll().size();
        Assertions.assertTrue(taskManager.getSavePath().exists());
        Assertions.assertEquals(0, taskManager.getSavePath().length());
        Assertions.assertEquals(allTasksBeforeSaving, allTasksAfterLoading);
    }

    @Test
    void saveSomeDifferentTasks() {
        Integer startedNumberLines = 0;

        try {
            Scanner scanner = new Scanner(taskManager.getSavePath());
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                startedNumberLines++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(0, startedNumberLines);

        taskManager.createTask(new Task(0, "Уборка", "Протереть пыль", TaskStatus.NEW));
        taskManager.createTask(new Task(0, "Отдых", "Посмотреть фильм", TaskStatus.NEW));
        taskManager.createEpic(new Epic(0, "Закончить 6 спринт", "Выполнить все задания курса", TaskStatus.DONE, new ArrayList<>()));
        taskManager.createSubtask(new Subtask(0, "Закончить теорию", "Пройти все уроки спринта", TaskStatus.DONE, 2));
        taskManager.createSubtask(new Subtask(0, "Закончить практику", "Сдать ТЗ 6", TaskStatus.NEW, 2));
        System.out.println("count tasks: " + taskManager.getAll().size());
        Integer finalNumberLines = 0;
        try {
            Scanner scanner = new Scanner(taskManager.getSavePath());
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                System.out.println(str);
                finalNumberLines++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("final " + finalNumberLines);
        //Assertions.assertEquals(5, finalNumberLines);
    }

    @Test
    void loadSomeDifferentTasks() {

    }

    @Override
    protected FileBackedTaskManager createManager() {
        FileBackedTaskManager manager = null;
        try {
            File tempFile = File.createTempFile("taskInfoTest", ".csv");
            manager = new FileBackedTaskManager(tempFile);
            manager.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return manager;
    }

//    public static File getTempFile() {
//        return tempFile;
//    }

}
