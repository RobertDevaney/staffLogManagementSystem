package com.devaneystafflog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainForm {

    private StaffLogManager manager;           // Added as an instance variable
    private InputValidator validator;          // Added as an instance variable
    private JTextArea txtOutput;               // Added as an instance variable for output messages
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

    public MainForm() {
        // Instantiate manager and validator
        manager = new StaffLogManager();         // Initialize the StaffLogManager instance
        validator = new InputValidator(new Scanner(System.in));  // Initialize the InputValidator instance


        // Initialize month and year combo boxes
        initializeComboBoxes();
        // Set up action listeners for GUI buttons
        btnAddStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaff();
            }
        });

        btnRemoveStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStaff();
            }
        });

        btnUpdateStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStaff();
            }
        });

        btnLoadFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });

        btnSearchStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStaff();
            }
        });
        btnGenerateMonthlyReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMonthlyReport();
            }
        });

        btnGenerateYearlyReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateYearlyReport();
            }
        });
        // Set up real-time validation for date input
        txtStartDate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateDate(txtStartDate, "MM-dd-yyyy", btnAddStaff);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateDate(txtStartDate, "MM-dd-yyyy", btnAddStaff);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateDate(txtStartDate, "MM-dd-yyyy", btnAddStaff);
            }
        });

        // Display all staff members in table on launch
        displayAllStaff();
    }
    private void initializeComboBoxes() {
        // Populate months in comboMonth
        if (comboMonth != null) {
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            for (String month : months) {
                comboMonth.addItem(month);
            }
        }

        // Populate years in comboYear and comboYearOnly
        if (comboYear != null) {
            for (int year = 2000; year <= 2030; year++) {
                comboYear.addItem(String.valueOf(year));
            }
        }
        if (comboYearOnly != null) {
            for (int year = 2000; year <= 2030; year++) {
                comboYearOnly.addItem(String.valueOf(year));
            }
        }
    }
    private void addStaff() {
        try {
            String name = txtName.getText().trim();
            String teamID = txtTeamID.getText().trim();
            String startDateStr = txtStartDate.getText().trim();
            String lastFlexStr = txtLastFlex.getText().trim();
            String lastFloatStr = txtLastFloat.getText().trim();
            String phoneNumber = txtPhoneNumber.getText().trim();

            // Validate the start date format before parsing it
            if (!isDateValid(startDateStr)) {
                txtOutput.setText("Error: Start Date must be in MM-dd-yyyy format.");
                return;
            }

            Date startDate = validator.validateDate(startDateStr);
            LocalDateTime lastFlex = validator.validateDateTime(lastFlexStr);
            LocalDateTime lastFloat = validator.validateDateTime(lastFloatStr);

            if (!manager.isDuplicate(teamID)) {
                StaffMember staff = new StaffMember(name, teamID, startDate, phoneNumber);
                staff.addFlexDate(lastFlex);
                staff.addFloatDate(lastFloat);
                manager.addStaff(staff);
                txtOutput.setText("Staff member added successfully.");
                displayAllStaff();
            } else {
                txtOutput.setText("Duplicate TeamID. This staff member already exists.");
            }
        } catch (ParseException | IllegalArgumentException ex) {
            txtOutput.setText("Error adding staff: " + ex.getMessage());
        }
    }

    private void removeStaff() {
        String identifier = txtRemoveIdentifier.getText().trim();
        StaffMember staffToRemove = manager.getStaffByIdentifier(identifier);
        if (staffToRemove != null) {
            manager.removeStaff(identifier, validator.getScanner());
            txtOutput.setText("Staff member removed successfully.");
        } else {
            txtOutput.setText("Staff member not found.");
        }
        displayAllStaff();
    }



    private void updateStaff() {
        try {
            String identifier = txtUpdateIdentifier.getText().trim();
            String phoneNumber = txtUpdatePhoneNumber.getText().trim();
            String lastFlexStr = txtUpdateLastFlex.getText().trim();
            String lastFloatStr = txtUpdateLastFloat.getText().trim();

            // Validate phone number format
            if (!isPhoneNumberValid(phoneNumber)) {
                txtOutput.setText("Error: Phone Number must be in XXX-XXX-XXXX format.");
                return;
            }

            // Validate date and time formats for Last Flex and Last Float
            if (!isDateTimeValid(lastFlexStr) || !isDateTimeValid(lastFloatStr)) {
                txtOutput.setText("Error: Date and Time must be in MM-dd-yyyy HH format.");
                return;
            }

            LocalDateTime lastFlex = validator.validateDateTime(lastFlexStr);
            LocalDateTime lastFloat = validator.validateDateTime(lastFloatStr);

            StaffMember staffToUpdate = manager.getStaffByIdentifier(identifier);
            if (staffToUpdate != null) {
                manager.updateStaff(identifier, phoneNumber, lastFlex, lastFloat, validator.getScanner());
                txtOutput.setText("Staff member updated successfully.");
                displayAllStaff();
            } else {
                txtOutput.setText("Staff member not found.");
            }
        } catch (IllegalArgumentException ex) {
            txtOutput.setText("Error updating staff: " + ex.getMessage());
        }
    }

    private void loadFromFile() {
        String filePath = txtFilePath.getText().trim();
        FileIO fileIO = new FileIO();
        fileIO.loadFromFile(filePath, manager);
        txtOutput.setText("Data loaded from file: " + filePath);
        displayAllStaff();
    }

    private void searchStaff() {
        String identifier = txtSearchIdentifier.getText().trim();
        StaffMember staff = manager.getStaffByIdentifier(identifier);
        if (staff != null) {
            txtSearchResult.setText(staff.toString());
        } else {
            txtSearchResult.setText("No staff member found with the identifier: " + identifier);
        }
    }
    private void generateMonthlyReport() {
        String month = (String) comboMonth.getSelectedItem();
        String year = (String) comboYear.getSelectedItem();
        if (month == null || year == null) {
            txtReportResult.setText("Please select both a month and a year to generate the report.");
            return;
        }

        // Parse the month into its numerical value
        int monthValue = Month.valueOf(month.toUpperCase()).getValue();
        int yearValue = Integer.parseInt(year);

        List<StaffMember> floatedStaff = new ArrayList<>();
        for (StaffMember staff : manager.getAllStaffMembers()) {
            for (LocalDateTime floatDate : staff.getFloatDates()) {
                if (floatDate.getMonthValue() == monthValue && floatDate.getYear() == yearValue) {
                    floatedStaff.add(staff);
                    break;
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("Number of staff who floated in " + month + " " + year + ": " + floatedStaff.size() + "\n");
        report.append("Staff members who floated:\n");
        for (StaffMember staff : floatedStaff) {
            report.append(" - " + staff.getName() + " (TeamID: " + staff.getTeamID() + ")\n");
        }
        txtReportResult.setText(report.toString());
    }


    private void generateYearlyReport() {
        String year = (String) comboYearOnly.getSelectedItem();
        if (year == null) {
            txtReportResult.setText("Please select a year to generate the report.");
            return;
        }

        int yearValue = Integer.parseInt(year);
        List<StaffMember> flexedStaff = new ArrayList<>();
        List<StaffMember> floatedStaff = new ArrayList<>();

        for (StaffMember staff : manager.getAllStaffMembers()) {
            for (LocalDateTime flexDate : staff.getFlexDates()) {
                if (flexDate.getYear() == yearValue) {
                    flexedStaff.add(staff);
                    break;
                }
            }
            for (LocalDateTime floatDate : staff.getFloatDates()) {
                if (floatDate.getYear() == yearValue) {
                    floatedStaff.add(staff);
                    break;
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("Number of staff who flexed in year " + year + ": " + flexedStaff.size() + "\n");
        report.append("Staff members who flexed:\n");
        for (StaffMember staff : flexedStaff) {
            report.append(" - " + staff.getName() + " (TeamID: " + staff.getTeamID() + ")\n");
        }
        report.append("\nNumber of staff who floated in year " + year + ": " + floatedStaff.size() + "\n");
        report.append("Staff members who floated:\n");
        for (StaffMember staff : floatedStaff) {
            report.append(" - " + staff.getName() + " (TeamID: " + staff.getTeamID() + ")\n");
        }
        txtReportResult.setText(report.toString());
    }

    private void displayAllStaff() {
        List<StaffMember> staffList = manager.getAllStaffMembers();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("TeamID");
        model.addColumn("Start Date");
        model.addColumn("Phone Number");
        model.addColumn("Last Flex");
        model.addColumn("Last Float");

        for (StaffMember staff : staffList) {
            model.addRow(new Object[]{
                    staff.getName(),
                    staff.getTeamID(),
                    staff.getStartDate(),
                    staff.getPhoneNumber(),
                    staff.getLastFlex(),
                    staff.getLastFloat()
            });
        }
        tableViewAllStaff.setModel(model);
    }

    private boolean isDateValid(String dateStr) {
        return dateStr.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}");
    }

    private boolean isDateTimeValid(String dateTimeStr) {
        return dateTimeStr.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}");
    }



    private void validateDate(JTextField textField, String format, JButton actionButton) {
        if (!isDateValid(textField.getText().trim())) {
            textField.setBorder(BorderFactory.createLineBorder(Color.RED));
            actionButton.setEnabled(false);
        } else {
            textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            actionButton.setEnabled(true);
        }
    }

    public JPanel getMainPanel() {
        return panel1; // Make sure panel1 is your main container
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Staff Log Management System");
            MainForm mainForm = new MainForm();
            frame.setContentPane(mainForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(800, 600); // Set preferred size
            frame.setVisible(true);
        });
    }

}
