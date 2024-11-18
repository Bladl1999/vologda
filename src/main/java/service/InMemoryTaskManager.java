package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int taskCounter = 0;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            allTasks.add(task);
        }

        return allTasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> allEpics = new ArrayList<>();

        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }

        return allEpics;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        List<Subtask> allSubtasks = new ArrayList<>();

        for (Subtask subtask : subtasks.values()) {
            allSubtasks.add(subtask);
        }

        return allSubtasks;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet())
            historyManager.remove(id);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            for (Integer subtaskId : getEpicById(id).getSubtasksId())
                historyManager.remove(subtaskId);
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer id : subtasks.keySet())
            historyManager.remove(id);
        subtasks.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task != null)
            historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null)
            historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null)
            historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        task.setId(taskCounter);
        tasks.put(task.getId(), task);
        taskCounter++;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(taskCounter);
        List<Integer> subtaskList = epic.getSubtasksId();

        if (subtaskList.contains(taskCounter))
            return;

        epics.put(epic.getId(), epic);
        checkEpicStatus(epic.getId());
        taskCounter++;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(taskCounter);

        if (subtask.getId() == subtask.getIdEpictask())
            return;

        List<Integer> subtasksList = findEpicById(subtask.getIdEpictask()).getSubtasksId();
        subtasks.put(subtask.getId(), subtask);
        subtasksList.add(subtask.getId());
        checkEpicStatus(subtask.getIdEpictask());
        taskCounter++;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()))
            tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            if (epic.getSubtasksId().contains(epic.getId()))
                return;
            epics.put(epic.getId(), epic);
            checkEpicStatus(epic.getId());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (subtask.getId() == subtask.getIdEpictask())
                return;
            subtasks.put(subtask.getId(), subtask);
            checkEpicStatus(subtask.getIdEpictask());
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            List<Integer> subtasksList = epics.get(id).getSubtasksId();

            for (Integer subtaskId : subtasksList) {
                if (subtasks.containsKey(subtaskId)) {
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                }
            }

            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (subtasks.containsKey(id)) {
            getEpicById(getSubtaskById(id).getIdEpictask()).getSubtasksId().remove(id); //Удаление id subtask из model.Epic
            historyManager.remove(id);
            subtasks.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtasksOfEpicById(Integer id) {
        List<Subtask> resultList = new ArrayList<>();

        for (Integer subtaskId : getEpicById(id).getSubtasksId()) {
            resultList.add(subtasks.get(subtaskId));
        }

        return resultList;
    }

    private void checkEpicStatus(Integer epicId) {
        Epic epic = findEpicById(epicId);
        List<Integer> subtasksList = epic.getSubtasksId();

        if (subtasksList.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            int newCount = 0;
            int doneCount = 0;

            for (Integer subtaskId : subtasksList) {
                if (findSubtaskById(subtaskId).getStatus() == TaskStatus.NEW)
                    newCount++;
                if (findSubtaskById(subtaskId).getStatus() == TaskStatus.DONE)
                    doneCount++;
            }

            if (newCount == subtasksList.size() || doneCount == subtasksList.size()) {
                if (newCount == subtasksList.size()) epic.setStatus(TaskStatus.NEW);
                else epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public List<Task> getAll() {
        List<Task> result = new ArrayList<>();

        for (Task task : getAllTasks())
            result.add(task);

        for (Epic epic : getAllEpics())
            result.add(epic);

        for (Subtask subtask : getAllSubtasks())
            result.add(subtask);

        return result;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Epic findEpicById(Integer id) {
        return epics.get(id);
    }

    private Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public Integer getTaskCounter() {
        return taskCounter;
    }

    protected void setTaskCounter(Integer taskCounter) {
        this.taskCounter = taskCounter;
    }

    protected Map<Integer, Task> getTasksMap() {
        return tasks;
    }

    protected Map<Integer, Epic> getEpicsMap() {
        return epics;
    }

    protected Map<Integer, Subtask> getSubtasksMap() {
        return subtasks;
    }

    @Override
    public File getSavePath() {
        return null;
    }

}

