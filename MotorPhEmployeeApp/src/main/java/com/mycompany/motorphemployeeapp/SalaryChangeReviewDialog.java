package com.mycompany.motorphemployeeapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SalaryChangeReviewDialog extends JDialog {

    private JTable requestTable;
    private DefaultTableModel tableModel;
    private EmployeeDataManager manager;
    private Employee reviewer;

    public SalaryChangeReviewDialog(
            Window parent
            ) {

        this(parent, null);
    }

    public SalaryChangeReviewDialog(
            Window parent,
            Employee reviewer
            ) {

        super(
                parent,
                "Salary Change Requests",
                ModalityType.APPLICATION_MODAL
                );

        manager =
                new EmployeeDataManager();
        this.reviewer =
                reviewer;

        setSize(980, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columns = {
            "Request ID",
                    "Employee ID",
                    "Employee Name",
                    "Old Salary",
                    "New Salary",
                    "Old Rice",
                    "New Rice",
                    "Old Phone",
                    "New Phone",
                    "Old Clothing",
                    "New Clothing",
                    "Requested By",
                    "Requested Date",
                    "Status"
                };

        tableModel =
                new DefaultTableModel(
                columns,
                0
                ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
                    ) {

                return false;
            }
        };

        requestTable =
                new JTable(tableModel);

        requestTable.setRowHeight(28);
        requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        add(
                new JScrollPane(requestTable),
                BorderLayout.CENTER
                );

        JPanel buttonPanel =
                new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
                );

        JButton approveButton =
                new JButton("Approve");

        JButton rejectButton =
                new JButton("Reject");

        JButton closeButton =
                new JButton("Close");

        approveButton.setBackground(
                new Color(46, 125, 50)
                );

        approveButton.setForeground(Color.WHITE);

        rejectButton.setBackground(
                new Color(220, 60, 60)
                );

        rejectButton.setForeground(Color.WHITE);

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(closeButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH
                );

        approveButton.addActionListener(
                e -> reviewSelectedRequest(true)
                );

        rejectButton.addActionListener(
                e -> reviewSelectedRequest(false)
                );

        closeButton.addActionListener(
                e -> dispose()
                );

        loadRequests();

        setVisible(true);
    }

    private void loadRequests() {

        tableModel.setRowCount(0);

        List<String[]> requests =
                manager.getPendingSalaryChangeRequests();

        for(String[] request : requests){

            tableModel.addRow(
                    new Object[]{
                        request[0],
                        request[1],
                        request[2],
                        request[3],
                        request[4],
                        request[5],
                        request[6],
                        request[7],
                        request[8],
                        request[9],
                        request[10],
                        request[11],
                        request[12],
                        request[13]
                    }
                    );
        }

        for(int i = 0; i < requestTable.getColumnCount(); i++){

            requestTable.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                    i == 12 ? 180 : 110
                    );
        }

        requestTable.setPreferredScrollableViewportSize(
                new Dimension(940, 390)
                );
    }

    private void reviewSelectedRequest(
            boolean approve
            ) {

        int selectedRow =
                requestTable.getSelectedRow();

        if(selectedRow == -1){

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a salary change request."
                    );

            return;
        }

        int modelRow =
                requestTable.convertRowIndexToModel(
                selectedRow
                );

        String requestId =
                tableModel.getValueAt(
                modelRow,
                0
                ).toString();

        String requestEmployeeId =
                tableModel.getValueAt(
                modelRow,
                1
                ).toString();

        if(isOwnRequest(requestEmployeeId)){

            JOptionPane.showMessageDialog(
                    this,
                    "You cannot approve or reject your own salary change request.",
                    "Self-Approval Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        boolean success =
                approve
                ? manager.approveSalaryChangeRequest(
                requestId,
                getReviewerName()
                )
                : manager.rejectSalaryChangeRequest(
                requestId,
                getReviewerName()
                );

        if(success){

            JOptionPane.showMessageDialog(
                    this,
                    approve
                    ? "Salary change approved and employee CSV updated."
                    : "Salary change request rejected."
                    );

            loadRequests();

        }else{

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to review salary change request.",
                    "Review Error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }
    }

    private boolean isOwnRequest(
            String requestEmployeeId
            ) {

        return reviewer != null
                && reviewer.getEmployeeNumber() != null
                && reviewer.getEmployeeNumber()
                .trim()
                .equals(requestEmployeeId.trim());
    }

    private String getReviewerName() {

        if(reviewer == null){
            return "Payroll Manager";
        }

        String reviewerName =
                (reviewer.getFirstName() + " " + reviewer.getLastName())
                .trim();

        if(reviewerName.isEmpty()){
            return "Payroll Manager";
        }

        return reviewerName;
    }
}
