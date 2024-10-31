/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 20, 2024
 * StaffLogManager.java
 * This class manages the list of staff members, allowing additions, removals, updates,
 * and display of staff details. It includes features for reporting staff flex/float dates
 * and generating reports based on monthly and yearly flex/float data.
 */
package com.devaneystafflog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StaffLogManager {
    public List<StaffMember> staffList;

    // Constructor
    public StaffLogManager() {
        this.staffList = new ArrayList<>();
    }

    // Add a new staff member
    public void addStaff(StaffMember staff) {
        staffList.add(staff);
        System.out.println("Staff member added successfully: " + staff.getName());
    }

    // Check if a staff member with the same TeamID already exists
    public boolean isDuplicate(String teamID) {
        return staffList.stream().anyMatch(staff -> staff.getTeamID().equalsIgnoreCase(teamID.trim()));
    }

    // Remove a staff member by name or TeamID
    public void removeStaff(String identifier, Scanner scanner) {
        List<StaffMember> matchingStaff = findStaffByNameOrTeamID(identifier);

        if (matchingStaff.isEmpty()) {
            System.out.println("No staff member found.");
        } else if (matchingStaff.size() == 1) {
            staffList.remove(matchingStaff.get(0));
            System.out.println("Staff member removed successfully.");
        } else {
            // Multiple matching staff members found, ask for clarification
            String teamID = promptForTeamID(matchingStaff, scanner);
            Optional<StaffMember> staffToRemove = staffList.stream()
                    .filter(staff -> staff.getTeamID().equalsIgnoreCase(teamID.trim()))
                    .findFirst();

            if (staffToRemove.isPresent()) {
                staffList.remove(staffToRemove.get());
                System.out.println("Staff member removed successfully.");
            } else {
                System.out.println("No staff member found with TeamID: " + teamID);
            }
        }
    }

    // Update staff member details by Name or TeamID
    public void updateStaff(String identifier, String newPhoneNumber, LocalDateTime updatedLastFlex, LocalDateTime updatedLastFloat, Scanner scanner) {
        List<StaffMember> matchingStaff = findStaffByNameOrTeamID(identifier);

        if (matchingStaff.isEmpty()) {
            System.out.println("No staff member found.");
        } else if (matchingStaff.size() == 1) {
            updateStaffDetails(matchingStaff.get(0), newPhoneNumber, updatedLastFlex, updatedLastFloat);
        } else {
            // Multiple matching staff members found, ask for clarification
            String teamID = promptForTeamID(matchingStaff, scanner);
            Optional<StaffMember> staffToUpdate = staffList.stream()
                    .filter(staff -> staff.getTeamID().equalsIgnoreCase(teamID.trim()))
                    .findFirst();

            if (staffToUpdate.isPresent()) {
                updateStaffDetails(staffToUpdate.get(), newPhoneNumber, updatedLastFlex, updatedLastFloat);
            } else {
                System.out.println("No staff member found with TeamID: " + teamID);
            }
        }
    }

    // Helper method to find staff by name or teamID
    private List<StaffMember> findStaffByNameOrTeamID(String identifier) {
        return staffList.stream()
                .filter(staff -> staff.getName().equalsIgnoreCase(identifier.trim()) || staff.getTeamID().equalsIgnoreCase(identifier.trim()))
                .toList();
    }

    // Helper method to prompt for teamID when there are multiple matching names
    private String promptForTeamID(List<StaffMember> matchingStaff, Scanner scanner) {
        System.out.println("Multiple staff members found with the same name:");
        for (StaffMember staff : matchingStaff) {
            System.out.println("Name: " + staff.getName() + " | TeamID: " + staff.getTeamID());
        }
        System.out.print("Enter the correct TeamID: ");
        return scanner.nextLine().trim();
    }

    // Helper method to update staff details
    private void updateStaffDetails(StaffMember staff, String newPhoneNumber, LocalDateTime updatedLastFlex, LocalDateTime updatedLastFloat) {
        staff.setPhoneNumber(newPhoneNumber);
        staff.addFlexDate(updatedLastFlex);
        staff.addFloatDate(updatedLastFloat);
        System.out.println("Staff member updated successfully.");
    }

    // Display all staff members
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

    // Search and display last flex/float by TeamID or Name
    public void displayLastFlexFloat(String identifier, Scanner scanner) {
        List<StaffMember> matchingStaff = findStaffByNameOrTeamID(identifier);

        if (matchingStaff.isEmpty()) {
            System.out.println("No staff member found.");
        } else if (matchingStaff.size() == 1) {
            displayFlexFloatDetails(matchingStaff.get(0));
        } else {
            // Multiple matching staff members found, ask for clarification
            String teamID = promptForTeamID(matchingStaff, scanner);
            Optional<StaffMember> staffToDisplay = staffList.stream()
                    .filter(staff -> staff.getTeamID().equalsIgnoreCase(teamID.trim()))
                    .findFirst();

            if (staffToDisplay.isPresent()) {
                displayFlexFloatDetails(staffToDisplay.get());
            } else {
                System.out.println("No staff member found with TeamID: " + teamID);
            }
        }
    }

    // Helper method to display flex/float details
    private void displayFlexFloatDetails(StaffMember staff) {
        System.out.println("Staff Member: " + staff.getName() + " | TeamID: " + staff.getTeamID());
        System.out.println("Last Flex Date and Time: " + staff.getLastFlex());
        System.out.println("Last Float Date and Time: " + staff.getLastFloat());
    }

    // Generate monthly float report
    public void generateMonthlyFloatReport(int month, int year) {
        List<StaffMember> floatedStaff = staffList.stream()
                .filter(staff -> staff.getFloatDates().stream()
                        .anyMatch(date -> date.getMonthValue() == month && date.getYear() == year))
                .toList();

        System.out.println("Number of staff who floated in " + month + "/" + year + ": " + floatedStaff.size());

        if (!floatedStaff.isEmpty()) {
            System.out.println("Staff members who floated:");
            for (StaffMember staff : floatedStaff) {
                System.out.println(" - " + staff.getName() + " (TeamID: " + staff.getTeamID() + ")");
            }
        }
    }

    // Generate yearly flex/float report
    public void generateYearlyFlexFloatReport(int year) {
        long flexCount = staffList.stream()
                .flatMap(staff -> staff.getFlexDates().stream())
                .filter(date -> date.getYear() == year)
                .count();

        long floatCount = staffList.stream()
                .flatMap(staff -> staff.getFloatDates().stream())
                .filter(date -> date.getYear() == year)
                .count();

        System.out.println("Number of staff who flexed in year " + year + ": " + flexCount);
        System.out.println("Number of staff who floated in year " + year + ": " + floatCount);
    }


    // Check if a staff member exists by Name or TeamID
    public boolean staffExists(String identifier) {
        return !findStaffByNameOrTeamID(identifier).isEmpty();
    }

    // Return all staff members as a List for GUI table display
    public List<StaffMember> getAllStaffMembers() {
        return new ArrayList<>(staffList);
    }

    // Find a staff member by identifier and return it for update/search display in GUI
    public StaffMember getStaffByIdentifier(String identifier) {
        return staffList.stream()
                .filter(staff -> staff.getName().equalsIgnoreCase(identifier.trim()) || staff.getTeamID().equalsIgnoreCase(identifier.trim()))
                .findFirst()
                .orElse(null);
    }


}
