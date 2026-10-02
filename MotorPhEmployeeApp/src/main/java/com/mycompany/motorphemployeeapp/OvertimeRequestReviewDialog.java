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

public class OvertimeRequestReviewDialog extends JDialog {

    private Employee reviewer;
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public OvertimeRequestReviewDialog(
            Window parent,
            Employee reviewer
            ) {

        super(
                parent,
                "Pending Overtime Requests",
                ModalityType.APPLICATION_MODAL
                );

        this.reviewer = reviewer;

        setSize(980, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columns = {
            "Request ID",
                    "Employee ID",
                    "Employee Name",
                    "Date",
                    "Start Time",
                    "End Time",
                    "Hours",
                    "Reason",
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
                OvertimeRequestService.getPendingRequestsForSupervisor(
                reviewer
                );

        for(String[] request : requests){

            tableModel.addRow(
                    new Object[]{
                        request[0],
                        request[1],
                        request[2],
                        request[4],
                        request[5],
                        request[6],
                        request[7],
                        request[8],
                        request[9],
                        request[10]
                    }
                    );
        }

        for(int i = 0; i < requestTable.getColumnCount(); i++){

            requestTable.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                    i == 7 || i == 8 ? 180 : 110
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
                    "Please select an overtime request."
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

        if(reviewer != null
                && reviewer.getEmployeeNumber() != null
                && reviewer.getEmployeeNumber()
                .trim()
                .equals(requestEmployeeId.trim())){

            JOptionPane.showMessageDialog(
                    this,
                    "You cannot approve or reject your own overtime request.",
                    "Self-Approval Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        boolean success =
                OvertimeRequestService.reviewRequest(
                requestId,
                approve,
                reviewer
                );

        if(success){

            JOptionPane.showMessageDialog(
                    this,
                    approve
                    ? "Overtime request approved."
                    : "Overtime request rejected."
                    );

            loadRequests();

        }else{

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to review overtime request.",
                    "Review Error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }
    }
}
