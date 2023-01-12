package ua.com.foxstudent102052.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@Component
@Slf4j
public class ConsoleUtils {
    public String getInputString(String messageToPrint, Reader in) {
        print(messageToPrint);

        String inputString;
        var reader = new BufferedReader(in);

        try {
            inputString = reader.readLine();

            if (inputString.isEmpty()) {
                print("You entered an empty string. Try again.");

                log.info("User entered an empty string");

                return getInputString(messageToPrint, in);
            } else {
                log.info("User entered: {}", inputString);

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

                log.info("User entered an empty string");

                return getInputInt(messageToPrint, in);
            } else {
                inputDigit = Integer.parseInt(readLine);

                log.info("User entered: {}", inputDigit);
            }
        } catch (IOException e) {
            var msg = "Error while reading input";

            print(msg);

            log.error(msg, e);

            return getInputInt(messageToPrint, in);
        } catch (NumberFormatException e) {

            print("You entered not number, try again:");

            log.info("User entered not number");

            return getInputInt(messageToPrint, in);
        }

        return inputDigit;
    }

    public void print(String userMsg) {
        System.out.println(userMsg);
    }
}
