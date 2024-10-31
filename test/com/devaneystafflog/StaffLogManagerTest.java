/**
 * Robert Devaney
 * CEN-3024C-15339
 * October 20, 2024
 * StaffLogManagerTest.java *
 * This test class is responsible for validating the functionality of the Staff Log Management System.
 * It ensures that key operations such as adding, removing, updating staff members are functioning correctly.
 * Along with verifying that custom actions like generating reports work as expected.
 */

package com.devaneystafflog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class StaffLogManagerTest {

    private StaffLogManager manager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out; // Save original System.out

    @BeforeEach
    void setUp() {
        manager = new StaffLogManager();
        System.setOut(new PrintStream(outContent)); // Redirect System.out for all tests
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut); // Restore System.out after each test
    }

    /**@Test
    void testAddStaff() {
        StaffMember staff = new StaffMember("Luke Skywalker", "123456", new Date(), "123-456-7890");
        manager.addStaff(staff);
        assertEquals(1, manager.staffList.size(), "Staff member was not added correctly.");
    }*/

    /**@Test
    void testRemoveStaff() {
        StaffMember staff = new StaffMember("Luke Skywalker", "654321", new Date(), "987-654-3210");
        manager.addStaff(staff);

        // Simulating user input for Scanner (TeamID removal)
        String simulatedInput = "654321";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(in);

        // Removing by TeamID
        manager.removeStaff("654321", scanner);
        assertEquals(0, manager.staffList.size(), "Staff member was not removed correctly by TeamID.");

        // Adding again to test removal by Name
        manager.addStaff(staff);
        in = new ByteArrayInputStream("Luke Skywalker".getBytes()); // Simulate new input for name
        scanner = new Scanner(in);
        manager.removeStaff("Luke Skywalker", scanner);
        assertEquals(0, manager.staffList.size(), "Staff member was not removed correctly by Name.");
    }*/

      @Test
    void testGenerateMonthlyFloatReport() {
        StaffLogManager manager = new StaffLogManager();
        StaffMember staff = new StaffMember("Alice Fall", "654321", new Date(), "987-654-3210");
        LocalDateTime floatDate = LocalDateTime.of(2024, 10, 15, 10, 0);
        staff.addFloatDate(floatDate);
        manager.addStaff(staff);

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Generate the report
        manager.generateMonthlyFloatReport(10, 2024);

        // Capture and assert the output
        String output = outContent.toString();
        assertTrue(output.contains("Number of staff who floated in 10/2024: 1"), "Float report did not generate correctly.");

        // Print the captured output to the console
        System.out.println(output);
    }

    @Test
    void testGenerateYearlyFlexReport() {
        StaffLogManager manager = new StaffLogManager();
        StaffMember staff1 = new StaffMember("Bob Builder", "123456", new Date(), "123-456-7890");
        StaffMember staff2 = new StaffMember("Alice Wonderland", "654321", new Date(), "987-654-3210");

        // Add flex dates for the staff members
        LocalDateTime flexDate1 = LocalDateTime.of(2024, 5, 10, 10, 0);
        LocalDateTime flexDate2 = LocalDateTime.of(2024, 6, 12, 10, 0);
        staff1.addFlexDate(flexDate1);
        staff2.addFlexDate(flexDate2);

        manager.addStaff(staff1);
        manager.addStaff(staff2);

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Generate the yearly flex report
        manager.generateYearlyFlexFloatReport(2024);

        // Capture and assert the output
        String output = outContent.toString();
        assertTrue(output.contains("Number of staff who flexed in year 2024: 2"), "Flex report did not generate correctly.");

        // Print the captured output to the console
        System.out.println(output);
    }


}
