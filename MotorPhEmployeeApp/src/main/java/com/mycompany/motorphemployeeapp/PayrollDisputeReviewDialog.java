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

public class PayrollDisputeReviewDialog extends JDialog {

    private JTable disputeTable;
    private DefaultTableModel tableModel;
    private EmployeeDataManager manager;
    private Employee reviewer;

    public PayrollDisputeReviewDialog(
            Window parent
            ) {

        this(parent, null);
    }

    public PayrollDisputeReviewDialog(
            Window parent,
            Employee reviewer
            ) {

        super(
                parent,
                "Payroll Dispute Requests",
                ModalityType.APPLICATION_MODAL
                );

        manager =
                new EmployeeDataManager();
        this.reviewer =
                reviewer;

        setSize(1050, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columns = {
            "Dispute ID",
                    "Employee ID",
                    "Employee Name",
                    "Cutoff Period",
                    "Date of Record",
                    "Time In",
                    "Time Out",
                    "Overtime",
                    "Category",
                    "Evidence",
                    "Remarks",
                    "Submitted Date",
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

        disputeTable =
                new JTable(tableModel);

        disputeTable.setRowHeight(28);
        disputeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        add(
                new JScrollPane(disputeTable),
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
                e -> reviewSelectedDispute(true)
                );

        rejectButton.addActionListener(
                e -> reviewSelectedDispute(false)
                );

        closeButton.addActionListener(
                e -> dispose()
                );

        loadDisputes();

        setVisible(true);
    }

    private void loadDisputes() {

        tableModel.setRowCount(0);

        List<String[]> disputes =
                manager.getPendingPayrollDisputeRequests();

        for(String[] dispute : disputes){

            tableModel.addRow(
                    new Object[]{
                        dispute[0],
                        dispute[1],
                        dispute[2],
                        dispute[3],
                        dispute[4],
                        dispute[5],
                        dispute[6],
                        dispute[7],
                        dispute[8],
                        dispute[9],
                        dispute[10],
                        dispute[11],
                        dispute[12]
                    }
                    );
        }

        for(int i = 0; i < disputeTable.getColumnCount(); i++){

            disputeTable.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                    i == 10 || i == 11 ? 180 : 115
                    );
        }

        disputeTable.setPreferredScrollableViewportSize(
                new Dimension(1000, 390)
                );
    }

    private void reviewSelectedDispute(
            boolean approve
            ) {

        int selectedRow =
                disputeTable.getSelectedRow();

        if(selectedRow == -1){

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a dispute request."
                    );

            return;
        }

        int modelRow =
                disputeTable.convertRowIndexToModel(
                selectedRow
                );

        String disputeId =
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
                    "You cannot approve or reject your own payroll dispute request.",
                    "Self-Approval Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        boolean success =
                approve
                ? manager.approvePayrollDisputeRequest(
                disputeId,
                getReviewerName()
                )
                : manager.rejectPayrollDisputeRequest(
                disputeId,
                getReviewerName()
                );

        if(success){

            JOptionPane.showMessageDialog(
                    this,
                    approve
                    ? "Dispute request approved."
                    : "Dispute request rejected."
                    );

            loadDisputes();

        }else{

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to review dispute request.",
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
