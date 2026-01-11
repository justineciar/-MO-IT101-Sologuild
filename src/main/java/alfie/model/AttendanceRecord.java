/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package alfie.model;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR02-Feature 2
 * Purpose: Model class for constructor and getter to use in:
 *      1.  AttendanceFileHnadler class
 *      2.  SalaryCalculator class
 * 
 */

public class AttendanceRecord { // Public class. Used as a data model to represent a single employee's attendace log.
                                // Model: One row in attendance CSV file.
    
    private final String employeeNumber;    // Purpose:     Private fields for the class
    private final String lastName;          // Explanation:
    private final String firstName;         //      private     : means they can only be accessed within the class.
    private final String date;              //                  (ensure encapsulation, for OOP principle).
    private final String logIn;             //      final       : these values are immutable after the object is created.
    private final String logOut;

// Constructor to initialized every instance of attendance record
    public AttendanceRecord(String employeeNumber, String lastName, String firstName,
                            String date, String logIn, String logOut) {
        this.employeeNumber = employeeNumber;   // Purpose: This is a constructor. It allows to create an attendanceRecord
        this.lastName = lastName;               //          object and immediatly initialize all its values.
        this.firstName = firstName;             // Explanation:
        this.date = date;                       //      1. this.employeeNumber = employee
        this.logIn = logIn;                     // to be continue
        this.logOut = logOut;
    }

// Getters method from Employee.java(Class)
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getDate() { return date; }
    public String getLogIn() { return logIn; }
    public String getLogOut() { return logOut; }
}

