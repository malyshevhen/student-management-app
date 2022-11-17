package ua.com.foxstudent102052.facade;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class InputUtils {

    private InputUtils() {
        throw new IllegalStateException("Utility class");
    }

    static String takeInputStringFromUser(String messageToPrint) {
        print(messageToPrint);
        String inputString;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            inputString = reader.readLine();

            if (inputString.isEmpty()) {
                String userMsg = "You entered an empty string. Try again.";
                print(userMsg);
                String logMsg = "User entered an empty string";
                log.info(logMsg);

                return takeInputStringFromUser(messageToPrint);
            } else {
                log.info("User entered: {}", inputString);

                return inputString;
            }
        } catch (IOException e) {
            String msg = "Error while reading input";
            print(msg);
            log.error(msg, e);

            return takeInputStringFromUser(messageToPrint);
        }
    }

    static Integer takeInputIntFromUser(String messageToPrint) {
        print(messageToPrint);
        int inputDigit;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String readLine = reader.readLine();

            if (readLine.isEmpty()) {
                String userMsg = "You entered an empty string. Try again.";
                print(userMsg);
                String logMsg = "User entered an empty string";
                log.info(logMsg);

                return takeInputIntFromUser(messageToPrint);
            } else {
                inputDigit = Integer.parseInt(readLine);
                log.info("User entered: {}", inputDigit);

            }
        } catch (IOException e) {
            String msg = "Error while reading input";
            print(msg);
            log.error(msg, e);

            return takeInputIntFromUser(messageToPrint);
        } catch (NumberFormatException e) {
            String userMsg = "You entered not number, try again:";
            print(userMsg);
            String logMsg = "User entered not number";
            log.info(logMsg);

            return takeInputIntFromUser(messageToPrint);
        }

        return inputDigit;
    }

    private static void print(String userMsg) {
        System.out.println(userMsg);
    }
}
