package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextManagerTest {

    @Mock
    private MessageDigest digest;

    private Gson gson;
    private ContextManager contextManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gson = new Gson();
        contextManager = new ContextManager(gson, digest);
    }

    @Test
    void testGenerateUID() throws Exception {
        Context context = new Context("тема", "цель", List.of("шаг1"), "текущий шаг", "код", "заметки");
        String timestamp = "2023-10-01T12:00:00";
        String jsonContext = gson.toJson(context);
        String data = timestamp + jsonContext;

        when(digest.digest(data.getBytes(StandardCharsets.UTF_8))).thenReturn(new byte[]{0x12, 0x34, 0x56, 0x78});

        String uid = contextManager.generateUID(context);

        assertEquals("12345678", uid);
        verify(digest, times(1)).digest(data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testGenerateUID_Exception() throws Exception {
        Context context = new Context("тема", "цель", List.of("шаг1"), "текущий шаг", "код", "заметки");

        when(digest.digest(any(byte[].class))).thenThrow(new RuntimeException("Ошибка хеширования"));

        Exception exception = assertThrows(RuntimeException.class, () -> contextManager.generateUID(context));
        assertEquals("Ошибка при генерации UID", exception.getMessage());
    }
}