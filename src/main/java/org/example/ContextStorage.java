package org.example;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ContextStorage {

    private static final String STORAGE_FILE = "context_storage.gz";
    private static final String UID_FILE = "uids.txt";
    private static final Gson gson = new Gson();

    // Сохранение контекста в сжатый файл
    public static void saveContext(String uid, Context context) {
        Map<String, Context> data = loadAllData(); // Загружаем все данные
        data.put(uid, context); // Добавляем новый контекст

        try (GZIPOutputStream gzipOut = new GZIPOutputStream(new FileOutputStream(STORAGE_FILE));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(gzipOut))) {
            writer.write(gson.toJson(data)); // Сохраняем все данные в сжатый файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Восстановление контекста по UID
    public static Context loadContext(String uid) {
        Map<String, Context> data = loadAllData();
        return data.get(uid); // Возвращаем контекст по UID
    }

    // Получение всех UID
    public static List<String> getAllUIDs() {
        return new ArrayList<>(loadAllData().keySet());
    }

    // Загрузка всех данных из сжатого файла
    private static Map<String, Context> loadAllData() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            return new HashMap<>(); // Если файла нет, возвращаем пустую карту
        }

        try (GZIPInputStream gzipIn = new GZIPInputStream(new FileInputStream(STORAGE_FILE));
             BufferedReader reader = new BufferedReader(new InputStreamReader(gzipIn))) {
            String json = reader.readLine();
            return gson.fromJson(json, new com.google.gson.reflect.TypeToken<Map<String, Context>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Сохранение UID с комментарием
    public static void saveUidWithComment(String uid, String comment) {
        try (FileWriter writer = new FileWriter(UID_FILE, true)) {
            writer.write(uid + ":" + comment + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Сохранение восстановленного контекста в JSON
    public static void saveRestoredContextToJson(String uid, Context context) {
        String fileName = "restored_context_" + uid + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(context, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}