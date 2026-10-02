package com.mycompany.motorphemployeeapp;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.border.LineBorder;

public class PayrollDisputePanel {

    private Employee employee;
    private String selectedPeriod;

    public PayrollDisputePanel(
            Employee employee,
            String selectedPeriod
            ) {

        this.employee = employee;
        this.selectedPeriod = selectedPeriod;

    }

    // =========================
    // DISPUTE DIALOG
    // =========================

    public void showDialog() {

        JDialog dialog =
                new JDialog();

        dialog.setTitle("File Dispute");

        dialog.setSize(560, 610);

        dialog.setLayout(null);

        dialog.setLocationRelativeTo(null);

        dialog.getContentPane().setBackground(
                new Color(245, 245, 245)
                );

        // =========================
        // TITLE
        // =========================

        JLabel title =
                new JLabel(
                "File a Dispute",
                SwingConstants.CENTER
                );

        title.setBounds(
                110,
                25,
                320,
                40
                );

        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                38
                )
                );

        dialog.add(title);

        JLabel subtitle =
                new JLabel(
                "Please provide the details of your dispute for review",
                SwingConstants.CENTER
                );

        subtitle.setBounds(
                70,
                78,
                400,
                22
                );

        subtitle.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                16
                )
                );

        dialog.add(subtitle);

        // =========================
        // DETAILS TITLE
        // =========================

        JLabel detailsTitle =
                new JLabel("Dispute Details");

        detailsTitle.setBounds(
                40,
                120,
                220,
                30
                );

        detailsTitle.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                20
                )
                );

        dialog.add(detailsTitle);

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel();

        formPanel.setLayout(null);

        formPanel.setBounds(
                40,
                155,
                490,
                340
                );

        formPanel.setBackground(
                new Color(240, 240, 240)
                );

        formPanel.setBorder(
                new LineBorder(Color.GRAY)
                );

        dialog.add(formPanel);

        String[] labels = {

            "Date of Record",
                    "Time In",
                    "Time Out",
                    "Overtime Hours",
                    "Dispute Category",
                    "Evidence",
                    "Description/\nRemarks"
                };

        int y = 0;

        // =========================
        // COMPONENTS
        // =========================

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

        JComboBox<String> recordMonthBox =
                new JComboBox<>(monthOptions);

        JComboBox<Integer> recordDayBox =
                new JComboBox<>();

        JComboBox<Integer> recordYearBox =
                new JComboBox<>(
                new Integer[]{
                    currentYear - 1,
                    currentYear
                }
                );

        recordMonthBox.setSelectedIndex(
                today.getMonthValue() - 1
                );

        recordYearBox.setSelectedItem(
                today.getYear()
                );

        updateDayChoices(
                recordMonthBox,
                recordDayBox,
                recordYearBox,
                today.getDayOfMonth()
                );

        recordDayBox.setSelectedItem(
                today.getDayOfMonth()
                );

        recordMonthBox.addActionListener(
                event -> updateDayChoices(
                recordMonthBox,
                recordDayBox,
                recordYearBox,
                today.getDayOfMonth()
                )
                );

        recordYearBox.addActionListener(
                event -> updateDayChoices(
                recordMonthBox,
                recordDayBox,
                recordYearBox,
                today.getDayOfMonth()
                )
                );

        JComboBox<String> timeInBox =
                new JComboBox<>(

                new String[] {

                    "08:00 AM",
                    "08:30 AM",
                    "09:00 AM",
                    "09:30 AM"
                }
                );

        timeInBox.setEditable(true);

        JComboBox<String> timeOutBox =
                new JComboBox<>(

                new String[] {

                    "05:00 PM",
                    "05:30 PM",
                    "06:00 PM",
                    "06:30 PM"
                }
                );

        timeOutBox.setEditable(true);

        JTextField overtimeField =
                new JTextField();

        JComboBox<String> categoryBox =
                new JComboBox<>(

                new String[] {

                    "Missing Time In",
                    "Missing Time Out",
                    "Incorrect Overtime",
                    "Incorrect Salary",
                    "Other"
                }
                );

        JTextField fileField =
                new JTextField();

        fileField.setEditable(false);

        JButton uploadButton =
                new JButton("Choose File");

        JTextArea remarksArea =
                new JTextArea();

        remarksArea.setLineWrap(true);

        remarksArea.setWrapStyleWord(true);

        JScrollPane remarksScroll =
                new JScrollPane(remarksArea);

        // =========================
        // FORM ROWS
        // =========================

        for (int i = 0; i < labels.length; i++) {

            int rowHeight =
                    (i == 6) ? 88 : 42;

            // =========================
            // LEFT PANEL
            // =========================

            JPanel left =
                    new JPanel();

            left.setLayout(null);

            left.setBounds(
                    0,
                    y,
                    185,
                    rowHeight
                    );

            left.setBackground(
                    new Color(240, 240, 240)
                    );

            left.setBorder(
                    new LineBorder(Color.BLACK)
                    );

            JLabel label =
                    new JLabel(
                    "<html>"
                    + labels[i]
                    + "</html>"
                    );

            label.setBounds(
                    12,
                    0,
                    150,
                    rowHeight
                    );

            label.setVerticalAlignment(
                    SwingConstants.CENTER
                    );

            label.setFont(
                    new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    15
                    )
                    );

            left.add(label);

            formPanel.add(left);

            // =========================
            // RIGHT PANEL
            // =========================

            JPanel right =
                    new JPanel();

            right.setLayout(null);

            right.setBounds(
                    185,
                    y,
                    305,
                    rowHeight
                    );

            right.setBackground(
                    new Color(240, 240, 240)
                    );

            right.setBorder(
                    new LineBorder(Color.BLACK)
                    );

            formPanel.add(right);

            int inputY =
                    (rowHeight - 28) / 2;

            switch (i) {

                // =========================
                // DATE
                // =========================

                case 0:

                    recordMonthBox.setBounds(
                            18,
                            inputY,
                            122,
                            28
                            );

                    right.add(recordMonthBox);

                    recordDayBox.setBounds(
                            145,
                            inputY,
                            58,
                            28
                            );

                    right.add(recordDayBox);

                    recordYearBox.setBounds(
                            208,
                            inputY,
                            78,
                            28
                            );

                    right.add(recordYearBox);

                    break;

                    // =========================
                    // TIME IN
                    // =========================

                case 1:

                    timeInBox.setBounds(
                            42,
                            inputY,
                            220,
                            28
                            );

                    right.add(timeInBox);

                    break;

                    // =========================
                    // TIME OUT
                    // =========================

                case 2:

                    timeOutBox.setBounds(
                            42,
                            inputY,
                            220,
                            28
                            );

                    right.add(timeOutBox);

                    break;

                    // =========================
                    // OVERTIME
                    // =========================

                case 3:

                    overtimeField.setBounds(
                            42,
                            inputY,
                            220,
                            28
                            );

                    right.add(overtimeField);

                    break;

                    // =========================
                    // CATEGORY
                    // =========================

                case 4:

                    categoryBox.setBounds(
                            42,
                            inputY,
                            220,
                            28
                            );

                    right.add(categoryBox);

                    break;

                    // =========================
                    // FILE
                    // =========================

                case 5:

                    fileField.setBounds(
                            42,
                            inputY,
                            140,
                            28
                            );

                    right.add(fileField);

                    uploadButton.setBounds(
                            188,
                            inputY,
                            74,
                            28
                            );

                    right.add(uploadButton);

                    break;

                    // =========================
                    // REMARKS
                    // =========================

                case 6:

                    remarksScroll.setBounds(
                            20,
                            10,
                            265,
                            65
                            );

                    right.add(remarksScroll);

                    break;
            }

            y += rowHeight;
        }

        // =========================
        // FILE CHOOSER EVENT
        // =========================

        uploadButton.addActionListener(e -> {

                    JFileChooser chooser =
                    new JFileChooser();

                    int result =
                    chooser.showOpenDialog(dialog);

                    if (result == JFileChooser.APPROVE_OPTION) {

                        fileField.setText(
                        chooser
                        .getSelectedFile()
                        .getName()
                        );
                    }
                });

        // =========================
        // SUBMIT BUTTON
        // =========================

        JButton submitButton =
                new RoundedButton(
                "Submit",
                new Color(20, 90, 210),
                Color.WHITE
                );

        submitButton.setBounds(
                100,
                525,
                120,
                42
                );

        dialog.add(submitButton);

        // =========================
        // CANCEL BUTTON
        // =========================

        JButton cancelButton =
                new RoundedButton(
                "Cancel",
                new Color(255, 90, 90),
                Color.WHITE
                );

        cancelButton.setBounds(
                310,
                525,
                120,
                42
                );

        dialog.add(cancelButton);

        // =========================
        // SUBMIT EVENT
        // =========================

        submitButton.addActionListener(e -> {

                    try {

                        String overtime =
                        overtimeField.getText().trim();

                        String remarks =
                        remarksArea.getText().trim();

                        LocalDate selectedRecordDate =
                        buildSelectedDate(
                        recordMonthBox,
                        recordDayBox,
                        recordYearBox
                        );

                        if(selectedRecordDate.isAfter(LocalDate.now())){

                            JOptionPane.showMessageDialog(

                            dialog,

                            "Date of Record cannot be in the future.",

                            "Invalid Date",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        String timeInText =
                        timeInBox.getSelectedItem().toString().trim();

                        String timeOutText =
                        timeOutBox.getSelectedItem().toString().trim();

                        LocalTime selectedTimeIn;
                        LocalTime selectedTimeOut;

                        try{

                            DateTimeFormatter disputeTimeFormat =
                            DateTimeFormatter.ofPattern("hh:mm a");

                            selectedTimeIn =
                            LocalTime.parse(
                            timeInText.toUpperCase(),
                            disputeTimeFormat
                            );

                            selectedTimeOut =
                            LocalTime.parse(
                            timeOutText.toUpperCase(),
                            disputeTimeFormat
                            );

                        } catch(Exception ex){

                            JOptionPane.showMessageDialog(
                            dialog,
                            "Please enter time in HH:MM AM/PM format.",
                            "Invalid Time",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if(!selectedTimeOut.isAfter(selectedTimeIn)){

                            JOptionPane.showMessageDialog(
                            dialog,
                            "Time Out must be later than Time In.",
                            "Invalid Time",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if(selectedRecordDate.equals(LocalDate.now())
                        && (selectedTimeIn.isAfter(LocalTime.now())
                        || selectedTimeOut.isAfter(LocalTime.now()))){

                            JOptionPane.showMessageDialog(
                            dialog,
                            "Dispute time cannot be in the future.",
                            "Invalid Time",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        String recordDate =
                        String.format(
                        "%02d/%02d/%d",
                        selectedRecordDate.getMonthValue(),
                        selectedRecordDate.getDayOfMonth(),
                        selectedRecordDate.getYear()
                        );

                        String employeeName =
                        employee.getFirstName()
                        + " "
                        + employee.getLastName();

                        if (overtime.isEmpty()) {

                            JOptionPane.showMessageDialog(

                            dialog,

                            "Please enter overtime hours.",

                            "Missing Information",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if(!overtime.matches("\\d+(\\.\\d+)?")){

                            JOptionPane.showMessageDialog(

                            dialog,

                            "Overtime Hours must be a number, such as 1 or 1.5.",

                            "Invalid Overtime Hours",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if (remarks.isEmpty()) {

                            JOptionPane.showMessageDialog(

                            dialog,

                            "Please enter remarks.",

                            "Missing Information",

                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        EmployeeDataManager manager =
                        new EmployeeDataManager();

                        boolean saved =
                        manager.createPayrollDisputeRequest(
                        employee.getEmployeeNumber(),
                        employeeName,
                        selectedPeriod,
                        recordDate,
                        timeInText,
                        timeOutText,
                        overtime,
                        categoryBox.getSelectedItem().toString(),
                        fileField.getText().trim(),
                        remarks
                        );

                        if(!saved){

                            JOptionPane.showMessageDialog(
                            dialog,
                            "Unable to submit dispute. Please try again.",
                            "Submission Error",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        JOptionPane.showMessageDialog(

                        dialog,

                        "Your dispute has been submitted successfully.",

                        "Dispute Submitted",

                        JOptionPane.INFORMATION_MESSAGE
                        );

                        dialog.dispose();

                    } catch (Exception ex) {

                        JOptionPane.showMessageDialog(

                        dialog,

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

                    dialog.dispose();

                });

        dialog.setVisible(true);
    }

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

        for(int day = 1; day <= maxDay; day++){

            dayBox.addItem(day);
        }

        dayBox.setSelectedItem(
                Math.min(
                selectedDay,
                maxDay
                )
                );
    }

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
