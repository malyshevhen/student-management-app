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
        System.out.print(messageToPrint);
        String inputString;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            inputString = reader.readLine();
    
            if (inputString.isEmpty()) {
                System.out.println("You entered an empty string. Try again.");
                log.info("User entered an empty string");
                
                return takeInputStringFromUser(messageToPrint);
            } else {
                log.info("User entered: {}", inputString);
                
                return inputString;
            }
        } catch (IOException e) {
            System.out.println("Error while reading input");
            log.error("Error while reading input", e);
    
            return takeInputStringFromUser(messageToPrint);
        }
    }

    static Integer takeInputIntFromUser(String messageToPrint) {
        System.out.println(messageToPrint);
        int inputDigit;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
        try {
            String readLine = reader.readLine();
    
            if (readLine.isEmpty()) {
                System.out.println("You entered an empty string. Try again.");
                log.info("User entered an empty string");
    
                return takeInputIntFromUser(messageToPrint);
            } else {
                
                try {
                    inputDigit = Integer.parseInt(readLine);
                    log.info("User entered: {}", inputDigit);
                } catch (NumberFormatException e) {
                    System.out.println("You entered not number, try again:");
                    log.info("User entered not number");
    
                    return takeInputIntFromUser(messageToPrint);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading input");
            log.error("Error while reading input", e);
    
            return takeInputIntFromUser(messageToPrint);
        }
    
        return inputDigit;
    }
}
