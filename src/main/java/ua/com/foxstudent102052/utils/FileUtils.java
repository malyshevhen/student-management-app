package ua.com.foxstudent102052.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
}
