package service;

import model.Task;

import java.util.Objects;

public class Node {

    private Node nextNode;

    private Node prevNode;

    private Task task;

    public Node(Node nextNode, Node prevNode, Task task) {
        this.nextNode = nextNode;
        this.prevNode = prevNode;
        this.task = task;
    }

    public Node(Task task) {
        this.nextNode = null;
        this.prevNode = null;
        this.task = task;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Node getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(Node prevNode) {
        this.prevNode = prevNode;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(nextNode, node.nextNode) && Objects.equals(prevNode, node.prevNode) && Objects.equals(task, node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextNode, prevNode, task);
    }
}
