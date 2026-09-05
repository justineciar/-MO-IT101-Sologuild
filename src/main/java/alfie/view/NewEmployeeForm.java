/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package alfie.view;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR02-Feature 2
 * Purpose:
 *  1.  Interface to add new employee using JDialog.
 *  2.  Added button to add new employee to CSV file.
 *  3.  Note: This class also handles the automatic generation of employee number.
 * 
 */

import alfie.model.Employee;
import alfie.util.EmployeeFileHandler;
import alfie.util.FilePathManager;
import alfie.util.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class NewEmployeeForm extends JDialog {

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField birthDateField;
    private final JTextField addressField;
    private final JTextField phoneNumberField;
    private final JTextField sssNumberField;
    private final JTextField philHealthField;
    private final JTextField tinNumberField;
    private final JTextField pagIbigNumberField;
    private final JTextField statusField;
    private final JTextField positionField;
    private final JTextField basicSalaryField;
    private final JTextField riceSubsidyField;
    private final JTextField phoneAllowanceField;
    private final JTextField clothingAllowanceField;
    private final JTextField semiMonthlyField;
    private final JTextField hourlyRateField;

    private final Map<String, JTextField> requiredFields = new LinkedHashMap<>();

    private final EmployeeFileHandler handler;
    private final Runnable refreshCallback;

    public NewEmployeeForm(JFrame parent, EmployeeFileHandler handler, Runnable refreshCallback) {
        super(parent, "Add New Employee", true);
        this.handler = handler;
        this.refreshCallback = refreshCallback;

        setLayout(new BorderLayout(10, 10));
        setSize(820, 620);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        positionField = new JTextField();
        birthDateField = new JTextField();
        addressField = new JTextField();
        phoneNumberField = new JTextField();
        philHealthField = new JTextField();
        tinNumberField = new JTextField();
        pagIbigNumberField = new JTextField();
        sssNumberField = new JTextField();
        statusField = new JTextField();
        basicSalaryField = new JTextField();
        riceSubsidyField = new JTextField();
        phoneAllowanceField = new JTextField();
        clothingAllowanceField = new JTextField();
        semiMonthlyField = new JTextField();
        hourlyRateField = new JTextField();

        // Register fields as required
        requiredFields.put("First Name", firstNameField);
        requiredFields.put("Last Name", lastNameField);
        requiredFields.put("Birth Date", birthDateField);
        requiredFields.put("Address", addressField);
        requiredFields.put("Phone Number", phoneNumberField);
        requiredFields.put("SSS Number", sssNumberField);
        requiredFields.put("PhilHealth Number", philHealthField);
        requiredFields.put("TIN Number", tinNumberField);
        requiredFields.put("Pag-Ibig Number", pagIbigNumberField);
        requiredFields.put("Status", statusField);
        requiredFields.put("Position", positionField);
        requiredFields.put("Basic Salary", basicSalaryField);
        requiredFields.put("Rice Subsidy", riceSubsidyField);
        requiredFields.put("Phone Allowance", phoneAllowanceField);
        requiredFields.put("Clothing Allowance", clothingAllowanceField);
        requiredFields.put("Gross Semi-Monthly Rate", semiMonthlyField);
        requiredFields.put("Hourly Rate", hourlyRateField);

        form.add(createRequiredLabel("First Name :")); form.add(firstNameField);
        form.add(createRequiredLabel("Last Name :")); form.add(lastNameField);
        form.add(createRequiredLabel("Birth Date : (MM/DD/YYYY)")); form.add(birthDateField);
        form.add(createRequiredLabel("Full Address :")); form.add(addressField);
        form.add(createRequiredLabel("Phone Number :")); form.add(phoneNumberField);
        form.add(createRequiredLabel("SSS Number :")); form.add(sssNumberField);
        form.add(createRequiredLabel("PhilHealth Number :")); form.add(philHealthField);
        form.add(createRequiredLabel("TIN Number :")); form.add(tinNumberField);
        form.add(createRequiredLabel("Pag-Ibig Number :")); form.add(pagIbigNumberField);
        form.add(createRequiredLabel("Status :")); form.add(statusField);
        form.add(createRequiredLabel("Position :")); form.add(positionField);
        form.add(createRequiredLabel("Basic Salary :")); form.add(basicSalaryField);
        form.add(createRequiredLabel("Rice Subsidy :")); form.add(riceSubsidyField);
        form.add(createRequiredLabel("Phone Allowance :")); form.add(phoneAllowanceField);
        form.add(createRequiredLabel("Clothing Allowance :")); form.add(clothingAllowanceField);
        form.add(createRequiredLabel("Gross Semi-Monthly Rate :")); form.add(semiMonthlyField);
        form.add(createRequiredLabel("Hourly Rate :")); form.add(hourlyRateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::handleSave);

        add(form, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void handleSave(ActionEvent e) {
        resetFieldBackgrounds();

        if (!InputValidator.validateRequiredFields(this, requiredFields.values().toArray(new JTextField[0]))) {
            return;
        }

        try {
            Employee newEmp = new Employee();
            newEmp.setEmployeeNumber(generateEmployeeNumber());
            newEmp.setFirstName(firstNameField.getText());
            newEmp.setLastName(lastNameField.getText());
            newEmp.setAddress(addressField.getText());
            newEmp.setPhoneNumber(phoneNumberField.getText());
            newEmp.setPhilHealthNumber(philHealthField.getText());
            newEmp.setTin(tinNumberField.getText());
            newEmp.setPagIbigNumber(pagIbigNumberField.getText());
            newEmp.setSssNumber(sssNumberField.getText());
            newEmp.setStatus(statusField.getText());
            newEmp.setPosition(positionField.getText());
            newEmp.setBasicSalary(parseFieldAsDouble(basicSalaryField, "Basic Salary"));
            newEmp.setRiceSubsidy(parseFieldAsDouble(riceSubsidyField, "Rice Subsidy"));
            newEmp.setPhoneAllowance(parseFieldAsDouble(phoneAllowanceField, "Phone Allowance"));
            newEmp.setClothingAllowance(parseFieldAsDouble(clothingAllowanceField, "Clothing Allowance"));
            newEmp.setGrossRate(parseFieldAsDouble(semiMonthlyField, "Semi-Monthly"));
            newEmp.setMonthlyRate(parseFieldAsDouble(hourlyRateField, "Hourly Rate"));

            boolean success = handler.saveEmployee(newEmp);
            if (success) {
                JOptionPane.showMessageDialog(this, "Employee saved successfully!");
                refreshCallback.run();
                dispose();
            } else {
                //Debbuging console message purpose only. Remove after dubbuging.
                JOptionPane.showMessageDialog(this, "Failed to save employee.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            //Debbuging console message purpose purpose only. Remove after dubbuging.
            JOptionPane.showMessageDialog(this, "Some numeric fields are invalid. Please check highlighted fields.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateEmployeeNumber() {
        String path = FilePathManager.getInstance().getEmployeeFilePath();
        int max = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    try {
                        int num = Integer.parseInt(parts[0].trim());
                        max = Math.max(max, num);
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee file: " + e.getMessage());
        }

        return String.format("%03d", max + 1);
    }

    private double parseFieldAsDouble(JTextField field, String fieldName) {
        String value = field.getText().trim();
        try {
            field.setBackground(Color.WHITE);
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            field.setBackground(new Color(255, 102, 102));
            JOptionPane.showMessageDialog(
                this,
                fieldName + " must be a valid number.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
            field.requestFocus();
            throw e;
        }
    }

    private void resetFieldBackgrounds() {
        for (JTextField field : requiredFields.values()) {
            field.setBackground(Color.WHITE);
        }
    }

    private JLabel createRequiredLabel(String labelText) {
        return new JLabel("<html>" + labelText + " <font color='red'>*</font></html>");
    }
}
