package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtasksId;
    private final TaskType taskType = TaskType.EPIC;

    public Epic(Integer id, String title, String description, TaskStatus status, List<Integer> subtasksId) {
        super(id, title, description, status);
        this.subtasksId = subtasksId;
    }

    public Epic(Integer id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        subtasksId = new ArrayList<>();
    }


    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        for (Integer subtaskId : subtasksId)
            sb.append(",").append(subtaskId);
        return sb.toString();
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }
}
