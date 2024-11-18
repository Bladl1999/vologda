package service;

import java.io.File;
import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(String saveFile) {
        return FileBackedTaskManager.loadFromFile(new File(Paths.get(saveFile).toUri()));
    }

}
