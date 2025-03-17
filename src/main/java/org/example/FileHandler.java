package org.example;

import java.io.IOException;

public interface FileHandler {
    void writeToFile(String fileName, String content, boolean append) throws IOException;
    String readFromFile(String fileName) throws IOException;
    boolean fileExists(String fileName);
    void deleteFile(String fileName) throws IOException;
}