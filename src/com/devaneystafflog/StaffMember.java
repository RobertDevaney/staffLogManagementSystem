/**
 * Robert Devaney
 * CEN-3024C-15339
 * November 15, 2024
 * StaffMember.java
 * This class represents a staff member, including details such as name, teamID, start date, last flex, last float,
 * and phone number. Each field has validation checks to ensure correct format and data integrity.
 */
package com.devaneystafflog;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaffMember {
    private String name;
    private String teamID;
    private Date startDate;
    private String phoneNumber;
    private List<LocalDateTime> flexDates;
    private List<LocalDateTime> floatDates;

    /**
     * Constructor: StaffMember
     * Initializes a StaffMember object with the provided details.
     *
     * @param name        the staff member's name in "First Last" format.
     * @param teamID      the 6-digit team ID of the staff member.
     * @param startDate   the start date of the staff member as a Date object.
     * @param phoneNumber the staff member's contact number in the format "XXX-XXX-XXXX".
     */
    public StaffMember(String name, String teamID, Date startDate, String phoneNumber) {
        setName(name);
        setTeamID(teamID);
        setStartDate(startDate);
        setPhoneNumber(phoneNumber);
        this.flexDates = new ArrayList<>();
        this.floatDates = new ArrayList<>();
    }
    /**
     * Converts a LocalDateTime to a Timestamp object.
     *
     * @param dateTime the LocalDateTime to be converted.
     * @return the corresponding Timestamp object or null if dateTime is null.
     */
    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? Timestamp.valueOf(dateTime) : null;
    }


    // Getters and Setters with Validation

    /**
     * Gets the name of the staff member.
     *
     * @return the staff member's name.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the staff member.
     *
     * @param name the name in "First Last" format.
     * @throws IllegalArgumentException if the name format is invalid.
     */
    public void setName(String name) {
        if (name.matches("^[A-Za-z]+\\s[A-Za-z]+$")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name format. Please enter both first and last name (e.g., John Doe).");
        }
    }
    /**
     * Gets the team ID of the staff member.
     *
     * @return the staff member's team ID.
     */
    public String getTeamID() {
        return teamID;
    }

    /**
     * Sets the team ID of the staff member.
     *
     * @param teamID the 6-digit team ID.
     * @throws IllegalArgumentException if the team ID format is invalid.
     */
    public void setTeamID(String teamID) {
        if (teamID.matches("^\\d{6}$")) {
            this.teamID = teamID;
        } else {
            throw new IllegalArgumentException("Invalid TeamID format. Please enter exactly 6 digits (e.g., 548063).");
        }
    }

    /**
     * Gets the start date of the staff member.
     *
     * @return the start date as a Date object.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the staff member.
     *
     * @param startDate the start date as a Date object.
     * @throws IllegalArgumentException if the start date is null.
     */
    public void setStartDate(Date startDate) {
        if (startDate != null) {
            this.startDate = startDate;
        } else {
            throw new IllegalArgumentException("Start date cannot be null.");
        }
    }

    /**
     * Gets the phone number of the staff member.
     *
     * @return the staff member's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the staff member.
     *
     * @param phoneNumber the phone number in "XXX-XXX-XXXX" format.
     * @throws IllegalArgumentException if the phone number format is invalid.
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number format. Please enter in the format 777-777-7777.");
        }
    }

    // Methods to manage flex and float dates

    /**
     * Adds a flex date for the staff member.
     *
     * @param date the LocalDateTime to be added as a flex date.
     */
    public void addFlexDate(LocalDateTime date) {
        this.flexDates.add(date);
    }

    /**
     * Adds a float date for the staff member.
     *
     * @param date the LocalDateTime to be added as a float date.
     */
    public void addFloatDate(LocalDateTime date) {
        this.floatDates.add(date);
    }

    /**
     * Gets all flex dates of the staff member.
     *
     * @return a list of LocalDateTime objects representing the flex dates.
     */
    public List<LocalDateTime> getFlexDates() {
        return flexDates;
    }

    /**
     * Gets all float dates of the staff member.
     *
     * @return a list of LocalDateTime objects representing the float dates.
     */
    public List<LocalDateTime> getFloatDates() {
        return floatDates;
    }

    /**
     * Gets the most recent flex date of the staff member.
     *
     * @return the most recent flex date as a Timestamp, or null if no dates exist.
     */
    public Timestamp getLastFlex() {
        if (flexDates.isEmpty()) {
            return null;
        }
        return toTimestamp(flexDates.get(flexDates.size() - 1));
    }

    /**
     * Gets the most recent float date of the staff member.
     *
     * @return the most recent float date as a Timestamp, or null if no dates exist.
     */
    public Timestamp getLastFloat() {
        if (floatDates.isEmpty()) {
            return null;
        }
        return toTimestamp(floatDates.get(floatDates.size() - 1));
    }

    /**
     * Returns a string representation of the staff member's details.
     *
     * @return a string containing the staff member's details.
     */
    @Override
    public String toString() {
        return "StaffMember{" +
                "name='" + name + '\'' +
                ", teamID='" + teamID + '\'' +
                ", startDate=" + startDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", lastFlex=" + (getLastFlex() != null ? getLastFlex() : "No flex dates") +
                ", lastFloat=" + (getLastFloat() != null ? getLastFloat() : "No float dates") +
                '}';
    }
}
