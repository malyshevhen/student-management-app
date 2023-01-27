package ua.com.foxstudent102052.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsoleUtils {
    private static final Logger consoleLog = LoggerFactory.getLogger("CONSOLE");
    private static final Marker STDOUT = MarkerFactory.getMarker("STDOUT");

    public String getInputString(String messageToPrint, Reader in) {

        print(messageToPrint);

        String inputString;
        var reader = new BufferedReader(in);

        try {
            inputString = reader.readLine();

            if (inputString.isEmpty()) {
                print("You entered an empty string. Try again.");

                log.debug("User entered an empty string");

                return getInputString(messageToPrint, in);
            } else {
                log.debug("User entered: {}", inputString);

                return inputString;
            }
        } catch (IOException e) {
            var msg = "Error while reading input";

            print(msg);

            log.error(msg, e);

            return getInputString(messageToPrint, in);
        }
    }

    public Integer getInputInt(String messageToPrint, Reader in) {
        print(messageToPrint);

        int inputDigit;
        var reader = new BufferedReader(in);

        try {
            var readLine = reader.readLine();

            if (readLine.isEmpty()) {

                print("You entered an empty string. Try again.");

                log.debug("User entered an empty string");

                return getInputInt(messageToPrint, in);
            } else {
                inputDigit = Integer.parseInt(readLine);

                log.debug("User entered: {}", inputDigit);
            }
        } catch (IOException e) {
            var msg = "Error while reading input";

            print(msg);

            log.error(msg, e);

            return getInputInt(messageToPrint, in);
        } catch (NumberFormatException e) {

            print("You entered not number, try again:");

            log.debug("User entered not number");

            return getInputInt(messageToPrint, in);
        }

        return inputDigit;
    }

    public void print(String userMsg) {
        consoleLog.info(STDOUT, userMsg);
    }
}
