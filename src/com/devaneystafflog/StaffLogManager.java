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

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StaffLogManager {
    private final DatabaseConnection dbConnection;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");


    /**
     * Constructor: StaffLogManager
     * Initializes the StaffLogManager with the provided database connection.
     *
     * @param dbConnection an instance of DatabaseConnection to interact with the database.
     */
    public StaffLogManager(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }



    /**
     * Converts a Timestamp to a LocalDateTime object.
     *
     * @param timestamp the Timestamp to be converted.
     * @return a LocalDateTime object or null if the timestamp is null.
     */
    public LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }


    /**
     * Adds a new staff member to the database.
     *
     * @param staff an instance of StaffMember containing staff details.
     * @return true if the staff member is added successfully; false otherwise.
     */
    public boolean addStaff(StaffMember staff) {
        String sql = "INSERT INTO Staff (name, teamID, startDate, phoneNumber, lastFlex, lastFloat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getTeamID());
            pstmt.setDate(3, new java.sql.Date(staff.getStartDate().getTime()));
            pstmt.setString(4, staff.getPhoneNumber());
            pstmt.setTimestamp(5, staff.getLastFlex());
            pstmt.setTimestamp(6, staff.getLastFloat());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error adding staff: " + ex.getMessage());
            return false;
        }
    }
    /**
     * Checks if a staff member with the specified TeamID already exists in the database.
     *
     * @param teamID the TeamID to check for duplicates.
     * @return true if a duplicate exists; false otherwise.
     */
    public boolean isDuplicate(String teamID) {
        String sql = "SELECT COUNT(*) FROM Staff WHERE teamID = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, teamID);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            System.err.println("Error checking duplicate TeamID: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Removes a staff member from the database by their TeamID.
     *
     * @param teamID the TeamID of the staff member to be removed.
     * @return true if the staff member is removed successfully; false otherwise.
     */
    public boolean removeStaff(String teamID) {
        String sql = "DELETE FROM Staff WHERE teamID = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, teamID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error removing staff: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Updates a staff member's details in the database.
     *
     * @param teamID the TeamID of the staff member to be updated.
     * @param newPhoneNumber the updated phone number of the staff member.
     * @param updatedLastFlex the updated lastFlex date as a LocalDateTime object (nullable).
     * @param updatedLastFloat the updated lastFloat date as a LocalDateTime object (nullable).
     * @return true if the update is successful; false otherwise.
     */
    public boolean updateStaff(String teamID, String newPhoneNumber, LocalDateTime updatedLastFlex, LocalDateTime updatedLastFloat) {
        String fetchSql = "SELECT phoneNumber, lastFlex, lastFloat, lastFlexHistory, lastFloatHistory FROM Staff WHERE teamID = ?";
        String updateSql = "UPDATE Staff SET phoneNumber = ?, lastFlex = ?, lastFloat = ?, lastFlexHistory = ?, lastFloatHistory = ? WHERE teamID = ?";

        try (PreparedStatement fetchStmt = dbConnection.getConnection().prepareStatement(fetchSql)) {
            fetchStmt.setString(1, teamID);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                // Get the current values of lastFlex and lastFloat
                Timestamp existingLastFlex = rs.getTimestamp("lastFlex");
                Timestamp existingLastFloat = rs.getTimestamp("lastFloat");
                String existingFlexHistory = rs.getString("lastFlexHistory");
                String existingFloatHistory = rs.getString("lastFloatHistory");

                // Convert the new dates to timestamps if they are provided
                Long newFlexTimestamp = updatedLastFlex != null ? Timestamp.valueOf(updatedLastFlex).getTime() : null;
                Long newFloatTimestamp = updatedLastFloat != null ? Timestamp.valueOf(updatedLastFloat).getTime() : null;

                // Append the previous lastFlex to lastFlexHistory if it exists
                String updatedFlexHistory = (existingLastFlex != null)
                        ? (existingFlexHistory == null ? String.valueOf(existingLastFlex.getTime()) : existingFlexHistory + "," + existingLastFlex.getTime())
                        : existingFlexHistory;

                // Append the previous lastFloat to lastFloatHistory if it exists
                String updatedFloatHistory = (existingLastFloat != null)
                        ? (existingFloatHistory == null ? String.valueOf(existingLastFloat.getTime()) : existingFloatHistory + "," + existingLastFloat.getTime())
                        : existingFloatHistory;

                // Execute the update with new values
                try (PreparedStatement updateStmt = dbConnection.getConnection().prepareStatement(updateSql)) {
                    updateStmt.setString(1, newPhoneNumber);
                    updateStmt.setTimestamp(2, newFlexTimestamp != null ? new Timestamp(newFlexTimestamp) : null);
                    updateStmt.setTimestamp(3, newFloatTimestamp != null ? new Timestamp(newFloatTimestamp) : null);
                    updateStmt.setString(4, updatedFlexHistory);
                    updateStmt.setString(5, updatedFloatHistory);
                    updateStmt.setString(6, teamID);

                    return updateStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error updating staff: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all staff members from the database for display or processing.
     *
     * @return a list of StaffMember objects containing details of all staff members.
     */
    public List<StaffMember> getAllStaffMembers() {
        List<StaffMember> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                if (name == null || !name.contains(" ")) {
                    System.err.println("Invalid name format: " + name);
                    continue;
                }

                Date startDate = rs.getDate("startDate");
                Timestamp lastFlexTimestamp = rs.getTimestamp("lastFlex");
                Timestamp lastFloatTimestamp = rs.getTimestamp("lastFloat");

                StaffMember staff = new StaffMember(
                        name,
                        rs.getString("teamID"),
                        startDate,
                        rs.getString("phoneNumber")
                );

                // Convert Timestamp to LocalDateTime if not null
                if (lastFlexTimestamp != null) {
                    staff.addFlexDate(convertToLocalDateTime(lastFlexTimestamp));
                }

                if (lastFloatTimestamp != null) {
                    staff.addFloatDate(convertToLocalDateTime(lastFloatTimestamp));
                }

                staffList.add(staff);
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving all staff members: " + ex.getMessage());
        }
        return staffList;
    }

    /**
     * Retrieves a staff member by their unique TeamID.
     *
     * @param teamID the TeamID of the staff member to be retrieved.
     * @return a StaffMember object containing the staff member's details, or null if not found.
     */
    public StaffMember getStaffByIdentifier(String teamID) {
        String sql = "SELECT * FROM Staff WHERE teamID = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, teamID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                StaffMember staff = new StaffMember(
                        rs.getString("name"),
                        rs.getString("teamID"),
                        rs.getDate("startDate"),
                        rs.getString("phoneNumber")
                );
                if (rs.getTimestamp("lastFlex") != null) {
                    staff.addFlexDate(convertToLocalDateTime(rs.getTimestamp("lastFlex")));
                }
                if (rs.getTimestamp("lastFloat") != null) {
                    staff.addFloatDate(convertToLocalDateTime(rs.getTimestamp("lastFloat")));
                }
                return staff;
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving staff by identifier: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Generates a report of staff who floated in a specific month and year.
     *
     * @param month the month for the report (1-12).
     * @param year the year for the report.
     * @return a list of StaffMember objects who floated during the specified month.
     */
    public List<StaffMember> generateMonthlyFloatReport(int month, int year) {
        List<StaffMember> floatedStaff = new ArrayList<>();
        String sql = "SELECT * FROM Staff WHERE strftime('%m', datetime(lastFloat / 1000, 'unixepoch')) = ? AND strftime('%Y', datetime(lastFloat / 1000, 'unixepoch')) = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            // Format month and year as strings
            pstmt.setString(1, String.format("%02d", month)); // e.g., "08" for August
            pstmt.setString(2, String.valueOf(year));          // e.g., "2024"

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StaffMember staff = new StaffMember(
                        rs.getString("name"),
                        rs.getString("teamID"),
                        rs.getDate("startDate"),
                        rs.getString("phoneNumber")
                );
                if (rs.getTimestamp("lastFloat") != null) {
                    staff.addFloatDate(rs.getTimestamp("lastFloat").toLocalDateTime());
                }
                floatedStaff.add(staff);
            }
        } catch (SQLException ex) {
            System.err.println("Error generating monthly float report: " + ex.getMessage());
        }
        return floatedStaff;
    }

    /**
     * Generates a report of staff who flexed or floated during a specific year.
     *
     * @param year the year for the report.
     * @return a list of StaffMember objects who flexed or floated during the specified year.
     */
    public List<StaffMember> generateYearlyFlexFloatReport(int year) {
        List<StaffMember> yearlyStaff = new ArrayList<>();
        String sql = "SELECT * FROM Staff WHERE strftime('%Y', datetime(lastFlex / 1000, 'unixepoch')) = ? OR strftime('%Y', datetime(lastFloat / 1000, 'unixepoch')) = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(year)); // e.g., "2024" for the year
            pstmt.setString(2, String.valueOf(year)); // Match for both lastFlex and lastFloat

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StaffMember staff = new StaffMember(
                        rs.getString("name"),
                        rs.getString("teamID"),
                        rs.getDate("startDate"),
                        rs.getString("phoneNumber")
                );
                if (rs.getTimestamp("lastFlex") != null) {
                    staff.addFlexDate(rs.getTimestamp("lastFlex").toLocalDateTime());
                }
                if (rs.getTimestamp("lastFloat") != null) {
                    staff.addFloatDate(rs.getTimestamp("lastFloat").toLocalDateTime());
                }
                yearlyStaff.add(staff);
            }
        } catch (SQLException ex) {
            System.err.println("Error generating yearly flex/float report: " + ex.getMessage());
        }
        return yearlyStaff;
    }
}