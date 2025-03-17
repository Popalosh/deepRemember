package org.example;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {
    private final FileHandler fileHandler = new FileHandlerImpl();

    @Test
    void testWriteAndReadFile() throws IOException {
        String fileName = "test_file.txt";
        String content = "test content";

        fileHandler.writeToFile(fileName, content, false);
        String readContent = fileHandler.readFromFile(fileName);

        assertEquals(content, readContent);
        fileHandler.deleteFile(fileName);
    }

    @Test
    void testFileExists() throws IOException {
        String fileName = "test_file.txt";
        fileHandler.writeToFile(fileName, "test", false);

        assertTrue(fileHandler.fileExists(fileName));
        fileHandler.deleteFile(fileName);
        assertFalse(fileHandler.fileExists(fileName));
    }
}