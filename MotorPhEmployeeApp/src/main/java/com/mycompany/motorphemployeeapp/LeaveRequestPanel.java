package com.mycompany.motorphemployeeapp;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class LeaveRequestPanel extends JDialog {

    private final Employee employee;

    public LeaveRequestPanel(Employee employee) {

        this.employee = employee;

        setTitle("Leave Request");
        setSize(600, 650);
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(
                new Color(245, 245, 245)
                );

        // =========================
        // TITLE
        // =========================

        JLabel title = new JLabel(
                "Leave Request Form",
                SwingConstants.CENTER
                );

        title.setBounds(0, 15, 600, 40);
        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                28
                )
                );

        add(title);

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel();

        formPanel.setLayout(null);
        formPanel.setBounds(20, 70, 550, 450);
        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                new LineBorder(
                new Color(220, 220, 220),
                1,
                true
                )
                );

        add(formPanel);

        // =========================
        // TYPE
        // =========================

        JLabel typeLabel = new JLabel("Type");

        typeLabel.setBounds(20, 20, 100, 20);

        formPanel.add(typeLabel);

        JComboBox<String> typeBox =
                new JComboBox<>(

                new String[]{
                    "",
                    "Vacation Leave",
                    "Sick Leave",
                    "Paid Time Off (PTO)",
                    "Emergency Leave",
                    "Leave of Absence (LOA)"
                }
                );

        typeBox.setBounds(20, 42, 510, 40);

        formPanel.add(typeBox);

        // =========================
        // REASON
        // =========================

        JLabel reasonLabel = new JLabel("Reason");

        reasonLabel.setBounds(20, 90, 100, 20);

        formPanel.add(reasonLabel);

        JTextArea reasonArea = new JTextArea();

        reasonArea.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);

        JScrollPane reasonScroll =
                new JScrollPane(reasonArea);

        reasonScroll.setBounds(20, 112, 510, 150);

        formPanel.add(reasonScroll);

        // =========================
        // START DATE
        // =========================

        JLabel startLabel = new JLabel("Start Date");

        startLabel.setBounds(20, 280, 100, 20);

        formPanel.add(startLabel);

        String[] monthOptions = {

            "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December"
                };

        LocalDate today =
                LocalDate.now();

        int currentYear =
                today.getYear();

        JComboBox<String> startMonthBox =
                new JComboBox<>(monthOptions);

        startMonthBox.setBounds(20, 302, 220, 38);

        formPanel.add(startMonthBox);

        JComboBox<Integer> startDayBox =
                new JComboBox<>();

        startDayBox.setBounds(245, 302, 120, 38);

        formPanel.add(startDayBox);

        JComboBox<Integer> startYearBox =
                new JComboBox<>(
                new Integer[]{
                    currentYear,
                    currentYear + 1
                }
                );

        startYearBox.setBounds(370, 302, 160, 38);

        formPanel.add(startYearBox);

        // =========================
        // END DATE
        // =========================

        JLabel endLabel = new JLabel("End Date");

        endLabel.setBounds(20, 360, 100, 20);

        formPanel.add(endLabel);

        JComboBox<String> endMonthBox =
                new JComboBox<>(monthOptions);

        endMonthBox.setBounds(20, 382, 220, 38);

        formPanel.add(endMonthBox);

        JComboBox<Integer> endDayBox =
                new JComboBox<>();

        endDayBox.setBounds(245, 382, 120, 38);

        formPanel.add(endDayBox);

        JComboBox<Integer> endYearBox =
                new JComboBox<>(
                new Integer[]{
                    currentYear,
                    currentYear + 1
                }
                );

        endYearBox.setBounds(370, 382, 160, 38);

        formPanel.add(endYearBox);

        Runnable updateStartDays =
                () -> updateDayChoices(
                startMonthBox,
                startDayBox,
                startYearBox,
                today.getDayOfMonth()
                );

        Runnable updateEndDays =
                () -> updateDayChoices(
                endMonthBox,
                endDayBox,
                endYearBox,
                today.getDayOfMonth()
                );

        startMonthBox.setSelectedIndex(
                today.getMonthValue() - 1
                );

        endMonthBox.setSelectedIndex(
                today.getMonthValue() - 1
                );

        startYearBox.setSelectedItem(
                today.getYear()
                );

        endYearBox.setSelectedItem(
                today.getYear()
                );

        updateStartDays.run();
        updateEndDays.run();

        startDayBox.setSelectedItem(
                today.getDayOfMonth()
                );

        endDayBox.setSelectedItem(
                today.getDayOfMonth()
                );

        startMonthBox.addActionListener(event -> {

                    updateStartDays.run();

                    endMonthBox.setSelectedIndex(startMonthBox.getSelectedIndex());
                    endYearBox.setSelectedItem(startYearBox.getSelectedItem());

                    updateEndDays.run();

                    endDayBox.setSelectedItem(startDayBox.getSelectedItem());

                });

        startYearBox.addActionListener(event -> {

                    updateStartDays.run();

                    endMonthBox.setSelectedIndex(startMonthBox.getSelectedIndex());
                    endYearBox.setSelectedItem(startYearBox.getSelectedItem());

                    updateEndDays.run();

                    endDayBox.setSelectedItem(startDayBox.getSelectedItem());

                });

        startDayBox.addActionListener(event -> {

                    endDayBox.setSelectedItem(startDayBox.getSelectedItem());

                });

        endMonthBox.addActionListener(
                event -> updateEndDays.run()
                );

        endYearBox.addActionListener(
                event -> updateEndDays.run()
                );

        // =========================
        // APPLY BUTTON
        // =========================

        RoundedButton applyButton =
                new RoundedButton(
                "Apply",
                new Color(18, 92, 210),
                Color.WHITE
                );

        applyButton.setBounds(165, 545, 120, 38);

        add(applyButton);

        // =========================
        // CANCEL BUTTON
        // =========================

        RoundedButton cancelButton =
                new RoundedButton(
                "Cancel",
                new Color(255, 85, 85),
                Color.WHITE
                );

        cancelButton.setBounds(315, 545, 120, 38);

        add(cancelButton);

        // =========================
        // APPLY EVENT
        // =========================

        applyButton.addActionListener(e -> {

                    try {

                        if (typeBox.getSelectedIndex() == 0) {

                            JOptionPane.showMessageDialog(

                            this,

                            "Please select a leave type.",

                            "Missing Information",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if (reasonArea.getText().trim().isEmpty()) {

                            JOptionPane.showMessageDialog(

                            this,

                            "Please enter a reason.",

                            "Missing Information",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        LocalDate startDate =
                        buildSelectedDate(
                        startMonthBox,
                        startDayBox,
                        startYearBox
                        );

                        LocalDate endDate =
                        buildSelectedDate(
                        endMonthBox,
                        endDayBox,
                        endYearBox
                        );

                        if (startDate.isBefore(LocalDate.now())) {

                            JOptionPane.showMessageDialog(

                            this,

                            "Start Date cannot be in the past.",

                            "Invalid Date",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if (endDate.isBefore(LocalDate.now())) {

                            JOptionPane.showMessageDialog(

                            this,

                            "End Date cannot be in the past.",

                            "Invalid Date",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if (endDate.isBefore(startDate)) {

                            JOptionPane.showMessageDialog(

                            this,

                            "End Date cannot be earlier than Start Date.",

                            "Invalid Date",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        String startDateText =
                        String.format(
                        "%02d/%02d/%d",
                        startDate.getMonthValue(),
                        startDate.getDayOfMonth(),
                        startDate.getYear()
                        );

                        String endDateText =
                        String.format(
                        "%02d/%02d/%d",
                        endDate.getMonthValue(),
                        endDate.getDayOfMonth(),
                        endDate.getYear()
                        );

                        long days =
                        ChronoUnit.DAYS.between(
                        startDate,
                        endDate
                        ) + 1;

                        boolean saved =
                        saveLeaveRequest(
                        typeBox.getSelectedItem().toString(),
                        reasonArea.getText().trim(),
                        startDateText,
                        endDateText,
                        days + " day(s)"
                        );

                        if (!saved) {

                            JOptionPane.showMessageDialog(

                            this,

                            "Unable to save leave request. Please try again.",

                            "Save Error",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        String message;

                        switch (typeBox.getSelectedItem().toString()) {

                            case "Vacation Leave":
                                message =
                                "Your vacation leave request has been submitted.\n\n"
                                + "We hope you enjoy your well-deserved break.\n"
                                + "Have a wonderful and relaxing time!";
                                break;

                            case "Sick Leave":
                                message =
                                "Your sick leave request has been submitted.\n\n"
                                + "Your health comes first.\n "
                                + "We wish you a speedy recovery and hope to see you back soon.";
                                break;

                            case "Paid Time Off (PTO)":
                                message =
                                "Your paid time off request has been submitted.\n\n"
                                + "Enjoy your time away and make the most of your well-earned break.";
                                break;

                            case "Emergency Leave":
                                message =
                                "Your emergency leave request has been submitted.\n\n"
                                + "We understand that unexpected situations happen.\n"
                                + "Take care, and we hope everything goes well.";
                                break;

                            case "Leave of Absence (LOA)":
                                message =
                                "Your Leave of Absence request has been submitted.\n\n"
                                + "Your request will be carefully reviewed.\n"
                                + "We wish you the very best during this time.";
                                break;

                            default:
                                message =
                                "Your leave request has been submitted.\n\n"
                                + "You'll be notified once it's reviewed.";
                        }

                        JOptionPane.showMessageDialog(
                        this,
                        message,
                        "Leave Request Submitted",
                        JOptionPane.INFORMATION_MESSAGE
                        );

                        dispose();

                    } catch (Exception ex) {

                        JOptionPane.showMessageDialog(

                        this,

                        "An unexpected error occurred:\n"
                        +
                        ex.getMessage(),

                        "System Error",

                        JOptionPane.ERROR_MESSAGE
                        );
                    }

                });

        // =========================
        // CANCEL EVENT
        // =========================

        cancelButton.addActionListener(e -> {

                    dispose();

                });
    }

    // =========================
    // SAVE LEAVE REQUEST
    // =========================

    private boolean saveLeaveRequest(
            String leaveType,
            String reason,
            String startDate,
            String endDate,
            String duration
            ) {

        try {

            File file =
                    new File(
                    "src/main/resources/LeaveRequests.csv"
                    );

            boolean fileExists =
                    file.exists();

            try (FileWriter writer =
                    new FileWriter(
                    file,
                    true
                    )) {

                if (!fileExists) {

                    writer.write(
                            "Request ID,Employee ID,Employee Name,Supervisor,Leave Type,Start Date,End Date,Duration,Reason,Submitted Date,Status,Reviewed By,Review Date"
                            );
                }

                String employeeName =
                        employee.getFirstName()
                        + " "
                        + employee.getLastName();

                String[] row = {

                    String.valueOf(
                            System.currentTimeMillis()
                            ),

                            employee.getEmployeeNumber(),

                            employeeName,

                            employee.getSupervisor(),

                            leaveType,

                            startDate,

                            endDate,

                            duration,

                            reason,

                            LocalDateTime.now().toString(),

                            "Pending",

                            "",

                            ""
                        };

                writer.write(
                        System.lineSeparator()
                        + formatCSVLine(row)
                        );
            }

            return true;

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return false;
    }

    // =========================
    // FORMAT CSV
    // =========================

    private String formatCSVLine(
            String[] data
            ) {

        StringBuilder line =
                new StringBuilder();

        for (int i = 0; i < data.length; i++) {

            String value =
                    data[i] == null
                    ? ""
                    : data[i].trim();

            if (value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")) {

                value =
                        "\""
                        + value.replace("\"", "\"\"")
                        + "\"";
            }

            line.append(value);

            if (i < data.length - 1) {

                line.append(",");
            }
        }

        return line.toString();
    }

    // =========================
    // UPDATE DAY CHOICES
    // =========================

    private void updateDayChoices(
            JComboBox<String> monthBox,
            JComboBox<Integer> dayBox,
            JComboBox<Integer> yearBox,
            int fallbackDay
            ) {

        int selectedDay =
                dayBox.getSelectedItem() == null
                ? fallbackDay
                : (Integer) dayBox.getSelectedItem();

        int month =
                monthBox.getSelectedIndex() + 1;

        int year =
                (Integer) yearBox.getSelectedItem();

        int maxDay =
                YearMonth.of(
                year,
                month
                ).lengthOfMonth();

        dayBox.removeAllItems();

        for (int day = 1; day <= maxDay; day++) {

            dayBox.addItem(day);
        }

        dayBox.setSelectedItem(
                Math.min(
                selectedDay,
                maxDay
                )
                );
    }

    // =========================
    // BUILD DATE
    // =========================

    private LocalDate buildSelectedDate(
            JComboBox<String> monthBox,
            JComboBox<Integer> dayBox,
            JComboBox<Integer> yearBox
            ) {

        return LocalDate.of(
                (Integer) yearBox.getSelectedItem(),
                monthBox.getSelectedIndex() + 1,
                (Integer) dayBox.getSelectedItem()
                );
    }
}
