package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import model.TaskType;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File savePath;

    public FileBackedTaskManager(File savePath) {
        super();
        this.savePath = savePath;

    }

    public static FileBackedTaskManager loadFromFile(File path) {
        FileBackedTaskManager manager = new FileBackedTaskManager(path);
        manager.loadData();
        return manager;
    }

    private void loadData() {
        int maxId = 0;

        Map<Integer, Task> tasksMap = getTasksMap();
        Map<Integer, Epic> epicsMap = getEpicsMap();
        Map<Integer, Subtask> subtasksMap = getSubtasksMap();

        try (BufferedReader br = new BufferedReader(new FileReader(String.valueOf(savePath)))) {
            while (true) {
                String currentLine = br.readLine();
                if (currentLine == null)
                    break;

                Task currentTask = fromString(currentLine);
                switch (currentTask.getTaskType()) {
                    case TASK:
                        tasksMap.put(currentTask.getId(), currentTask);
                        break;
                    case EPIC:
                        epicsMap.put(currentTask.getId(), (Epic) currentTask);
                        break;
                    case SUBTASK:
                        subtasksMap.put(currentTask.getId(), (Subtask) currentTask);
                        break;
                }

                if (currentTask.getId() > maxId)
                    maxId = currentTask.getId();
            }

            for (Subtask subtask : subtasksMap.values()) {
                Epic currentEpic = epicsMap.get(subtask.getIdEpictask());
                currentEpic.getSubtasksId().add(subtask.getId());
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения файла: " + savePath.getAbsolutePath(), exception);
        }

        setTaskCounter(maxId);
    }

    public void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(String.valueOf(savePath.getAbsolutePath())))) {


            for (Task task : getAllTasks()) {
                fileWriter.write(toString(task) + "\n");
            }

            for (Epic epic : getAllEpics()) {
                fileWriter.write(toString(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(toString(subtask) + "\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при сохранении в файл: " + savePath.getAbsolutePath(), exception);
        }

    }

    private String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getTitle() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getEpicId();
    }

    private Task fromString(String value) {
        String[] taskInfo = value.split(",");

        Integer id = Integer.valueOf(taskInfo[0]);
        TaskType taskType = TaskType.valueOf(taskInfo[1]);
        String title = taskInfo[2];
        TaskStatus status = TaskStatus.valueOf(taskInfo[3]);
        String description = taskInfo[4];

        Task task = null;
        switch (taskType) {
            case TASK:
                task = new Task(id, title, description, status);
                break;
            case EPIC:
                task = new Epic(id, title, description, status);
                break;
            case SUBTASK:
                task = new Subtask(id, title, description, status, Integer.valueOf(taskInfo[5]));
                break;
        }

        return task;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    public void init() {
        loadData();
    }

    @Override
    public File getSavePath() {
        return savePath;
    }
}
