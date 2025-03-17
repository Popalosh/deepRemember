package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextStorageTest {

    @Mock
    private FileHandler fileHandler;

    private ContextStorage contextStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contextStorage = new ContextStorage(fileHandler);
    }

    @Test
    void testSaveContext() throws IOException {
        Context context = new Context("тема", "цель", List.of("шаг1"), "текущий шаг", "код", "заметки");
        contextStorage.saveContext("uid1", context);

        verify(fileHandler, times(1)).writeToFile(eq("context_storage.gz"), anyString(), eq(false));
    }

    @Test
    void testSaveContext_IOException() throws IOException {
        doThrow(new IOException("Ошибка записи")).when(fileHandler).writeToFile(anyString(), anyString(), anyBoolean());

        Context context = new Context("тема", "цель", List.of("шаг1"), "текущий шаг", "код", "заметки");

        assertThrows(IOException.class, () -> contextStorage.saveContext("uid1", context));
    }

    @Test
    void testLoadContext() throws IOException {
        when(fileHandler.fileExists("context_storage.gz")).thenReturn(true);
        when(fileHandler.readFromFile("context_storage.gz")).thenReturn(
                "{\"uid1\":{\"topic\":\"тема\",\"goal\":\"цель\",\"steps\":[\"шаг1\"],\"currentStep\":\"текущий шаг\",\"code\":\"код\",\"notes\":\"заметки\"}}"
        );

        Context context = contextStorage.loadContext("uid1");

        assertNotNull(context);
        assertEquals("тема", context.getTopic());
        assertEquals("цель", context.getGoal());
        assertEquals(List.of("шаг1"), context.getSteps());
        assertEquals("текущий шаг", context.getCurrentStep());
        assertEquals("код", context.getCode());
        assertEquals("заметки", context.getNotes());
    }

    @Test
    void testLoadContext_NotFound() throws IOException {
        when(fileHandler.fileExists("context_storage.gz")).thenReturn(true);
        when(fileHandler.readFromFile("context_storage.gz")).thenReturn("{}");

        Context context = contextStorage.loadContext("uid1");

        assertNull(context);
    }

    @Test
    void testLoadContext_IOException() throws IOException {
        when(fileHandler.fileExists("context_storage.gz")).thenReturn(true);
        when(fileHandler.readFromFile("context_storage.gz")).thenThrow(new IOException("Ошибка чтения"));

        assertThrows(IOException.class, () -> contextStorage.loadContext("uid1"));
    }

    @Test
    void testResetStorage() throws IOException {
        contextStorage.resetStorage();

        verify(fileHandler, times(1)).deleteFile("context_storage.gz");
        verify(fileHandler, times(1)).deleteFile("uids.txt");
    }

    @Test
    void testResetStorage_IOException() throws IOException {
        doThrow(new IOException("Ошибка удаления")).when(fileHandler).deleteFile(anyString());

        assertThrows(IOException.class, () -> contextStorage.resetStorage());
    }

    @Test
    void testGetAllUIDsWithComments() throws IOException {
        when(fileHandler.fileExists("uids.txt")).thenReturn(true);
        when(fileHandler.readFromFile("uids.txt")).thenReturn("uid1:Комментарий 1\nuid2:Комментарий 2");

        List<String[]> uidsWithComments = contextStorage.getAllUIDsWithComments();

        assertEquals(2, uidsWithComments.size());
        assertArrayEquals(new String[]{"uid1", "Комментарий 1"}, uidsWithComments.get(0));
        assertArrayEquals(new String[]{"uid2", "Комментарий 2"}, uidsWithComments.get(1));
    }

    @Test
    void testGetAllUIDsWithComments_FileNotExists() throws IOException {
        when(fileHandler.fileExists("uids.txt")).thenReturn(false);

        List<String[]> uidsWithComments = contextStorage.getAllUIDsWithComments();

        assertTrue(uidsWithComments.isEmpty());
    }

    @Test
    void testSaveUidWithComment() throws IOException {
        contextStorage.saveUidWithComment("uid1", "Комментарий 1");

        verify(fileHandler, times(1)).writeToFile(eq("uids.txt"), eq("uid1:Комментарий 1\n"), eq(true));
    }

    @Test
    void testSaveUidWithComment_IOException() throws IOException {
        doThrow(new IOException("Ошибка записи")).when(fileHandler).writeToFile(anyString(), anyString(), anyBoolean());

        assertThrows(IOException.class, () -> contextStorage.saveUidWithComment("uid1", "Комментарий 1"));
    }

    @Test
    void testSaveRestoredContextToJson() throws IOException {
        Context context = new Context("тема", "цель", List.of("шаг1"), "текущий шаг", "код", "заметки");
        contextStorage.saveRestoredContextToJson("uid1", context);

        verify(fileHandler, times(1)).writeToFile(eq("restored_context_uid1.json"), anyString(), eq(false));
    }

    @Test
    void testSaveRestoredContextToJson_IOException() throws IOException {
        doThrow(new IOException("Ошибка записи")).when(fileHandler).writeToFile(anyString(), anyString(), anyBoolean());

        Context context = new Context("тема", "цель", List.of("шаг1"), "текущий шаг", "код", "заметки");

        assertThrows(IOException.class, () -> contextStorage.saveRestoredContextToJson("uid1", context));
    }
}