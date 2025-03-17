package org.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;

public class ContextStorage {
    private static final String STORAGE_FILE = "context_storage.gz";
    private static final String UID_FILE = "uids.txt";
    private final Gson gson = new Gson();
    private final FileHandler fileHandler;

    public ContextStorage(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // Сохранение контекста
    public void saveContext(String uid, Context context) throws IOException {
        Map<String, Context> data = loadAllData();
        data.put(uid, context);
        fileHandler.writeToFile(STORAGE_FILE, gson.toJson(data), false);
    }

    // Восстановление контекста
    public Context loadContext(String uid) throws IOException {
        Map<String, Context> data = loadAllData();
        return data.get(uid);
    }

    // Получение всех UID с комментариями
    public List<String[]> getAllUIDsWithComments() throws IOException {
        List<String[]> uidsWithComments = new ArrayList<>();
        if (fileHandler.fileExists(UID_FILE)) {
            String content = fileHandler.readFromFile(UID_FILE);
            for (String line : content.split("\n")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    uidsWithComments.add(new String[]{parts[0], parts[1]});
                }
            }
        }
        return uidsWithComments;
    }

    // Сохранение UID с комментарием
    public void saveUidWithComment(String uid, String comment) throws IOException {
        fileHandler.writeToFile(UID_FILE, uid + ":" + comment + "\n", true);
    }

    // Сброс всех данных
    public void resetStorage() throws IOException {
        fileHandler.deleteFile(STORAGE_FILE);
        fileHandler.deleteFile(UID_FILE);
    }

    // Сохранение восстановленного контекста в JSON
    public void saveRestoredContextToJson(String uid, Context context) throws IOException {
        String fileName = "restored_context_" + uid + ".json";
        fileHandler.writeToFile(fileName, gson.toJson(context), false);
    }

    // Загрузка всех данных
    private Map<String, Context> loadAllData() throws IOException {
        if (!fileHandler.fileExists(STORAGE_FILE)) {
            return new HashMap<>();
        }
        String json = fileHandler.readFromFile(STORAGE_FILE);
        return gson.fromJson(json, new com.google.gson.reflect.TypeToken<Map<String, Context>>() {}.getType());
    }
}