/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 13, 2024
 * MainApp.java
 * This is the main class for the Staff Log Management System. It provides a menu-based interface for managing
 * staff members and loading data from a text file. The system allows users to add, remove, update staff members,
 * search for staff details, and generate reports based on flex and float data.
 */
package com.devaneystafflog;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.Date;

public class MainApp {

    /**
     * Method: main
     * Purpose: The main entry point for the Staff Log Management System. This method presents a menu-based
     * interface that allows users to perform various operations such as adding, removing, updating staff,
     * generating reports, and loading data from a file.
     * Arguments:
     *   - args: Command-line arguments (not used in this program).
     * Return: void
     */
    public static void main(String[] args) {
        StaffLogManager manager = new StaffLogManager();
        Scanner scanner = new Scanner(System.in);
        InputValidator validator = new InputValidator(scanner);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Staff Log Management System ---");
            System.out.println("1. Add Staff Member");
            System.out.println("2. Remove Staff Member");
            System.out.println("3. Update Staff Member");
            System.out.println("4. Display All Staff Members");
            System.out.println("5. Search Staff Member's Last Flex/Float");
            System.out.println("6. Generate Monthly Float Report");
            System.out.println("7. Generate Yearly Flex/Float Report");
            System.out.println("8. Load Staff Members from Text File");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1: // Adding a new staff member
                        String name = validator.promptValidatedName();
                        String teamID = validator.promptValidatedTeamID();
                        Date startDate = validator.promptValidatedDate();
                        LocalDateTime lastFlex = validator.promptValidatedDateTime("Enter Last Flex Date and Time (MM-dd-yyyy HH:mm): ");
                        LocalDateTime lastFloat = validator.promptValidatedDateTime("Enter Last Float Date and Time (MM-dd-yyyy HH:mm): ");
                        String phoneNumber = validator.promptValidatedPhoneNumber();

                        if (!manager.isDuplicate(teamID)) {
                            StaffMember newStaff = new StaffMember(name, teamID, startDate, phoneNumber);
                            newStaff.addFlexDate(lastFlex);
                            newStaff.addFloatDate(lastFloat);
                            manager.addStaff(newStaff);
                        } else {
                            System.out.println("Duplicate TeamID. This staff member already exists.");
                        }
                        manager.displayAllStaff();
                        break;

                    case 2:// Removing a staff member
                        System.out.print("Enter Name or TeamID to remove: ");
                        String identifier = scanner.nextLine().trim();
                        manager.removeStaff(identifier, scanner);
                        manager.displayAllStaff();
                        break;

                    case 3: // Updating a staff member
                        System.out.print("Enter Name or TeamID to update: ");
                        String updateIdentifier = scanner.nextLine().trim();
                        if (manager.isDuplicate(updateIdentifier)) {
                            LocalDateTime updatedLastFlex = validator.promptValidatedDateTime("Enter new Last Flex Date and Time (MM-dd-yyyy HH:mm): ");
                            LocalDateTime updatedLastFloat = validator.promptValidatedDateTime("Enter new Last Float Date and Time (MM-dd-yyyy HH:mm): ");
                            String newPhoneNumber = validator.promptValidatedPhoneNumber();
                            manager.updateStaff(updateIdentifier, newPhoneNumber, updatedLastFlex, updatedLastFloat, scanner);
                            manager.displayAllStaff();
                        } else {
                            System.out.println("Staff member not found. Please enter a valid name or TeamID.");
                        }
                        break;

                    case 4:// Displaying all staff members
                        manager.displayAllStaff();
                        break;

                    case 5: // Searching for a staff member's last flex/float
                        System.out.print("Enter Name or TeamID to search for last flex/float: ");
                        String searchIdentifier = scanner.nextLine().trim();
                        manager.displayLastFlexFloat(searchIdentifier, scanner);
                        break;

                    case 6:// Generating a monthly float report
                        int reportMonth = validator.promptValidatedMonth();
                        int reportYear = validator.promptValidatedYear();
                        manager.generateMonthlyFloatReport(reportMonth, reportYear);
                        break;

                    case 7:// Generating a yearly flex/float report
                        int reportYearForFlexFloat = validator.promptValidatedYear();
                        manager.generateYearlyFlexFloatReport(reportYearForFlexFloat);
                        break;

                    case 8: // Loading staff members from a text file
                        System.out.print("Enter the file name to load data from: ");
                        String fileName = scanner.nextLine().trim();
                        FileIO fileIO = new FileIO();
                        fileIO.loadFromFile(fileName, manager);
                        manager.displayAllStaff();
                        break;

                    case 9: // Exiting the program
                        exit = true;
                        System.out.println("Exiting Staff Log Management System. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for menu selection.");
            }
        }
        scanner.close();
    }
}
