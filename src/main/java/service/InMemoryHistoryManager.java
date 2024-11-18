package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> history = new HashMap<>();

    private Node head;

    private Node tail;

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }

        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            Node currentNode = history.get(id);
            removeNode(currentNode);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node currentNode) {
        if (currentNode.equals(head) && currentNode.equals(tail)) {
            head = null;
            tail = null;
            return;
        }
        if (currentNode.equals(head)) {
            Node nextNode = currentNode.getNextNode();
            nextNode.setPrevNode(null);
            head = nextNode;
            return;
        }
        if (currentNode.equals(tail)) {
            Node prevNode = currentNode.getPrevNode();
            prevNode.setNextNode(null);
            tail = prevNode;
            return;
        }
        Node prevNode = currentNode.getPrevNode();
        prevNode.setNextNode(currentNode.getNextNode());
        Node nextNode = currentNode.getNextNode();
        nextNode.setPrevNode(prevNode);
    }

    public void linkLast(Task task) {
        Node tmpNode = new Node(task);

        history.put(task.getId(), tmpNode);
        if (head == null) {
            head = tmpNode;
            tail = tmpNode;
            return;
        }

        tmpNode.setPrevNode(tail);
        tail.setNextNode(tmpNode);
        tail = tmpNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>(history.size());
        Node currentNode = head;

        while (currentNode != null) {
            list.add(currentNode.getTask());
            currentNode = currentNode.getNextNode();
        }

        return list;
    }

}
