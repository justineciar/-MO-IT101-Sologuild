/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.util;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR02-Feature 2
 * Purpose: Utility to handle employee details, read and save.
 * 
 */

import alfie.model.Employee;
import java.io.*;
import java.util.*;

public class EmployeeFileHandler {

    private final String employeeFilePath;

    public EmployeeFileHandler() {
        this.employeeFilePath = FilePathManager.getInstance().getEmployeeFilePath();
    }

    public List<Employee> readEmployees() {
        List<Employee> employeeList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(employeeFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 19) {
                    Employee emp = new Employee(
                        parts[0], parts[1], parts[2], parts[3],
                        parts[4], parts[5], parts[6], parts[7], parts[8], parts[9],
                        parts[10], parts[11], parts[12], parts[13], parts[14],
                        parts[15], parts[16], parts[17], parts[18]
                    );
                    employeeList.add(emp);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading employee file: " + e.getMessage());
        }

        return employeeList;
    }
    
    public boolean saveEmployee(Employee emp) {
        String filePath = FilePathManager.getInstance().getEmployeeFilePath();

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(String.join(",",
                emp.getEmployeeNumber(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getBirthDay(),
                emp.getAddress(),
                emp.getPhoneNumber(),
                emp.getSssNumber(),
                emp.getPhilHealthNumber(),
                emp.getTin(),
                emp.getPagIbigNumber(),
                emp.getStatus(),
                emp.getPosition(),
                emp.getImmediateSupervision(),
                String.format("%.2f", emp.getBasicSalary()),
                String.format("%.2f", emp.getRiceSubsidy()),
                String.format("%.2f", emp.getPhoneAllowance()),
                String.format("%.2f", emp.getClothingAllowance()),
                String.format("%.2f", emp.getGrossRate()),
                String.format("%.2f", emp.getMonthlyRate())
            ));
            writer.write("\n");
            return true;

        } catch (IOException e) {
            System.err.println("Error saving employee: " + e.getMessage());
            return false;
        }
    }
    public boolean updateEmployee(Employee updatedEmp) {
        List<Employee> employees = readEmployees();
        boolean found = false;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeNumber().equals(updatedEmp.getEmployeeNumber())) {
                employees.set(i, updatedEmp);  // Replace with updated employee
                found = true;
                break;
            }
        }

        if (!found) {
            return false; // Employee not found
        }

    // Rewrite the CSV file with updated list
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(employeeFilePath))) {
            writer.write("Employee Number,Last Name,First Name,Birth Date,Address,Phone Number,SSS Number,PhilHealth Number,TIN,Pag-IBIG Number,Status,Position,Immediate Supervision,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Rate,Hourly Rate");
            writer.newLine();

            for (Employee emp : employees) {
                writer.write(String.join(",",
                    emp.getEmployeeNumber(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getBirthDay(),
                    emp.getAddress(),
                    emp.getPhoneNumber(),
                    emp.getSssNumber(),
                    emp.getPhilHealthNumber(),
                    emp.getTin(),
                    emp.getPagIbigNumber(),
                    emp.getStatus(),
                    emp.getPosition(),
                    emp.getImmediateSupervision(),
                    String.valueOf(emp.getBasicSalary()),
                    String.valueOf(emp.getRiceSubsidy()),
                    String.valueOf(emp.getPhoneAllowance()),
                    String.valueOf(emp.getClothingAllowance()),
                    String.valueOf(emp.getGrossRate()),
                    String.valueOf(emp.getMonthlyRate())
                ));
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error updating employee file: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteEmployeeByNumber(String empNumber) {
        File inputFile = new File(FilePathManager.getInstance().getEmployeeFilePath());
        File tempFile = new File("temp_employees.csv");

        boolean deleted = false;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String header = reader.readLine();
            if (header != null) {
                writer.write(header);
                writer.newLine();
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length > 0 && parts[0].trim().equals(empNumber)) {
                    deleted = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            return false;
        }

        // Replace original file with temp
        if (deleted && inputFile.delete()) {
            return tempFile.renameTo(inputFile);
        } else {
            tempFile.delete(); // cleanup
            return false;
        }
    }


    public boolean saveAllEmployees(List<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\Alfie\\Documents\\NetBeansProjects\\MotorPHCP2\\MotorPH Employee Details.csv"))) {
            for (Employee emp : employees) {
                writer.println(emp.toCSV());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
