package ua.com.foxstudent102052.utils;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleUtilsTest {
    private ConsoleUtils consoleUtils = new ConsoleUtils();

    @Test
    void getInputString() {
        var expected = "Some string";
        var in = new BufferedReader(new StringReader(expected));
        var actual = consoleUtils.getInputString("Some message", in);

        assertEquals(expected, actual);
    }

    @Test
    void getInputInt() {
        int expected = 1;
        var in = new BufferedReader(new StringReader(String.valueOf(expected)));
        int actual = consoleUtils.getInputInt("Some message",in);

        assertEquals(expected, actual);
    }
}
