package org.example;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class ContextManager {
    private final Gson gson;
    private final MessageDigest digest;

    // Конструктор (package-private)
    ContextManager(Gson gson, MessageDigest digest) {
        this.gson = gson;
        this.digest = digest;
    }

    // Статический фабричный метод
    public static ContextManager create() {
        try {
            Gson gson = new Gson();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new ContextManager(gson, digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // Генерация UID
    public String generateUID(Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        try {
            String timestamp = LocalDateTime.now().toString();
            String jsonContext = gson.toJson(context);
            String data = timestamp + jsonContext;

            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.substring(0, 8); // Первые 8 символов
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации UID", e);
        }
    }
}