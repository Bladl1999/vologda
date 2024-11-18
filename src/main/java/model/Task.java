package model;

import java.util.Objects;

public class Task {

    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskType taskType = TaskType.TASK;

    public Integer getId() {
        return id;
    }

    public void setId(Integer newId) {
        id = newId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus newStatus) {
        status = newStatus;
    }

    public Integer getEpicId() {
        return null;
    }

    public Task(Integer id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s", id, getTaskType(), title, status, description);
    }

}
