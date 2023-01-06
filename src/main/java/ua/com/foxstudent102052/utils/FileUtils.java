package ua.com.foxstudent102052.utils;

import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;

import java.io.*;

public class FileUtils {
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String readTextFile(String filePath) throws IOException {
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

    public static InputStream getFileFromResourceAsStream(String filename) {
        ClassLoader classLoader = PooledDataSource.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found:" + filename);
        } else {
            return inputStream;
        }
    }
}
