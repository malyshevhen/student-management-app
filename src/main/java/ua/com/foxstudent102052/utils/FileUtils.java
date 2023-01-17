package ua.com.foxstudent102052.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class FileUtils {
    public String readFileFromResourcesAsString(String filePath) {
        try (var inputStream = getFileFromResourceAsStream(filePath);
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            var stringBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line)
                        .append("\n");
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
    }

    public List<String[]> readCsvFileFromResources(String filePath) {
        try (var inputStream = getFileFromResourceAsStream(filePath);
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                var csvReader = new CSVReader(bufferedReader)) {
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
    }

    public InputStream getFileFromResourceAsStream(String filename) {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found:" + filename);
        } else {
            return inputStream;
        }
    }
}
