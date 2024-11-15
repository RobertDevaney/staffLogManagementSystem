/**
 * Robert Devaney
 * CEN-3024C-15339
 * November 15, 2024
 * MainForm.java
 * This class provides the GUI and main functionality for the Staff Log Management System.
 * It connects the system to the database, enables CRUD operations, and generates reports.
 */
package com.devaneystafflog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainForm {

    private StaffLogManager manager;
    private InputValidator validator;
    private JTextArea txtOutput;
    private JTabbedPane tabbedPaneMain;
    private JPanel panel1;
    private JPanel panelAddStaff;
    private JPanel panelRemoveStaff;
    private JPanel panelUpdateStaff;
    private JPanel panelViewAllStaff;
    private JPanel panelSearchStaff;
    private JPanel panelGenerateReports;
    private JTextField txtName;
    private JTextField txtTeamID;
    private JTextField txtStartDate;
    private JTextField txtLastFlex;
    private JTextField txtLastFloat;
    private JTextField txtPhoneNumber;
    private JButton btnAddStaff;
    private JTextField txtRemoveIdentifier;
    private JButton btnRemoveStaff;
    private JTextField txtUpdateIdentifier;
    private JTextField txtUpdatePhoneNumber;
    private JTextField txtUpdateLastFlex;
    private JTextField txtUpdateLastFloat;
    private JButton btnUpdateStaff;
    private JTable tableViewAllStaff;
    private JTextField txtSearchIdentifier;
    private JButton btnSearchStaff;
    private JTextArea txtSearchResult;
    private JComboBox<String> comboMonth;
    private JComboBox<String> comboYear;
    private JButton btnGenerateMonthlyReport;
    private JComboBox<String> comboYearOnly;
    private JButton btnGenerateYearlyReport;
    private JTextArea txtReportResult;
    private JLabel lblName;
    private JLabel lblTeamID;
    private JLabel lblStartDate;
    private JLabel lblLastFlex;
    private JLabel lblLastFloat;
    private JLabel lblPhoneNumber;
    private JLabel lblRemoveIdentifier;
    private JLabel lblUpdateIdentifier;
    private JLabel lblUpdatePhoneNumber;
    private JLabel lblUpdateLastFlex;
    private JLabel lblUpdateLastFloat;
    private JLabel lblSearchIdentifier;
    private JLabel lblMonth;
    private JLabel lblYear;
    private JLabel lblYearOnly;
    private JPanel panelMonthlyReport;
    private JPanel panelYearlyReport;
    private JButton btnLoadFromFile;
    private JTextField txtFilePath;
    private JTextField filePathTextField;
    private JButton connectButton;
    private DatabaseConnection dbConnection;

    /**
     * Constructor: MainForm
     * Initializes the GUI components and sets up action listeners for various operations.
     */
    public MainForm() {
        validator = new InputValidator(new Scanner(System.in));
        initializeComboBoxes();

        connectButton.addActionListener(e -> {
            String dbPath = filePathTextField.getText();
            dbConnection = new DatabaseConnection(dbPath);

            if (dbConnection.getConnection() != null) {
                manager = new StaffLogManager(dbConnection); // Initialize manager after connection
                JOptionPane.showMessageDialog(panel1, "Database connected successfully!");
                connectButton.setEnabled(false);

                // Only display staff after manager and connection are initialized
                displayAllStaff();
            } else {
                JOptionPane.showMessageDialog(panel1, "Failed to connect. Check the file path.");
            }
        });


        btnAddStaff.addActionListener(e -> addStaff());
        btnRemoveStaff.addActionListener(e -> removeStaff());
        btnUpdateStaff.addActionListener(e -> updateStaff());
        btnLoadFromFile.addActionListener(e -> loadFromFile());
        btnSearchStaff.addActionListener(e -> searchStaff());
        btnGenerateMonthlyReport.addActionListener(e -> generateMonthlyReport());
        btnGenerateYearlyReport.addActionListener(e -> generateYearlyReport());

        txtStartDate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateDate(txtStartDate, "MM-dd-yyyy", btnAddStaff); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateDate(txtStartDate, "MM-dd-yyyy", btnAddStaff); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateDate(txtStartDate, "MM-dd-yyyy", btnAddStaff); }
        });

        // displayAllStaff();
    }

    /**
     * Initializes combo boxes with appropriate values for months and years.
     */
    private void initializeComboBoxes() {
        if (comboMonth != null) {
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            for (String month : months) comboMonth.addItem(month);
        }
        if (comboYear != null) for (int year = 2000; year <= 2030; year++) comboYear.addItem(String.valueOf(year));
        if (comboYearOnly != null) for (int year = 2000; year <= 2030; year++) comboYearOnly.addItem(String.valueOf(year));
    }

    /**
     * Adds a new staff member to the database.
     */
    private void addStaff() {
        try {
            String name = txtName.getText().trim();
            String teamID = txtTeamID.getText().trim();
            String startDateStr = txtStartDate.getText().trim();
            String phoneNumber = txtPhoneNumber.getText().trim();

            // Parse and format the start date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            java.util.Date parsedDate = dateFormat.parse(startDateStr);
            java.sql.Date startDate = new java.sql.Date(parsedDate.getTime());

            // Parse LocalDateTime fields for lastFlex and lastFloat
            LocalDateTime lastFlex = null;
            LocalDateTime lastFloat = null;

            if (!txtLastFlex.getText().trim().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
                lastFlex = LocalDateTime.parse(txtLastFlex.getText().trim(), formatter);
            }

            if (!txtLastFloat.getText().trim().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
                lastFloat = LocalDateTime.parse(txtLastFloat.getText().trim(), formatter);
            }

            // Create the StaffMember and add it
            StaffMember staff = new StaffMember(name, teamID, startDate, phoneNumber);
            if (lastFlex != null) {
                staff.addFlexDate(lastFlex);
            }
            if (lastFloat != null) {
                staff.addFloatDate(lastFloat);
            }

            if (manager.isDuplicate(teamID)) {
                txtOutput.setText("Duplicate TeamID. This staff member already exists.");
            } else if (manager.addStaff(staff)) {
                txtOutput.setText("Staff member added successfully.");
                displayAllStaff();
            } else {
                txtOutput.setText("Error adding staff.");
            }
        } catch (ParseException | IllegalArgumentException ex) {
            txtOutput.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Removes a staff member from the database by TeamID.
     */
    private void removeStaff() {
        String teamID = txtRemoveIdentifier.getText().trim();
        if (manager.removeStaff(teamID)) {
            txtOutput.setText("Staff member removed successfully.");
        } else {
            txtOutput.setText("Staff member not found.");
        }
        displayAllStaff();
    }

    /**
     * Updates a staff member's details in the database.
     */
    private void updateStaff() {
        String teamID = txtUpdateIdentifier.getText().trim();
        String phoneNumber = txtUpdatePhoneNumber.getText().trim();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
            LocalDateTime lastFlex = null;
            LocalDateTime lastFloat = null;

            if (!txtUpdateLastFlex.getText().trim().isEmpty()) {
                lastFlex = LocalDateTime.parse(txtUpdateLastFlex.getText().trim(), formatter);
            }

            if (!txtUpdateLastFloat.getText().trim().isEmpty()) {
                lastFloat = LocalDateTime.parse(txtUpdateLastFloat.getText().trim(), formatter);
            }

            if (manager.updateStaff(teamID, phoneNumber, lastFlex, lastFloat)) {
                txtOutput.setText("Staff member updated successfully.");
                displayAllStaff();
            } else {
                txtOutput.setText("Staff member not found.");
            }
        } catch (Exception ex) {
            txtOutput.setText("Error updating staff: " + ex.getMessage());
        }
    }

    /**
     * Loads staff data from a file and updates the database.
     */
    private void loadFromFile() {
        String filePath = txtFilePath.getText().trim();
        FileIO fileIO = new FileIO();
        fileIO.loadFromFile(filePath, manager);
        txtOutput.setText("Data loaded from file: " + filePath);
        displayAllStaff();
    }

    /**
     * Searches for a staff member by their TeamID or identifier and displays the result.
     */
    private void searchStaff() {
        String identifier = txtSearchIdentifier.getText().trim();
        StaffMember staff = manager.getStaffByIdentifier(identifier);
        txtSearchResult.setText(staff != null ? staff.toString() : "No staff member found.");
    }

    /**
     * Generates a report of staff who floated during a specific month and year.
     * Displays the result in the report result text area.
     */
    private void generateMonthlyReport() {
        String month = (String) comboMonth.getSelectedItem();
        String year = (String) comboYear.getSelectedItem();

        if (month == null || year == null) {
            txtReportResult.setText("Please select both a month and a year.");
            return;
        }

        int selectedMonth = Month.valueOf(month.toUpperCase()).getValue();
        int selectedYear = Integer.parseInt(year);

        // Log selected values
        System.out.println("Generating report for Month: " + selectedMonth + ", Year: " + selectedYear);

        List<StaffMember> floatedStaff = manager.generateMonthlyFloatReport(selectedMonth, selectedYear);

        // Log if query execution happened and returned any data
        if (floatedStaff.isEmpty()) {
            System.out.println("No data found for the selected month and year.");
            txtReportResult.setText("No staff members floated in the selected month and year.");
        } else {
            System.out.println("Data retrieved successfully for monthly report.");
            StringBuilder report = new StringBuilder("Monthly Float Report:\n");
            floatedStaff.forEach(staff -> report.append(staff.getName()).append(" (").append(staff.getTeamID()).append(")\n"));
            txtReportResult.setText(report.toString());
        }
    }

    /**
     * Generates a report of staff who flexed or floated during a specific year.
     * Displays the result in the report result text area.
     */
    private void generateYearlyReport() {
        String year = (String) comboYearOnly.getSelectedItem();
        if (year == null) {
            txtReportResult.setText("Please select a year.");
            return;
        }

        int selectedYear = Integer.parseInt(year);
        System.out.println("Generating yearly report for year: " + selectedYear);

        List<StaffMember> yearlyStaff = manager.generateYearlyFlexFloatReport(selectedYear);

        // Log if query execution happened and returned any data
        if (yearlyStaff.isEmpty()) {
            System.out.println("No data found for the selected year.");
            txtReportResult.setText("No staff members had flexed or floated in the selected year.");
        } else {
            System.out.println("Data retrieved successfully for yearly report.");
            StringBuilder report = new StringBuilder("Yearly Flex/Float Report:\n");
            yearlyStaff.forEach(staff -> report.append(staff.getName()).append(" (").append(staff.getTeamID()).append(")\n"));
            txtReportResult.setText(report.toString());
        }
    }

    /**
     * Displays all staff members in the table view.
     * Populates the table with staff details such as name, teamID, start date, and more.
     */
    private void displayAllStaff() {
        List<StaffMember> staffList = manager.getAllStaffMembers();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("TeamID");
        model.addColumn("Start Date");
        model.addColumn("Phone Number");
        model.addColumn("Last Flex");
        model.addColumn("Last Float");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        for (StaffMember staff : staffList) {
            // Format Start Date
            String startDateFormatted = staff.getStartDate() != null ? dateFormat.format(staff.getStartDate()) : "";

            // Directly use Timestamp to format Last Flex and Last Float
            String lastFlexFormatted = staff.getLastFlex() != null ? staff.getLastFlex().toLocalDateTime().format(timestampFormatter) : "";
            String lastFloatFormatted = staff.getLastFloat() != null ? staff.getLastFloat().toLocalDateTime().format(timestampFormatter) : "";

            model.addRow(new Object[]{
                    staff.getName(),
                    staff.getTeamID(),
                    startDateFormatted,
                    staff.getPhoneNumber(),
                    lastFlexFormatted,
                    lastFloatFormatted
            });
        }

        tableViewAllStaff.setModel(model);
    }

    /**
     * Validates the date entered in a text field and enables or disables a button based on validity.
     *
     * @param textField    the JTextField containing the date input.
     * @param format       the required date format as a string.
     * @param actionButton the button to enable or disable based on validation.
     */
    private void validateDate(JTextField textField, String format, JButton actionButton) {
        String dateStr = textField.getText().trim();
        boolean isValid = dateStr.matches("\\d{2}-\\d{2}-\\d{4}"); // Ensures MM-dd-yyyy format
        textField.setBorder(BorderFactory.createLineBorder(isValid ? Color.GRAY : Color.RED));
        actionButton.setEnabled(isValid);
    }

    /**
     * Gets the main panel for the application.
     *
     * @return the JPanel containing the main GUI.
     */
    public JPanel getMainPanel() { return panel1; }

    /**
     * The main entry point of the application.
     * Sets up and displays the main GUI window.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Staff Log Management System");
            MainForm mainForm = new MainForm();
            frame.setContentPane(mainForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}