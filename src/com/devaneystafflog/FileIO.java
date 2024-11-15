/**
 * Robert Devaney
 * CEN-3024C-15339
 * November 15, 2024
 * FileIO.java
 * This class handles reading staff data from a file and converting it into StaffMember objects. It ensures proper
 * parsing of staff details such as name, teamID, and dates.
 */
package com.devaneystafflog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class FileIO {
    /**
     * Method: loadFromFile
     * Purpose: Reads data from a text file and converts it into a list of StaffMember objects.
     * Arguments:
     *   - filePath: the path to the file as a string
     *   - manager: The StaffLogManager instance where the staff data will be added.
     * Return: void
     */
    public void loadFromFile(String filePath, StaffLogManager manager) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length >= 6) {
                    String name = data[0];
                    String teamID = data[1];
                    String startDateStr = data[2];
                    String phoneNumber = data[3];
                    String[] flexDatesStr = data[4].split(";");
                    String[] floatDatesStr = data[5].split(";");

                    // Parse start date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

                    try {
                        Date startDate = dateFormat.parse(startDateStr);

                        // Create a new StaffMember object
                        StaffMember newStaff = new StaffMember(name, teamID, startDate, phoneNumber);

                        // Parse and add all flex dates
                        Arrays.stream(flexDatesStr).forEach(flexDateStr -> {
                            try {
                                LocalDateTime flexDate = LocalDateTime.parse(flexDateStr, dateTimeFormatter);
                                newStaff.addFlexDate(flexDate);
                            } catch (Exception e) {
                                System.err.println("Error parsing flex date: " + flexDateStr);
                            }
                        });

                        // Parse and add all float dates
                        Arrays.stream(floatDatesStr).forEach(floatDateStr -> {
                            try {
                                LocalDateTime floatDate = LocalDateTime.parse(floatDateStr, dateTimeFormatter);
                                newStaff.addFloatDate(floatDate);
                            } catch (Exception e) {
                                System.err.println("Error parsing float date: " + floatDateStr);
                            }
                        });

                        // Check if the staff already exists before adding to avoid duplicates
                        if (!manager.isDuplicate(teamID)) {
                            manager.addStaff(newStaff);
                        } else {
                            System.out.println("Duplicate TeamID found: " + teamID + ". Skipping entry.");
                        }

                    } catch (Exception e) {
                        System.err.println("Error parsing start date or adding staff member: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
            System.out.println("Data loaded successfully from file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }
}