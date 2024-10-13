/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 13, 2024
 * StaffLogManager.java
 * This class manages the list of staff members, allowing additions, removals, updates, and display of staff details.
 */
package com.devaneystafflog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffLogManager {
    public List<StaffMember> staffList;

    // Constructor
    /**
     * Method: StaffLogManager (Constructor)
     * Purpose: Initializes an empty list to store StaffMember objects.
     * Arguments: None
     * Return: None
     */
    public StaffLogManager() {
        this.staffList = new ArrayList<>();
    }

    /**
     * Method: addStaff
     * Purpose: Adds a new StaffMember to the staff list.
     * Arguments:
     *   - staff: A StaffMember object to be added.
     * Return: void
     */
    public void addStaff(StaffMember staff) {
        staffList.add(staff);
        System.out.println("Staff member added successfully: " + staff.getName());
    }

    /**
     * Method: isDuplicate
     * Purpose: Checks if a staff member with the given TeamID already exists in the list to avoid duplicates.
     * Arguments:
     *   - teamID: A string representing the TeamID of the staff member.
     * Return:
     *   - boolean: Returns true if a duplicate is found, otherwise false.
     */
    public boolean isDuplicate(String teamID) {
        return staffList.stream().anyMatch(staff -> staff.getTeamID().equalsIgnoreCase(teamID));
    }

    /**
     * Method: removeStaff
     * Purpose: Removes a staff member from the list by either name or TeamID.
     * Arguments:
     *   - identifier: A string representing either the name or TeamID of the staff member to be removed.
     * Return: void
     */
    public void removeStaff(String identifier) {
        Optional<StaffMember> staffToRemove = staffList.stream()
                .filter(staff -> staff.getName().equalsIgnoreCase(identifier) || staff.getTeamID().equalsIgnoreCase(identifier))
                .findFirst();

        if (staffToRemove.isPresent()) {
            staffList.remove(staffToRemove.get());
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member not found.");
        }
    }

    /**
     * Method: updateStaff
     * Purpose: Updates the contact details and work dates (last flex and float) of a staff member.
     * Arguments:
     *   - identifier: A string representing either the name or TeamID of the staff member.
     *   - newPhoneNumber: The updated phone number as a string.
     *   - updatedLastFlex: A LocalDateTime representing the new last flex date and time.
     *   - updatedLastFloat: A LocalDateTime representing the new last float date and time.
     * Return: void
     */
    public void updateStaff(String identifier, String newPhoneNumber, LocalDateTime updatedLastFlex, LocalDateTime updatedLastFloat) {
        Optional<StaffMember> staffToUpdate = staffList.stream()
                .filter(staff -> staff.getName().equalsIgnoreCase(identifier) || staff.getTeamID().equalsIgnoreCase(identifier))
                .findFirst();

        if (staffToUpdate.isPresent()) {
            StaffMember staff = staffToUpdate.get();
            staff.setPhoneNumber(newPhoneNumber);
            staff.setLastFlex(updatedLastFlex);
            staff.setLastFloat(updatedLastFloat);
            System.out.println("Staff member updated successfully.");
        } else {
            System.out.println("Staff member not found.");
        }
    }

    /**
     * Method: displayAllStaff
     * Purpose: Displays all staff members in the list along with their details.
     * Arguments: None
     * Return: void
     */
    public void displayAllStaff() {
        if (staffList.isEmpty()) {
            System.out.println("No staff members found.");
        } else {
            System.out.println("List of Staff Members:");
            for (StaffMember staff : staffList) {
                System.out.println(staff);
            }
        }
    }

    /**
     * Method: displayLastFlexFloat
     * Purpose: Displays the last flex and float dates for a specific staff member by name or TeamID.
     * Arguments:
     *   - identifier: A string representing either the name or TeamID of the staff member.
     * Return: void
     */
    public void displayLastFlexFloat(String identifier) {
        Optional<StaffMember> staffToDisplay = staffList.stream()
                .filter(staff -> staff.getName().equalsIgnoreCase(identifier) || staff.getTeamID().equalsIgnoreCase(identifier))
                .findFirst();

        if (staffToDisplay.isPresent()) {
            StaffMember staff = staffToDisplay.get();
            System.out.println("Staff Member: " + staff.getName() + " | TeamID: " + staff.getTeamID());
            System.out.println("Last Flex Date and Time: " + staff.getLastFlex());
            System.out.println("Last Float Date and Time: " + staff.getLastFloat());
        } else {
            System.out.println("Staff member not found.");
        }
    }
}
