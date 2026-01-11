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
 *  1.  User interface to view a detailed employee details.
 *  2.  Added button to view the monthly salary of employee using JDialog.
 *  3.  JOptionPane prompt to select the month.
 *  4.  After selecting the month view the monthly salary of employee
 *          via SalaryReportView class using JDialog.
 * 
 */

import alfie.model.Employee;
import alfie.util.AttendanceFileHandler;
import javax.swing.*;
import java.awt.*;

public class EmployeeDetailView extends JDialog {

    private final AttendanceFileHandler attendanceFileHandler;

    public EmployeeDetailView(JFrame parent, Employee emp, AttendanceFileHandler handler) {
        super(parent, "Employee Full Details", true);
        this.attendanceFileHandler = handler;

        setLayout(new BorderLayout(10, 10));

        // Content panel showing employee info
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabel(contentPanel, "Employee Number:", emp.getEmployeeNumber());
        addLabel(contentPanel, "Name:", emp.getLastName() + ", " + emp.getFirstName());
        addLabel(contentPanel, "Birth Date:", emp.getBirthDay());
        addLabel(contentPanel, "Address:", emp.getAddress());
        addLabel(contentPanel, "Phone Number:", emp.getPhoneNumber());
        addLabel(contentPanel, "SSS Number:", emp.getSssNumber());
        addLabel(contentPanel, "PhilHealth Number:", emp.getPhilHealthNumber());
        addLabel(contentPanel, "TIN:", emp.getTin());
        addLabel(contentPanel, "Pag-IBIG Number:", emp.getPagIbigNumber());
        addLabel(contentPanel, "Status:", emp.getStatus());
        addLabel(contentPanel, "Position:", emp.getPosition());
        addLabel(contentPanel, "Basic Salary:", String.format("%.2f", emp.getBasicSalary()));
        addLabel(contentPanel, "Rice Subsidy:", String.format("%.2f", emp.getRiceSubsidy()));
        addLabel(contentPanel, "Phone Allowance:", String.format("%.2f", emp.getPhoneAllowance()));
        addLabel(contentPanel, "Clothing Allowance:", String.format("%.2f", emp.getClothingAllowance()));
        addLabel(contentPanel, "Gross Semi-monthly Rate:", String.format("%.2f", emp.getGrossRate()));
        addLabel(contentPanel, "Hourly Rate:", String.format("%.2f", emp.getMonthlyRate()));

        // Buttons
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        // Button panel add here to set visible
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Assemble dialog
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(820, 620);
        setLocationRelativeTo(parent);
    }

    private void addLabel(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value));
    }
}