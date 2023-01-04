package ua.com.foxstudent102052.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ConsoleUtils {
    private ConsoleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getInputString(String messageToPrint) {
        print(messageToPrint);

        String inputString;
        var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            inputString = reader.readLine();

            if (inputString.isEmpty()) {
                print("You entered an empty string. Try again.");

                log.info("User entered an empty string");

                return getInputString(messageToPrint);
            } else {
                log.info("User entered: {}", inputString);

                return inputString;
            }
        } catch (IOException e) {
            var msg = "Error while reading input";

            print(msg);

            log.error(msg, e);

            return getInputString(messageToPrint);
        }
    }

    public static Integer getInputInt(String messageToPrint) {
        print(messageToPrint);

        int inputDigit;
        var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            var readLine = reader.readLine();

            if (readLine.isEmpty()) {

                print("You entered an empty string. Try again.");

                log.info("User entered an empty string");

                return getInputInt(messageToPrint);
            } else {
                inputDigit = Integer.parseInt(readLine);

                log.info("User entered: {}", inputDigit);
            }
        } catch (IOException e) {
            var msg = "Error while reading input";

            print(msg);

            log.error(msg, e);

            return getInputInt(messageToPrint);
        } catch (NumberFormatException e) {

            print("You entered not number, try again:");

            log.info("User entered not number");

            return getInputInt(messageToPrint);
        }

        return inputDigit;
    }

    public static void print(String userMsg) {
        System.out.println(userMsg);
    }
}
