/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.view;

import alfie.model.Employee;
import java.awt.*;
import javax.swing.*;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR02-Feature 2
 * Purpose: Interface to view the monthly salary of employee.
 * 
 */

public class SalaryReportView {

    public static void show(Employee emp, double totalHours, double salary, String monthName) {
        // Create dialog
        JDialog dialog = new JDialog((JFrame) null, "Monthly Salary Report", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(10, 10));

        // Panel for content
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Header design
        Font headerFont = new Font("SansSerif", Font.BOLD, 18);
        Font labelFont = new Font("Monospaced", Font.BOLD, 16);
        Font contentFont = new Font("Monospaced", Font.PLAIN, 14);

        String fullName = emp.getFirstName() + " " + emp.getLastName();
        JLabel heading = new JLabel("Monthly salary report") ;
        heading.setFont(headerFont);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(heading);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        contentPanel.add(makeLine("Name", fullName, labelFont));
        contentPanel.add(makeLine("Month ", monthName, labelFont));

        // Add info
        contentPanel.add(makeLine("Total Hours Worked", String.format("%.2f hrs", totalHours), contentFont));
        contentPanel.add(makeLine("Hourly Rate", String.format("₱%.2f", emp.getMonthlyRate()), contentFont));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        contentPanel.add(makeLine("Basic Salary", String.format("₱%.2f", emp.getBasicSalary()), contentFont));
        contentPanel.add(makeLine("Rice Subsidy", String.format("₱%.2f", emp.getRiceSubsidy()), contentFont));
        contentPanel.add(makeLine("Phone Allowance", String.format("₱%.2f", emp.getPhoneAllowance()), contentFont));
        contentPanel.add(makeLine("Clothing Allowance", String.format("₱%.2f", emp.getClothingAllowance()), contentFont));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        contentPanel.add(makeLine("Gross Monthly Salary", String.format("₱%.2f", salary), labelFont));

        // Close button
        JButton close = new JButton("Close");
        close.setAlignmentX(Component.CENTER_ALIGNMENT);
        close.addActionListener(e -> dialog.dispose());

        // Bottom panel for button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(close);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static JPanel makeLine(String label, String value, Font font) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label + ":"), BorderLayout.WEST);
        JLabel val = new JLabel(value);
        val.setFont(font);
        panel.add(val, BorderLayout.EAST);
        return panel;
    }
}
