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
 *  1.  Interface to view all list of employee using JFrame on top of JDialog.
 *  2.  Added button to view the detailed information
 *          of employee via EmployeeDetailView class using JDialog.
 *  3.  Added button to add new employee via NewEmployeeForm class using JDialog.
 *  4.  Added search bar for quick search to specific employee.
 *  5.  Added refresh button to provide the updated data every time.
 * 
 */

import alfie.model.Employee;
import alfie.util.EmployeeFileHandler;
import alfie.util.AttendanceFileHandler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class EmployeeListView extends JDialog {

    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField searchField;

    private final JButton editEmployeeButton;
    private final JButton viewDetailsButton;
    private final JButton deleteEmployeeButton;
    private final JButton newEmployeeButton;

    private boolean loggedIn;

    public EmployeeListView(JFrame parent, boolean isLoggedIn) {
        super(parent, "Employee List", true);
        this.loggedIn = isLoggedIn;

        setSize(860, 660);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Table setup
        String[] columnNames = {
            "Employee Number", "Last Name", "First Name",
            "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize buttons
        viewDetailsButton = new JButton("View Details");
        editEmployeeButton = new JButton("Edit Employee");
        deleteEmployeeButton = new JButton("Delete Employee");
        newEmployeeButton = new JButton("Add New Employee");

        viewDetailsButton.addActionListener(e -> showEmployeeDetails());
        editEmployeeButton.addActionListener(e -> editSelectedEmployee());
        deleteEmployeeButton.addActionListener(e -> deleteSelectedEmployee());
        newEmployeeButton.addActionListener(e -> {
            NewEmployeeForm form = new NewEmployeeForm((JFrame) SwingUtilities.getWindowAncestor(this), new EmployeeFileHandler(), this::loadEmployeeData);
            form.setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(viewDetailsButton);
        bottomPanel.add(editEmployeeButton);
        bottomPanel.add(newEmployeeButton);
        bottomPanel.add(deleteEmployeeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Top panel (Search + Refresh)
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton refreshButton = new JButton("Refresh");

        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { search(); }
            @Override public void removeUpdate(DocumentEvent e) { search(); }
            @Override public void changedUpdate(DocumentEvent e) { search(); }

            private void search() {
                String text = searchField.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        refreshButton.addActionListener(e -> loadEmployeeData());

        employeeTable.getSelectionModel().addListSelectionListener(e -> updateButtonStates());

        loadEmployeeData();
        setProtectedButtonsEnabled(loggedIn); // Initial state based on login
    }

    public void setProtectedButtonsEnabled(boolean enable) {
        this.loggedIn = enable;
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean selected = employeeTable.getSelectedRow() != -1;
        viewDetailsButton.setEnabled(loggedIn && selected);
        editEmployeeButton.setEnabled(loggedIn && selected);
        deleteEmployeeButton.setEnabled(loggedIn && selected);
        newEmployeeButton.setEnabled(loggedIn);
    }

    private void showEmployeeDetails() {
        Employee emp = getSelectedEmployee();
        if (emp != null) {
            AttendanceFileHandler handler = new AttendanceFileHandler();
            new EmployeeDetailView((JFrame) SwingUtilities.getWindowAncestor(this), emp, handler).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Employee details not found.");
        }
    }

    private void editSelectedEmployee() {
        Employee emp = getSelectedEmployee();
        if (emp != null) {
            new EditEmployeeForm((JFrame) SwingUtilities.getWindowAncestor(this), emp, new EmployeeFileHandler(), this::loadEmployeeData).setVisible(true);
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        String empNumber = (String) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Employee #" + empNumber + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = new EmployeeFileHandler().deleteEmployeeByNumber(empNumber);
            if (success) {
                JOptionPane.showMessageDialog(this, "Employee deleted.");
                loadEmployeeData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete employee.");
            }
        }
    }

    private Employee getSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) return null;

        String empNumber = (String) tableModel.getValueAt(employeeTable.convertRowIndexToModel(selectedRow), 0);
        return new EmployeeFileHandler().readEmployees().stream()
                .filter(e -> e.getEmployeeNumber().equals(empNumber))
                .findFirst()
                .orElse(null);
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        List<Employee> employees = new EmployeeFileHandler().readEmployees();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(),
                emp.getSssNumber(), emp.getPhilHealthNumber(), emp.getTin(), emp.getPagIbigNumber()
            });
        }
    }
}