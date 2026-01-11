/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Alfie
 */
package alfie.util;

import alfie.model.AttendanceRecord;
import alfie.model.Employee;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR02-Feature 2
 * Purpose: Utility class to calculate salary-related values such as total hours worked.
 * 
 */

public class SalaryCalculator {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
    private final AttendanceFileHandler handler;

    public SalaryCalculator(AttendanceFileHandler handler) {
        this.handler = handler;
    }

    public double calculateMonthlyHours(String employeeNumber, String year, String monthTwoDigit) {
        double totalHours = 0.0;

        List<AttendanceRecord> records = handler.getRecordsForEmployee(employeeNumber, year, monthTwoDigit);

        for (AttendanceRecord record : records) {
            try {
                LocalTime logIn = LocalTime.parse(record.getLogIn(), TIME_FORMATTER);
                LocalTime logOut = LocalTime.parse(record.getLogOut(), TIME_FORMATTER);

                if (logOut.isBefore(logIn)) continue;

                Duration duration = Duration.between(logIn, logOut);
                totalHours += duration.toMinutes() / 60.0;
            } catch (DateTimeParseException e) {
                System.err.println("Invalid time format: " + e.getMessage());
            }
        }
        return totalHours;
    }

    public double calculateSalary(Employee emp, double totalHours) {
        return totalHours * emp.getMonthlyRate();
    }

    public double calculateTotalWithAllowances(Employee emp) {
        return emp.getBasicSalary()
                + emp.getRiceSubsidy()
                + emp.getPhoneAllowance()
                + emp.getClothingAllowance();
    }
}
