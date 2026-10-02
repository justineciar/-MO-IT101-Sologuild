package com.mycompany.motorphemployeeapp;

public class UserAuthentication {

    private EmployeeDataManager manager;

    // Constructor
    public UserAuthentication(
            EmployeeDataManager manager) {

        this.manager = manager;
    }

    // Validate Login
    public Employee validateLogin(
            String employeeNumber,
            String password) {

        // Find employee using employee number
        Employee employee =
                manager.findEmployeeByNumber(
                employeeNumber
                );

        // Check if employee exists
        if (employee == null) {

            return null;
        }

        // Validate password
        if (employee.getPassword()
                .equals(password)) {

            return employee;
        }

        return null;
    }
}
