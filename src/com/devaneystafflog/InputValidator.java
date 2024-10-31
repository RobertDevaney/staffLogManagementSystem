/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 20, 2024
 * InputValidator.java
 * This class encapsulates methods for validating user inputs such as names, dates, phone numbers, and more.
 */
package com.devaneystafflog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Scanner;

public class InputValidator {
    private Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }
    /**
     * Method: promptValidatedName
     * Purpose: Prompts the user for a valid name input and ensures it is correctly formatted
     * (e.g., "First Last").
     * Arguments: None
     * Return: String - The validated name in "First Last" format.
     */
    public String promptValidatedName() {
        String name;
        while (true) {
            System.out.print("Enter Name (First and Last Name): ");
            name = scanner.nextLine().trim();
            try {
                if (name.matches("^[A-Za-z]+\\s[A-Za-z]+$")) {
                    return name;
                } else {
                    throw new IllegalArgumentException("Invalid name format. Please enter both first and last name (e.g., John Doe).");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Method: promptValidatedTeamID
     * Purpose: Prompts the user for a valid TeamID, ensuring it is exactly 6 digits long.
     * Arguments: None
     * Return: String - The validated TeamID.
     */
    public String promptValidatedTeamID() {
        String teamID;
        while (true) {
            System.out.print("Enter TeamID (6 digits): ");
            teamID = scanner.nextLine().trim();
            try {
                if (teamID.matches("^\\d{6}$")) {
                    return teamID;
                } else {
                    throw new IllegalArgumentException("Invalid TeamID format. Please enter exactly 6 digits (e.g., 548063).");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Method: promptValidatedPhoneNumber
     * Purpose: Prompts the user for a valid phone number in the format "XXX-XXX-XXXX".
     * Arguments: None
     * Return: String - The validated phone number.
     */
    public String promptValidatedPhoneNumber() {
        String phoneNumber;
        while (true) {
            System.out.print("Enter Phone Number (777-777-7777): ");
            phoneNumber = scanner.nextLine().trim();
            try {
                if (phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
                    return phoneNumber;
                } else {
                    throw new IllegalArgumentException("Invalid phone number format. Please enter in the format 777-777-7777.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Method: promptValidatedDate
     * Purpose: Prompts the user for a valid date in the format "MM-dd-yyyy" and ensures it is correctly parsed.
     * Arguments: None
     * Return: Date - The validated date as a Date object.
     */
    public Date promptValidatedDate() {
        Date startDate = null;
        String datePattern = "\\d{2}-\\d{2}-\\d{4}";  // Regex pattern for MM-dd-yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        dateFormat.setLenient(false);  // Enforces strict date validation

        while (startDate == null) {
            System.out.print("Enter Start Date (MM-dd-yyyy): ");
            String dateInput = scanner.nextLine().trim();

            // Validate the input against the date pattern
            if (dateInput.matches(datePattern)) {
                try {
                    startDate = dateFormat.parse(dateInput);  // Parse the validated date string
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter the date in MM-dd-yyyy format.");
                }
            } else {
                System.out.println("Invalid date format. Please enter the date in MM-dd-yyyy format.");
            }
        }
        return startDate;
    }

    /**
     * Method: promptValidatedDateTime
     * Purpose: Prompts the user for a valid date and time input in the format "MM-dd-yyyy HH:mm" and ensures it is
     * parsed into a LocalDateTime object.
     * Arguments:
     *   - prompt: String representing the message to display to the user.
     * Return: LocalDateTime - The validated date and time as a LocalDateTime object.
     */
    public LocalDateTime promptValidatedDateTime(String prompt) {
        LocalDateTime dateTime = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        while (dateTime == null) {
            System.out.print(prompt);
            try {
                String dateTimeInput = scanner.nextLine().trim();
                dateTime = LocalDateTime.parse(dateTimeInput, dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date and time format. Please enter in the format MM-dd-yyyy HH:mm.");
            }
        }
        return dateTime;
    }
    /**
     * Method: promptValidatedMonth
     * Purpose: Prompts the user for a valid month (1-12) and ensures it is within the valid range.
     * Arguments: None
     * Return: int - The validated month as an integer.
     */
    public int promptValidatedMonth() {
        int month = -1;
        while (month < 1 || month > 12) {
            System.out.print("Enter the month (1-12) for the report: ");
            try {
                month = Integer.parseInt(scanner.nextLine().trim());
                if (month < 1 || month > 12) {
                    System.out.println("Invalid month. Please enter a valid month between 1 and 12.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 12.");
            }
        }
        return month;
    }
    /**
     * Method: promptValidatedYear
     * Purpose: Prompts the user for a valid year and ensures it is a 4-digit number within a reasonable range.
     * Arguments: None
     * Return: int - The validated year as a 4-digit integer.
     */
    public int promptValidatedYear() {
        int year = -1;
        while (year < 1000 || year > 9999) {  // Enforces a valid 4-digit year
            System.out.print("Enter the year for the report: ");
            try {
                year = Integer.parseInt(scanner.nextLine().trim());
                if (year < 1000 || year > 9999) {
                    System.out.println("Invalid year. Please enter a valid four-digit year (e.g., 2024).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid four-digit year (e.g., 2024).");
            }
        }
        return year;
    }

    public Date validateDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        dateFormat.setLenient(false);
        return dateFormat.parse(dateStr);
    }

    public LocalDateTime validateDateTime(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }


    public Scanner getScanner() {
        return null;
    }
}

