package ua.com.foxstudent102052.utils;

import java.io.*;

public class FileUtils {
    public String readTextFile(String filePath) throws IOException {
        var file = new File(filePath);
        var fileReader = new FileReader(file);

        try (var bufferedReader = new BufferedReader(fileReader)) {
            var stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line)
                    .append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public InputStream getFileFromResourceAsStream(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found:" + filename);
        } else {
            return inputStream;
        }
    }
}
