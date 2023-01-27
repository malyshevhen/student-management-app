package ua.com.foxstudent102052.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtils {
    private static final String FILE_NOT_FOUND = "File not found: ";
    private static final String FILE_NOT_FOUND_LOG_MSG = "File not found: {}";

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
            log.error(FILE_NOT_FOUND_LOG_MSG, filePath, e);

            throw new IllegalArgumentException(FILE_NOT_FOUND + filePath);
        }
    }

    public List<String[]> readCsvFileFromResources(String filePath) {
        try (var inputStream = getFileFromResourceAsStream(filePath);
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                var csvReader = new CSVReader(bufferedReader)) {
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            log.error(FILE_NOT_FOUND_LOG_MSG, filePath, e);

            throw new IllegalArgumentException(FILE_NOT_FOUND + filePath);
        }
    }

    public InputStream getFileFromResourceAsStream(String filename) {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            log.error(FILE_NOT_FOUND_LOG_MSG, filename);

            throw new IllegalArgumentException(FILE_NOT_FOUND + filename);
        } else {
            return inputStream;
        }
    }
}
