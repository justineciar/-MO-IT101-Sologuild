/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.view;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR03-Feature 3
 * Purpose:
 *  1.  User interface to update/edit employee details.
 *  2.  Added save button to update/edit employee interface.
 * 
 */

import alfie.model.Employee;
import alfie.util.EmployeeFileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditEmployeeForm extends JDialog {

    private final Employee employee;
    private final EmployeeFileHandler handler;
    private final Runnable onSaveCallback;

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField positionField = new JTextField();
    private final JTextField basicSalaryField = new JTextField();
    private final JTextField riceSubsidyField = new JTextField();
    private final JTextField phoneAllowanceField = new JTextField();
    private final JTextField clothingAllowanceField = new JTextField();
    private final JTextField hourlyRateField = new JTextField();

    public EditEmployeeForm(JFrame parent, Employee emp, EmployeeFileHandler handler, Runnable onSaveCallback) {
        super(parent, "Edit Employee", true);
        this.employee = emp;
        this.handler = handler;
        this.onSaveCallback = onSaveCallback;

        setLayout(new BorderLayout(10, 10));
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        add(form, BorderLayout.CENTER);

        // Prefill data
        firstNameField.setText(emp.getFirstName());
        lastNameField.setText(emp.getLastName());
        positionField.setText(emp.getPosition());
        basicSalaryField.setText(emp.getBasicSalary().toString());
        riceSubsidyField.setText(emp.getRiceSubsidy().toString());
        phoneAllowanceField.setText(emp.getPhoneAllowance().toString());
        clothingAllowanceField.setText(emp.getClothingAllowance().toString());
        hourlyRateField.setText(emp.getMonthlyRate().toString());

        form.add(new JLabel("First Name:")); form.add(firstNameField);
        form.add(new JLabel("Last Name:")); form.add(lastNameField);
        form.add(new JLabel("Position:")); form.add(positionField);
        form.add(new JLabel("Basic Salary:")); form.add(basicSalaryField);
        form.add(new JLabel("Rice Subsidy:")); form.add(riceSubsidyField);
        form.add(new JLabel("Phone Allowance:")); form.add(phoneAllowanceField);
        form.add(new JLabel("Clothing Allowance:")); form.add(clothingAllowanceField);
        form.add(new JLabel("Hourly Rate:")); form.add(hourlyRateField);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(this::handleSave);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void handleSave(ActionEvent e) {
        try {
            // Create a new Employee instance with updated values
            Employee updated = new Employee(
                employee.getEmployeeNumber(),
                lastNameField.getText(),
                firstNameField.getText(),
                employee.getBirthDay(),
                employee.getAddress(),
                employee.getPhoneNumber(),
                employee.getSssNumber(),
                employee.getPhilHealthNumber(),
                employee.getTin(),
                employee.getPagIbigNumber(),
                employee.getStatus(),
                positionField.getText(),
                employee.getImmediateSupervision(),
                basicSalaryField.getText(),
                riceSubsidyField.getText(),
                phoneAllowanceField.getText(),
                clothingAllowanceField.getText(),
                employee.getGrossRate().toString(),
                hourlyRateField.getText()
            );

            handler.updateEmployee(updated);  // Overwrite old record
            onSaveCallback.run();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number: " + ex.getMessage());
        }
    }
}