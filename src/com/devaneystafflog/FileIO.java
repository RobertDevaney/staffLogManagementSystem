/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 13, 2024
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

public class FileIO {
    /**
     * Method: loadFromFile
     * Purpose: Reads data from a text file and converts it into a list of StaffMember objects.
     * Arguments:
     *   - filePath: the path to the file as a string
     * Return:
     *   - A list of StaffMember objects parsed from the file.
     */

    public void loadFromFile(String filePath, StaffLogManager manager) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length == 6) {
                    String name = data[0];
                    String teamID = data[1];
                    String startDateStr = data[2];
                    String lastFlexStr = data[3];
                    String lastFloatStr = data[4];
                    String phoneNumber = data[5];

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
                    LocalDateTime lastFlex = LocalDateTime.parse(lastFlexStr, dateTimeFormatter);
                    LocalDateTime lastFloat = LocalDateTime.parse(lastFloatStr, dateTimeFormatter);

                    StaffMember newStaff = new StaffMember(name, teamID, new SimpleDateFormat("MM/dd/yyyy").parse(startDateStr), lastFlex, lastFloat, phoneNumber);

                    // Check if the staff already exists before adding to avoid duplicates
                    if (!manager.isDuplicate(teamID)) {
                        manager.addStaff(newStaff);
                    } else {
                        System.out.println("Duplicate TeamID found: " + teamID + ". Skipping entry.");
                    }
                }
            }
            System.out.println("Data loaded successfully from file: " + filePath);
        } catch (IOException | java.text.ParseException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }
}
