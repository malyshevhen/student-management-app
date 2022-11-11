package ua.com.foxstudent102052.facade;

import ua.com.foxstudent102052.controller.GroupController;

import static ua.com.foxstudent102052.facade.InputUtils.takeInputIntFromUser;
import static ua.com.foxstudent102052.facade.InputUtils.takeInputStringFromUser;
import static ua.com.foxstudent102052.facade.TableBuilder.createGroupStudentsTable;
import static ua.com.foxstudent102052.facade.TableBuilder.createGroupTable;

public class GroupFacade {
    private static final String GROUP_MENU =
        """
            -----------------------------------------
            *********** GROUPS MANAGEMENT ***********
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Add group;
            2. Remove group;
            3. Update group;
            4. Find all groups with less or equal number of students;
            5. Show all groups;
            0. Back to main menu.
            -----------------------------------------
            Enter option number:""";

    static final String ENTER_GROUP_ID = "Enter group id: ";
    static final String ENTER_GROUP_NAME = "Enter group name: ";
    static final String WRONG_INPUT = "Wrong input.";

    private static final GroupController groupController = new GroupController();

    private GroupFacade() {
        throw new IllegalStateException("Utility class");
    }

    static int option = Integer.MAX_VALUE;

    static void callUpGroupManagementMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(GROUP_MENU);

            if (option == 1) {
                String groupName = takeInputStringFromUser(ENTER_GROUP_NAME);
                groupController.addGroup(groupName);

            } else if (option == 2) {
                int groupId = takeInputIntFromUser(ENTER_GROUP_ID);
                groupController.removeGroup(groupId);

            } else if (option == 3) {
                int groupId = takeInputIntFromUser(ENTER_GROUP_ID);
                System.out.println(createGroupTable(groupController
                    .getGroupById(groupId)));

            } else if (option == 4) {
                int numberOfStudents = takeInputIntFromUser("Enter minimum number of students: ");
                System.out.println(createGroupTable(groupController
                    .getGroupsSmallerThen(numberOfStudents)));

            } else if (option == 5) {
                System.out.println(createGroupStudentsTable(groupController
                    .getAllGroups()));

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                MainFacade.callUpMainMenu();

            } else {
                System.out.println(WRONG_INPUT);
            }
        }
    }
}
