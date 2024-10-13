/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 13, 2024
 * StaffMember.java
 * This class represents a staff member, including details such as name, teamID, start date, last flex, last float,
 * and phone number. Each field has validation checks to ensure correct format and data integrity.
 */
package com.devaneystafflog;

import java.time.LocalDateTime;
import java.util.Date;

public class StaffMember {
    private String name;
    private String teamID;
    private Date startDate;
    private LocalDateTime lastFlex;
    private LocalDateTime lastFloat;
    private String phoneNumber;

    /**
     * Constructor: StaffMember
     * Purpose: Initializes a StaffMember object with the provided details.
     * Arguments:
     *   - name: The staff member's name in "First Last" format.
     *   - teamID: The 6-digit team ID of the staff member.
     *   - startDate: The date when the staff member started, as a Date object.
     *   - lastFlex: The last time the staff member flexed, as a LocalDateTime.
     *   - lastFloat: The last time the staff member floated to another unit, as a LocalDateTime.
     *   - phoneNumber: The staff member's contact number in the format "XXX-XXX-XXXX".
     * Return: None
     */
    public StaffMember(String name, String teamID, Date startDate, LocalDateTime lastFlex, LocalDateTime lastFloat, String phoneNumber) {
        setName(name);
        setTeamID(teamID);
        setStartDate(startDate);
        setLastFlex(lastFlex);
        setLastFloat(lastFloat);
        setPhoneNumber(phoneNumber);
    }

    // Getters and Setters with Validation
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.matches("^[A-Za-z]+\\s[A-Za-z]+$")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name format. Please enter both first and last name (e.g., John Doe).");
        }
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        if (teamID.matches("^\\d{6}$")) {
            this.teamID = teamID;
        } else {
            throw new IllegalArgumentException("Invalid TeamID format. Please enter exactly 6 digits (e.g., 548063).");
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            this.startDate = startDate;
        } else {
            throw new IllegalArgumentException("Start date cannot be null.");
        }
    }

    public LocalDateTime getLastFlex() {
        return lastFlex;
    }

    public void setLastFlex(LocalDateTime lastFlex) {
        if (lastFlex != null) {
            this.lastFlex = lastFlex;
        } else {
            throw new IllegalArgumentException("Last flex date cannot be null.");
        }
    }

    public LocalDateTime getLastFloat() {
        return lastFloat;
    }

    public void setLastFloat(LocalDateTime lastFloat) {
        if (lastFloat != null) {
            this.lastFloat = lastFloat;
        } else {
            throw new IllegalArgumentException("Last float date cannot be null.");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number format. Please enter in the format 777-777-7777.");
        }
    }

    @Override
    public String toString() {
        return "StaffMember{" +
                "name='" + name + '\'' +
                ", teamID='" + teamID + '\'' +
                ", startDate=" + startDate +
                ", lastFlex=" + lastFlex +
                ", lastFloat=" + lastFloat +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
