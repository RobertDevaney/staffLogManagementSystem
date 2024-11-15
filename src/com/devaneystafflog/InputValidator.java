/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 20, 2024
 * InputValidator.java
 * This class provides utility methods to validate various types of user inputs,
 * such as names, dates, phone numbers, and more. It is a part of the
 * Database Management System (DMS) project and ensures user inputs
 * meet specific format and validation criteria.
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

    /**
     * Constructor: InputValidator
     * Initializes the InputValidator object with a Scanner instance.
     *
     * @param scanner a Scanner object used for reading user inputs.
     */
    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Method: promptValidatedName
     * Prompts the user to enter a name and validates its format.
     *
     * @return a valid name string in "First Last" format.
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
                    throw new IllegalArgumentException(
                            "Invalid name format. Please enter both first and last name (e.g., John Doe)."
                    );
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method: promptValidatedTeamID
     * Prompts the user to enter a Team ID and validates its format.
     *
     * @return a valid Team ID string consisting of exactly 6 digits.
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
                    throw new IllegalArgumentException(
                            "Invalid TeamID format. Please enter exactly 6 digits (e.g., 548063)."
                    );
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method: promptValidatedPhoneNumber
     * Prompts the user to enter a phone number and validates its format.
     *
     * @return a valid phone number string in the format "777-777-7777".
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
                    throw new IllegalArgumentException(
                            "Invalid phone number format. Please enter in the format 777-777-7777."
                    );
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method: promptValidatedDate
     * Prompts the user to enter a date and validates its format.
     *
     * @return a valid Date object in the "yyyy-MM-dd" format.
     */
    public Date promptValidatedDate() {
        Date startDate = null;
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        while (startDate == null) {
            System.out.print("Enter Start Date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine().trim();

            if (dateInput.matches(datePattern)) {
                try {
                    startDate = dateFormat.parse(dateInput);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            } else {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        return startDate;
    }

    /**
     * Method: promptValidatedDateTime
     * Prompts the user to enter a date and time, then validates its format.
     *
     * @param prompt the message displayed to the user for input.
     * @return a valid LocalDateTime object in the "yyyy-MM-dd HH:mm:ss" format.
     */
    public LocalDateTime promptValidatedDateTime(String prompt) {
        LocalDateTime dateTime = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (dateTime == null) {
            System.out.print(prompt);
            try {
                String dateTimeInput = scanner.nextLine().trim();
                dateTime = LocalDateTime.parse(dateTimeInput, dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date and time format. Please enter in the format yyyy-MM-dd HH:mm:ss.");
            }
        }
        return dateTime;
    }

    /**
     * Method: getScanner
     * Retrieves the Scanner object used for user input.
     *
     * @return the Scanner instance associated with this class.
     */
    public Scanner getScanner() {
        return this.scanner;
    }
}
