/**
 * Robert Devaney
 * CEN-3024C-15339
 * November 15, 2024
 * DatabaseConnection.java
 * This class manages the connection to the SQLite database used in the
 * Database Management System (DMS) project. It handles the initialization,
 * retrieval, and closure of the database connection.
 */
package com.devaneystafflog;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection connection;

    /**
     * Constructor: DatabaseConnection
     * Initializes the connection to the database specified by the provided file path.
     *
     * @param dbPath the file path to the SQLite database.
     */
    public DatabaseConnection(String dbPath) {
        File dbFile = new File(dbPath);

        if (!dbFile.exists()) {
            System.out.println("Database file does not exist.");
            connection = null; // Set connection to null if the file doesn't exist
        } else {
            try {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                System.out.println("Connected to the database successfully.");
            } catch (SQLException e) {
                System.out.println("Failed to connect to the database: " + e.getMessage());
            }
        }
    }

    /**
     * Method: getConnection
     * Retrieves the active database connection.
     *
     * @return the Connection object, or null if the connection failed.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Method: closeConnection
     * Closes the active database connection if it exists.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database connection: " + e.getMessage());
        }
    }

    /*
    // Uncomment and modify for testing purposes
    public static void main(String[] args) {
        // Replace "your-database-file-path.db" with the actual path to your SQLite database file.
        String dbPath = "C:\\sqlite\\staff_log.db";

        // Create an instance of DatabaseConnection with the specified path
        DatabaseConnection dbConnection = new DatabaseConnection(dbPath);

        // Check if the connection was successful
        if (dbConnection.getConnection() != null) {
            System.out.println("Database connection test successful.");
        } else {
            System.out.println("Database connection test failed.");
        }

        // Close the connection after testing
        dbConnection.closeConnection();
    }
    */
}
