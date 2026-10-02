package com.mycompany.motorphemployeeapp;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.*;
import java.util.ArrayList;

public class OvertimeRequestPanel extends JDialog {

    private final Employee employee;

    public OvertimeRequestPanel(Employee employee) {

        System.out.println("Constructor");

        this.employee = employee;

        initializeUI();

        System.out.println("initializeUI finished");
    }

    private void initializeUI() {
        System.out.println("initializeUI started");
        setTitle("Overtime Request");
        setSize(520, 560);
        setLayout(null);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setBackground(
                new Color(245, 245, 245)
                );

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel = new JLabel(
                "Overtime Request Form",
                SwingConstants.CENTER
                );

        titleLabel.setBounds(0, 20, 520, 35);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
                );

        add(titleLabel);
        System.out.println("Form panel created");

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel();

        formPanel.setLayout(null);
        formPanel.setBounds(30, 70, 460, 360);

        formPanel.setBackground(
                new Color(250, 250, 250)
                );

        formPanel.setBorder(
                new CompoundBorder(
                new LineBorder(
                new Color(220, 220, 220),
                1,
                true
                ),
                new EmptyBorder(
                10,
                10,
                10,
                10
                )
                )
                );

        add(formPanel);

        // =========================
        // DATE
        // =========================

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setBounds(20, 20, 120, 20);
        dateLabel.setForeground(Color.GRAY);
        formPanel.add(dateLabel);

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

        JComboBox<String> monthBox =
                new JComboBox<>(monthOptions);

        monthBox.setBounds(20, 45, 140, 35);
        formPanel.add(monthBox);

        JComboBox<Integer> dayBox =
                new JComboBox<>();

        dayBox.setBounds(170, 45, 70, 35);
        formPanel.add(dayBox);

        int currentYear = LocalDate.now().getYear();

        JComboBox<Integer> yearBox =
                new JComboBox<>(
                new Integer[]{
                    currentYear,
                    currentYear + 1
                }
                );

        yearBox.setBounds(250, 45, 90, 35);
        formPanel.add(yearBox);

        LocalDate today = LocalDate.now();

        monthBox.setSelectedIndex(
                today.getMonthValue() - 1
                );

        yearBox.setSelectedItem(
                today.getYear()
                );

        Runnable updateDayChoices = () -> {

            int selectedDay =
                    dayBox.getSelectedItem() == null
                    ? today.getDayOfMonth()
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
        };

        updateDayChoices.run();
        System.out.println("Day choices updated");

        monthBox.addActionListener(
                e -> updateDayChoices.run()
                );

        yearBox.addActionListener(
                e -> updateDayChoices.run()
                );

        // =========================
        // REASON
        // =========================

        JLabel reasonLabel = new JLabel("Reason");
        reasonLabel.setBounds(20, 105, 120, 20);
        reasonLabel.setForeground(Color.GRAY);
        formPanel.add(reasonLabel);

        JTextArea reasonField = new JTextArea();

        reasonField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reasonField.setLineWrap(true);
        reasonField.setWrapStyleWord(true);

        JScrollPane reasonScroll = new JScrollPane(reasonField);

        reasonScroll.setBounds(20, 130, 420, 120);

        formPanel.add(reasonScroll);
        System.out.println("Reason section created");

        // =========================
        // START TIME
        // =========================

        JLabel startLabel = new JLabel("Start Time");
        startLabel.setBounds(20, 275, 120, 20);
        startLabel.setForeground(Color.GRAY);
        formPanel.add(startLabel);

        ArrayList<String> timeList = new ArrayList<>();

        LocalTime time = LocalTime.of(17, 0);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("hh:mm a");

        // Generate time slots from 5:00 PM to 11:30 PM
        for (int i = 0; i <= 13; i++) {

            timeList.add(time.format(formatter));

            time = time.plusMinutes(30);
        }

        JComboBox<String> startField =
                new JComboBox<>(timeList.toArray(new String[0]));

        startField.setBounds(20, 300, 180, 35);
        startField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        formPanel.add(startField);

        JComboBox<String> endField =
                new JComboBox<>(timeList.toArray(new String[0]));

        endField.setBounds(260, 300, 180, 35);
        endField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        formPanel.add(endField);

        // Default selection: first available time at least 1 hour from now
        LocalTime earliestStart = LocalTime.now().plusHours(1);

        int defaultStartIndex = 0;

        for (int i = 0; i < timeList.size(); i++) {

            LocalTime option =
                    LocalTime.parse(timeList.get(i), formatter);

            if (!option.isBefore(earliestStart)) {
                defaultStartIndex = i;
                break;
            }
        }

        startField.setSelectedIndex(defaultStartIndex);

        if (defaultStartIndex < endField.getItemCount() - 1) {
            endField.setSelectedIndex(defaultStartIndex + 1);
        } else {
            endField.setSelectedIndex(defaultStartIndex);
        }

        // =========================
        // BUTTONS
        // =========================

        JButton applyButton = new JButton("Apply");

        applyButton.setBounds(110, 455, 110, 38);
        applyButton.setBackground(new Color(14, 92, 214));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");

        cancelButton.setBounds(300, 455, 110, 38);
        cancelButton.setBackground(new Color(240, 80, 80));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        add(applyButton);
        add(cancelButton);

        // =========================
        // BUTTON ACTIONS
        // =========================

        cancelButton.addActionListener(ev -> dispose());

        applyButton.addActionListener(ev -> {

                    int selectedMonth =
                    monthBox.getSelectedIndex() + 1;

                    int selectedDay =
                    (Integer) dayBox.getSelectedItem();

                    int selectedYear =
                    (Integer) yearBox.getSelectedItem();

                    String dateText =
                    String.format(
                    "%02d/%02d/%d",
                    selectedMonth,
                    selectedDay,
                    selectedYear
                    );

                    String reasonText =
                    reasonField.getText().trim();

                    String startText =
                    startField.getSelectedItem().toString();

                    String endText =
                    endField.getSelectedItem().toString();

                    LocalDate selectedDate =
                    LocalDate.of(
                    selectedYear,
                    selectedMonth,
                    selectedDay
                    );

                    if (selectedDate.isBefore(LocalDate.now())) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Overtime date cannot be in the past.",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    if (reasonText.isEmpty()) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Please enter a reason.",
                        "Missing Information",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    if (startText.equals(endText)) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Start Time and End Time cannot be the same.",
                        "Invalid Time",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    LocalTime selectedStartTime =
                    LocalTime.parse(
                    startText,
                    DateTimeFormatter.ofPattern("hh:mm a")
                    );

                    if (selectedDate.equals(LocalDate.now())
                    && !selectedStartTime.isAfter(LocalTime.now())) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Overtime start time cannot be earlier than the current time.",
                        "Invalid Time",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    if (endField.getSelectedIndex()
                    <= startField.getSelectedIndex()) {

                        JOptionPane.showMessageDialog(
                        this,
                        "End Time must be later than Start Time.",
                        "Invalid Time",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    boolean saved =
                    OvertimeRequestService.submitRequest(
                    employee,
                    dateText,
                    startText,
                    endText,
                    reasonText
                    );

                    if (!saved) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Unable to submit overtime request.",
                        "Submission Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    JDialog successDialog = new JDialog(this, "Success", true);

                    successDialog.setSize(420, 220);
                    successDialog.setResizable(false);
                    successDialog.setLocationRelativeTo(this);
                    successDialog.setLayout(null);
                    successDialog.getContentPane().setBackground(Color.WHITE);

                    // Title
                    JLabel successTitle = new JLabel(
                    "Overtime Request Sent!",
                    SwingConstants.CENTER
                    );

                    successTitle.setBounds(20, 25, 380, 35);
                    successTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
                    successTitle.setForeground(new Color(23, 138, 47));

                    successDialog.add(successTitle);

                    // Message
                    JLabel successMessage = new JLabel(
                    "<html><center>"
                    + "Your hard work makes a difference.<br>"
                    + "Thank you!"
                    + "</center></html>",
                    SwingConstants.CENTER
                    );

                    successMessage.setBounds(20, 75, 380, 45);
                    successMessage.setFont(new Font("Segoe UI", Font.PLAIN, 15));

                    successDialog.add(successMessage);

                    // OK Button
                    RoundedButton okButton =
                    new RoundedButton(
                    "OK",
                    new Color(14, 92, 214),
                    Color.WHITE
                    );

                    okButton.setBounds(160, 135, 100, 35);
                    okButton.setBackground(new Color(14, 92, 214));
                    okButton.setForeground(Color.WHITE);
                    okButton.setFocusPainted(false);

                    okButton.addActionListener(e -> {
                        successDialog.dispose();
                        dispose(); // closes the Overtime Request form
                    });

                    successDialog.add(okButton);

                    successDialog.setVisible(true);

                });

    }
}
