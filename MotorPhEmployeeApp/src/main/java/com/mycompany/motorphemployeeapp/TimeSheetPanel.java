package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TimeSheetPanel extends JPanel {

    public TimeSheetPanel(Employee employee) {

        setBounds(0, 0, 780, 700);
        setBackground(new Color(227, 234, 231));
        setLayout(null);

        // =========================
        // BREADCRUMB
        // =========================

        JLabel breadcrumb = new JLabel("Home > Timesheet");

        breadcrumb.setBounds(40, 45, 250, 20);
        breadcrumb.setForeground(new Color(120, 120, 120));
        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        add(breadcrumb);

        // =========================
        // TITLE
        // =========================

        JLabel title = new JLabel("Timesheet");

        title.setBounds(40, 90, 250, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));

        add(title);

        // =========================
        // OVERTIME BUTTON
        // =========================

        JButton overtimeButton = new JButton("+ Overtime Request");

        overtimeButton.setBounds(40, 150, 180, 36);
        overtimeButton.setBackground(new Color(38, 148, 188));
        overtimeButton.setForeground(Color.WHITE);
        overtimeButton.setFocusPainted(false);
        overtimeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        overtimeButton.setBorderPainted(false);
        overtimeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(overtimeButton);

        // =========================
        // RECENT LABEL
        // =========================

        JLabel recentLabel = new JLabel("Recent Attendance Records");

        recentLabel.setBounds(40, 215, 250, 25);
        recentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        add(recentLabel);

        // =========================
        // TABLE COLUMNS
        // =========================

        String[] columns = {

            "Date",
                    "Clock In",
                    "Clock Out",
                    "Meal Break",
                    "Overtime",
                    "Total Hours",
                    "Status"
                };

        // =========================
        // LOAD ATTENDANCE
        // =========================

        EmployeeDataManager manager = new EmployeeDataManager();

        manager.loadAttendanceData(
                "src/main/resources/MotorPH_Employee Data - Attendance Record.csv"
                );

        java.util.ArrayList<AttendanceRecord> attendanceRecords =
                manager.getAttendanceData();

        java.util.ArrayList<String[]> employeeRows =
                new java.util.ArrayList<>();

        for (int i = attendanceRecords.size() - 1; i >= 0; i--) {

            AttendanceRecord record = attendanceRecords.get(i);

            if (record.getEmployeeNumber().equals(
                    employee.getEmployeeNumber()
                    )) {

                employeeRows.add(

                        new String[] {

                            record.getDate(),
                            record.getTimeIn(),
                            record.getTimeOut(),
                            record.getBreakDisplay(),
                            String.format(
                            "%.2f hour",
                            record.getOvertimeHours()
                            ),

                            String.format(
                            "%.2f hours",
                            record.getTotalHoursWorked()
                            ),
                            record.getStatus()
                        }
                        );
            }
        }

        String[][] rows = employeeRows.toArray(new String[0][]);

        // =========================
        // TABLE
        // =========================

        JTable table = new JTable(rows, columns);

        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
                );

        table.getTableHeader().setBackground(
                new Color(215, 215, 215)
                );

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBounds(40, 250, 700, 380);

        scrollPane.setBorder(
                new LineBorder(new Color(210, 210, 210))
                );

        add(scrollPane);

        // =========================
        // OVERTIME FORM
        // =========================

        overtimeButton.addActionListener(e -> {

                    OvertimeRequestPanel panel =
                    new OvertimeRequestPanel(employee);

                    panel.setVisible(true);

                });
    }
}
