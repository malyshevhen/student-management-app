package ua.com.foxstudent102052.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStreamReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.table.TableFactory;
import ua.com.foxstudent102052.utils.ConsoleUtils;

class ConsoleUITest {
    private static GroupController groupController;
    private static CourseController courseController;
    private static StudentController studentController;
    private static ConsoleUtils consoleUtils;
    public static ConsoleUI consoleUI;

    @BeforeEach
    void setUp() {
        groupController = mock(GroupController.class);
        courseController = mock(CourseController.class);
        studentController = mock(StudentController.class);
        consoleUtils = mock(ConsoleUtils.class);
        var tableFactory = mock(TableFactory.class);
        consoleUI = new ConsoleUI(groupController, courseController, studentController, consoleUtils, tableFactory);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 6})
    void Method_callMainMenu_shouldCallCourseControllerMethod_getAllCourses(int number) {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(number).thenReturn(0);

        consoleUI.callMainMenu();

        verify(courseController).getAllCourses();
    }

    @Test
    void Method_callMainMenu_shouldCallGroupControllerMethod_GetAllGroups() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(1).thenReturn(0);

        consoleUI.callMainMenu();

        verify(groupController).getAllGroups();
    }

    @Test
    void Method_callMainMenu_shouldCallStudentControllerMethod_addStudent() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(1).thenReturn(0);

        consoleUI.callMainMenu();

        verify(studentController).addStudent(any(StudentDto.class));
    }

    @Test
    void Method_callMainMenu_shouldCallStudentControllerMethod_removeStudent() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(2).thenReturn(0);

        consoleUI.callMainMenu();

        verify(studentController).removeStudent(anyInt());
    }

    @Test
    void Method_callMainMenu_shouldCallStudentControllerMethod_addStudentToCourse() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(3).thenReturn(0);

        consoleUI.callMainMenu();

        verify(studentController).addStudentToCourse(anyInt(), anyInt());
    }

    @Test
    void Method_callMainMenu_shouldCallStudentControllerMethod_removeStudentFromCourse() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(4).thenReturn(0);

        consoleUI.callMainMenu();

        verify(studentController).removeStudentFromCourse(anyInt(), anyInt());
    }

    @Test
    void Method_callMainMenu_shouldCallGroupControllerMethod_getGroupsSmallerThen() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(5).thenReturn(0);

        consoleUI.callMainMenu();

        verify(groupController).getGroupsSmallerThen(anyInt());
    }

    @Test
    void Method_callMainMenu_shouldCallStudentControllerMethod_getStudentsByNameAndCourseId() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(6).thenReturn(1).thenReturn(0);
        when(consoleUtils.getInputString(anyString(), any(InputStreamReader.class))).thenReturn("Name");

        consoleUI.callMainMenu();

        verify(studentController).getStudents("Name", 1);
    }

    @Test
    void Method_callMainMenu_shouldCallStudentControllerMethod_getAllStudents() {
        when(consoleUtils.getInputInt(anyString(), any(InputStreamReader.class))).thenReturn(7).thenReturn(0);

        consoleUI.callMainMenu();

        verify(studentController).getAllStudents();
    }
}
