/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 13, 2024
 * MainApp.java
 * This is the main class for the Staff Log Management System. It provides a menu-based interface for managing
 * staff members and loading data from a text file.
 */
package com.devaneystafflog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class MainApp {
    /**
     * Method: main
     * Purpose: Starts the application by loading staff data from a file and initializing the manager.
     * Arguments:
     *   - args: Command-line arguments.
     * Return: void
     */
    public static void main(String[] args) {
        StaffLogManager manager = new StaffLogManager();
        FileIO fileIO = new FileIO();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Staff Log Management System ---");
            System.out.println("1. Add Staff Member");
            System.out.println("2. Remove Staff Member");
            System.out.println("3. Update Staff Member");
            System.out.println("4. Display All Staff Members");
            System.out.println("5. Search Staff Member's Last Flex/Float");
            System.out.println("6. Load Staff Members from Text File");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        String name = promptValidatedName(scanner);
                        String teamID = promptValidatedTeamID(scanner);
                        Date startDate = promptValidatedDate(scanner);
                        LocalDateTime lastFlex = promptValidatedDateTime(scanner, "Enter Last Flex Date and Time (MM/dd/yyyy HH:mm): ");
                        LocalDateTime lastFloat = promptValidatedDateTime(scanner, "Enter Last Float Date and Time (MM/dd/yyyy HH:mm): ");
                        String phoneNumber = promptValidatedPhoneNumber(scanner);

                        if (!manager.isDuplicate(teamID)) {
                            StaffMember newStaff = new StaffMember(name, teamID, startDate, lastFlex, lastFloat, phoneNumber);
                            manager.addStaff(newStaff);
                        } else {
                            System.out.println("Duplicate TeamID. This staff member already exists.");
                        }
                        manager.displayAllStaff();
                        break;

                    case 2:
                        System.out.print("Enter Name or TeamID to remove: ");
                        String identifier = scanner.nextLine().trim();
                        manager.removeStaff(identifier);
                        manager.displayAllStaff();
                        break;

                    case 3:
                        System.out.print("Enter Name or TeamID to update: ");
                        String updateIdentifier = scanner.nextLine().trim();
                        LocalDateTime updatedLastFlex = promptValidatedDateTime(scanner, "Enter new Last Flex Date and Time (MM/dd/yyyy HH:mm): ");
                        LocalDateTime updatedLastFloat = promptValidatedDateTime(scanner, "Enter new Last Float Date and Time (MM/dd/yyyy HH:mm): ");
                        String newPhoneNumber = promptValidatedPhoneNumber(scanner);

                        manager.updateStaff(updateIdentifier, newPhoneNumber, updatedLastFlex, updatedLastFloat);
                        manager.displayAllStaff();
                        break;

                    case 4:
                        manager.displayAllStaff();
                        break;

                    case 5:
                        System.out.print("Enter Name or TeamID to search for last flex/float: ");
                        String searchIdentifier = scanner.nextLine().trim();
                        manager.displayLastFlexFloat(searchIdentifier);
                        break;

                    case 6:
                        System.out.print("Enter the file name to load data from: ");
                        String fileName = scanner.nextLine().trim();
                        fileIO.loadFromFile(fileName, manager);  // Now appends data without overwriting
                        manager.displayAllStaff();
                        break;

                    case 7:
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

    private static String promptValidatedName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Enter Name (First and Last Name): ");
            name = scanner.nextLine().trim();
            try {
                new StaffMember(name, "000000", new Date(), LocalDateTime.now(), LocalDateTime.now(), "000-000-0000");
                break; // Name is valid if no exception is thrown
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return name;
    }

    private static String promptValidatedTeamID(Scanner scanner) {
        String teamID;
        while (true) {
            System.out.print("Enter TeamID (6 digits): ");
            teamID = scanner.nextLine().trim();
            try {
                new StaffMember("Test Name", teamID, new Date(), LocalDateTime.now(), LocalDateTime.now(), "000-000-0000");
                break; // TeamID is valid if no exception is thrown
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return teamID;
    }

    private static String promptValidatedPhoneNumber(Scanner scanner) {
        String phoneNumber;
        while (true) {
            System.out.print("Enter Phone Number (777-777-7777): ");
            phoneNumber = scanner.nextLine().trim();
            try {
                new StaffMember("Test Name", "000000", new Date(), LocalDateTime.now(), LocalDateTime.now(), phoneNumber);
                break; // Phone number is valid if no exception is thrown
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return phoneNumber;
    }

    private static Date promptValidatedDate(Scanner scanner) {
        Date startDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false); // Enforces date validation
        while (startDate == null) {
            System.out.print("Enter Start Date (MM/dd/yyyy): ");
            try {
                String dateInput = scanner.nextLine().trim();
                startDate = dateFormat.parse(dateInput);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in MM/dd/yyyy format.");
            }
        }
        return startDate;
    }

    private static LocalDateTime promptValidatedDateTime(Scanner scanner, String prompt) {
        LocalDateTime dateTime = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        while (dateTime == null) {
            System.out.print(prompt);
            try {
                String dateTimeInput = scanner.nextLine().trim();
                dateTime = LocalDateTime.parse(dateTimeInput, dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date and time format. Please enter in the format MM/dd/yyyy HH:mm.");
            }
        }
        return dateTime;
    }
}
