package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TimeOffRequestPanel extends JPanel {

    private JTable leaveTable;
    private DefaultTableModel tableModel;
    private Employee employee;
    private static final String LEAVE_REQUEST_FILE =
            "src/main/resources/LeaveRequests.csv";

    public TimeOffRequestPanel(Employee employee) {

        this.employee = employee;

        setLayout(null);
        setBackground(new Color(227, 234, 231));

        // =========================
        // BREADCRUMB
        // =========================

        JLabel breadcrumb =
                new JLabel("Home > Time Off Request");

        breadcrumb.setBounds(40, 40, 250, 20);
        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        breadcrumb.setForeground(new Color(120, 120, 120));

        add(breadcrumb);

        // =========================
        // TITLE
        // =========================

        JLabel title =
                new JLabel("Time Off Request");

        title.setBounds(40, 85, 350, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));

        add(title);

        // =========================
        // NEW REQUEST BUTTON
        // =========================

        JButton requestButton =
                createRoundedButton(
                "+ New Leave Request",
                new Color(36, 150, 195),
                Color.WHITE
                );

        requestButton.setBounds(40, 135, 240, 42);

        add(requestButton);

        // =========================
        // CREDIT CARD
        // =========================

        JPanel creditCard = new JPanel();

        creditCard.setLayout(null);
        creditCard.setBounds(40, 195, 700, 120);
        creditCard.setBackground(Color.WHITE);

        creditCard.setBorder(
                new LineBorder(
                new Color(220, 220, 220),
                1,
                true
                )
                );

        add(creditCard);

        addCreditSection(
                creditCard,
                "Total Credits",
                "15 days",
                20
                );

        addCreditSection(
                creditCard,
                "Used Credits",
                "4 days",
                200
                );

        addCreditSection(
                creditCard,
                "Pending",
                "4 days",
                380
                );

        addCreditSection(
                creditCard,
                "Remaining Balance",
                "7 days",
                560
                );

        // =========================
        // TABLE
        // =========================

        String[] columns = {

            "Leave Type",
                    "Start Date",
                    "End Date",
                    "Duration",
                    "Status",
                    "Validated By"
                };

        tableModel =
                new DefaultTableModel(columns, 0);

        leaveTable = new JTable(tableModel);

        leaveTable.setRowHeight(32);
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        leaveTable.setBackground(new Color(245, 245, 245));

        leaveTable.setSelectionBackground(
                new Color(210, 210, 210)
                );

        leaveTable.setShowGrid(false);
        leaveTable.setIntercellSpacing(new Dimension(0, 0));
        leaveTable.setFillsViewportHeight(true);

        leaveTable.setDefaultRenderer(

                Object.class,

                new DefaultTableCellRenderer() {

                    @Override
            public Component getTableCellRendererComponent(

                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
                    ) {

                        Component c =
                        super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                        );

                        if (!isSelected) {

                            if (row % 2 == 0) {

                                c.setBackground(
                                new Color(245, 245, 245)
                                );

                            } else {

                                c.setBackground(
                                new Color(220, 220, 220)
                                );
                            }
                        }

                        setHorizontalAlignment(
                        SwingConstants.CENTER
                        );

                        return c;
                    }
                }
                );

        // =========================
        // HEADER STYLE
        // =========================

        leaveTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
                );

        leaveTable.getTableHeader().setBackground(
                new Color(210, 210, 210)
                );

        leaveTable.getTableHeader().setForeground(Color.BLACK);
        leaveTable.getTableHeader().setOpaque(true);
        leaveTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane =
                new JScrollPane(leaveTable);

        scrollPane.setBounds(40, 370, 700, 230);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
                );

        add(scrollPane);

        System.out.println("Loading leave requests...");
        loadLeaveRequests();

        // =========================
        // BUTTON EVENT
        // =========================

        requestButton.addActionListener(e -> {

                    LeaveRequestPanel dialog =
                    new LeaveRequestPanel(employee);

                    dialog.setVisible(true);

                    loadLeaveRequests();

                });
    }

    // =========================
    // CREDIT SECTION
    // =========================

    private void addCreditSection(
            JPanel parent,
            String title,
            String value,
            int x
            ) {

        JLabel titleLabel = new JLabel(title);

        titleLabel.setBounds(x, 20, 150, 25);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        parent.add(titleLabel);

        JLabel valueLabel = new JLabel(value);

        valueLabel.setBounds(x, 55, 150, 40);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));

        parent.add(valueLabel);

        if (x != 560) {

            JSeparator separator =
                    new JSeparator(SwingConstants.VERTICAL);

            separator.setBounds(x + 160, 18, 5, 80);

            parent.add(separator);
        }
    }

    // =========================
    // BUTTON STYLE
    // =========================

    private void configureDateSpinner(
            JSpinner spinner
            ) {

        JSpinner.DateEditor editor =
                (JSpinner.DateEditor) spinner.getEditor();

        editor.getTextField().setHorizontalAlignment(
                SwingConstants.CENTER
                );

        editor.getTextField().setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
                );

        spinner.addMouseWheelListener(e -> {

                    if(e.getWheelRotation() < 0){

                        shiftDate(spinner, 1);

                    }else{

                        shiftDate(spinner, -1);
                    }
                });
    }

    private void shiftDate(
            JSpinner spinner,
            int days
            ) {

        java.util.Calendar calendar =
                java.util.Calendar.getInstance();

        calendar.setTime(
                (java.util.Date) spinner.getValue()
                );

        calendar.add(
                java.util.Calendar.DAY_OF_MONTH,
                days
                );

        spinner.setValue(
                calendar.getTime()
                );
    }

    private void showCalendarPicker(
            JDialog parent,
            JSpinner targetSpinner,
            String title
            ) {

        JDialog calendarDialog =
                new JDialog(
                parent,
                title,
                true
                );

        calendarDialog.setSize(360, 330);
        calendarDialog.setLayout(new BorderLayout());
        calendarDialog.setLocationRelativeTo(parent);
        calendarDialog.setResizable(false);

        java.util.Calendar calendar =
                java.util.Calendar.getInstance();

        calendar.setTime(
                (java.util.Date) targetSpinner.getValue()
                );

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        JButton previousMonthButton =
                new JButton("<");

        JButton nextMonthButton =
                new JButton(">");

        JLabel monthLabel =
                new JLabel(
                "",
                SwingConstants.CENTER
                );

        monthLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
                );

        headerPanel.add(
                previousMonthButton,
                BorderLayout.WEST
                );

        headerPanel.add(
                monthLabel,
                BorderLayout.CENTER
                );

        headerPanel.add(
                nextMonthButton,
                BorderLayout.EAST
                );

        calendarDialog.add(
                headerPanel,
                BorderLayout.NORTH
                );

        JPanel daysPanel =
                new JPanel(new GridLayout(0, 7, 4, 4));

        daysPanel.setBorder(
                BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
                )
                );

        calendarDialog.add(
                daysPanel,
                BorderLayout.CENTER
                );

        Runnable[] refreshCalendar =
                new Runnable[1];

        refreshCalendar[0] = () -> {

            daysPanel.removeAll();

            java.text.SimpleDateFormat monthFormat =
                    new java.text.SimpleDateFormat("MMMM yyyy");

            monthLabel.setText(
                    monthFormat.format(calendar.getTime())
                    );

            String[] dayNames = {
                "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    };

            for(String dayName : dayNames){

                JLabel dayLabel =
                        new JLabel(
                        dayName,
                        SwingConstants.CENTER
                        );

                dayLabel.setFont(
                        new Font("Segoe UI", Font.BOLD, 12)
                        );

                daysPanel.add(dayLabel);
            }

            java.util.Calendar monthCalendar =
                    (java.util.Calendar) calendar.clone();

            monthCalendar.set(
                    java.util.Calendar.DAY_OF_MONTH,
                    1
                    );

            int firstDayOfWeek =
                    monthCalendar.get(
                    java.util.Calendar.DAY_OF_WEEK
                    );

            int daysInMonth =
                    monthCalendar.getActualMaximum(
                    java.util.Calendar.DAY_OF_MONTH
                    );

            for(int i = 1; i < firstDayOfWeek; i++){

                daysPanel.add(new JLabel(""));
            }

            for(int day = 1; day <= daysInMonth; day++){

                JButton dayButton =
                        new JButton(String.valueOf(day));

                dayButton.setFocusPainted(false);

                int selectedDay =
                        day;

                dayButton.addActionListener(e -> {

                            calendar.set(
                            java.util.Calendar.DAY_OF_MONTH,
                            selectedDay
                            );

                            targetSpinner.setValue(
                            calendar.getTime()
                            );

                            calendarDialog.dispose();
                        });

                daysPanel.add(dayButton);
            }

            daysPanel.revalidate();
            daysPanel.repaint();
        };

        previousMonthButton.addActionListener(e -> {

                    calendar.add(
                    java.util.Calendar.MONTH,
                    -1
                    );

                    refreshCalendar[0].run();
                });

        nextMonthButton.addActionListener(e -> {

                    calendar.add(
                    java.util.Calendar.MONTH,
                    1
                    );

                    refreshCalendar[0].run();
                });

        refreshCalendar[0].run();

        calendarDialog.setVisible(true);
    }

    private void loadLeaveRequests() {

        tableModel.setRowCount(0);

        File file =
                new File(LEAVE_REQUEST_FILE);
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(
                new FileReader(file))) {

            reader.readLine(); // Skip header

                    String line;

            while ((line = reader.readLine()) != null) {

                System.out.println("LINE: " + line);
                String[] data = parseCSVLine(line);
                System.out.println("Columns: " + data.length);

                if (data.length < 13) {
                    continue;
                }

                if (!data[1].trim().equals(employee.getEmployeeNumber())) {
                    System.out.println("CSV Employee: '" + data[1] + "'");
                    System.out.println("Logged Employee: '" + employee.getEmployeeNumber() + "'");
                    continue;
                }

                System.out.println("ADDING ROW");
                tableModel.addRow(
                        new Object[]{

                            data[4],
                            data[5], // Start Date
                            data[6], // End Date
                            data[7], // Duration
                            data[10], // Status
                            data[11]

                        }
                        );
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private boolean saveLeaveRequest(
            String leaveType,
            String reason,
            String startDate,
            String endDate,
            String duration
            ) {

        try {

            File file =
                    new File(LEAVE_REQUEST_FILE);

            boolean fileExists =
                    file.exists();

            try(FileWriter writer =
                    new FileWriter(
                    file,
                    true
                    )){

                if(!fileExists){

                    writer.write(
                            "Request ID,Employee ID,Employee Name,Supervisor,Leave Type,Start Date,End Date,Duration,Reason,Submitted Date,Status,Reviewed By,Review Date"
                            );
                }

                String employeeName =
                        employee.getFirstName()
                        + " "
                        + employee.getLastName();

                String[] row = {
                    String.valueOf(System.currentTimeMillis()),
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

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    private String[] parseCSVLine(String line) {

        return line.split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                -1
                );
    }

    private String formatCSVLine(String[] data) {

        StringBuilder line =
                new StringBuilder();

        for(int i = 0; i < data.length; i++){

            String value =
                    data[i] == null ? "" : data[i].trim();

            if(value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")){

                value =
                        "\""
                        + value.replace("\"", "\"\"")
                        + "\"";
            }

            line.append(value);

            if(i < data.length - 1){
                line.append(",");
            }
        }

        return line.toString();
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

    private JButton createRoundedButton(
            String text,
            Color bgColor,
            Color textColor
            ) {

        JButton button = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                        );

                g2.setColor(bgColor);

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        25,
                        25
                        );

                super.paintComponent(g);
            }
        };

        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
