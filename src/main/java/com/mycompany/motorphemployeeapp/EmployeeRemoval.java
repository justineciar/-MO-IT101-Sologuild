package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.table.JTableHeader;

public class EmployeeRemoval {

    public static void deactivateEmployee(
            JTable employeeTable,
            JPanel parent,
            HREmployeeListPanel panel
            ) {

        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Please select an employee."
                    );

            return;
        }

        selectedRow =
                employeeTable.convertRowIndexToModel(
                selectedRow
                );

        String employeeId =
                employeeTable.getModel().getValueAt(
                selectedRow,
                0
                ).toString();

        int confirm =
                JOptionPane.showConfirmDialog(
                parent,
                "Mark Employee "
                + employeeId
                + " as Inactive?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
                );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        EmployeeDataManager manager =
                new EmployeeDataManager();

        boolean success =
                manager.deactivateEmployee(employeeId);

        if (success) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Employee marked as inactive."
                    );

            panel.refreshTable();

        } else {

            JOptionPane.showMessageDialog(
                    parent,
                    "Error deleting employee.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }
    }
}
