package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DailyTimeRecordPanel extends JPanel {
    private JLabel currentTimeLabel;
    private JLabel clockInValue;
    private JLabel breakValue;
    private JLabel hoursValue;
    private JButton clockInButton;
    private JButton breakButton;
    private Timer breakTimer;
    private JPanel topCard;
    private Employee employee;
    private AttendanceManager attendanceManager;

    public DailyTimeRecordPanel(Employee employee) {

        this.employee = employee;
        attendanceManager = new AttendanceManager();

        initializePanel();
        createHeader();
        createTopCard();
        createStatusCard();
        createTaskSection();

        // =========================
        // CLOCK BUTTON EVENT
        // =========================

        attendanceManager.restoreClockSession(employee);

        if (attendanceManager.isClockedIn()) {

            clockInButton.setText("Clock Out");
            breakButton.setEnabled(true);

            clockInValue.setText(
                    attendanceManager.getClockInTime()
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
                    );

            updateBreakDisplay();

            if (attendanceManager.isBreakStarted()) {
                breakButton.setText("Stop Meal Break");
                startBreakTimer();
            }
        }

        if (attendanceManager.hasCompletedAttendanceToday(employee)) {

            clockInButton.setText("Clocked Out");
            clockInButton.setEnabled(false);
            breakButton.setEnabled(false);
        }

        clockInButton.addActionListener(e -> handleClockButton());

        // =========================
        // BREAK BUTTON EVENT
        // =========================

        breakButton.addActionListener(e -> handleBreakButton());
    }

    private void initializePanel() {

        setBounds(
                0,
                0,
                780,
                700
                );

        setBackground(
                new Color(227, 234, 231)
                );

        setLayout(null);

    }

    private void createHeader() {
        // =========================
        // BREADCRUMB
        // =========================

        JLabel breadcrumb = new JLabel("Home > Daily Time Record");

        breadcrumb.setBounds(40, 45, 250, 20);
        breadcrumb.setForeground(new Color(120, 120, 120));
        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        add(breadcrumb);

        // =========================
        // TITLE
        // =========================

        JLabel title = new JLabel("Daily Time Record");

        title.setBounds(40, 90, 350, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));

        add(title);

        // =========================
        // CURRENT TIME
        // =========================

        currentTimeLabel = new JLabel();

        currentTimeLabel.setBounds(560, 95, 200, 25);
        currentTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        add(currentTimeLabel);

        Timer timer = new Timer(
                1000,
                e -> updateCurrentTime()
                );

        timer.start();

        updateCurrentTime();
    }

    private void createTopCard() {
        // =========================
        // TOP CARD
        // =========================

        topCard = new JPanel();

        topCard.setLayout(null);
        topCard.setBounds(40, 150, 700, 90);
        topCard.setBackground(Color.WHITE);

        topCard.setBorder(
                new LineBorder(
                new Color(220, 220, 220),
                1,
                true
                )
                );

        add(topCard);

        // =========================
        // DATE
        // =========================

        LocalDate now = LocalDate.now();

        JLabel monthLabel = new JLabel(
                now.getMonth().toString().substring(0, 3)
                );

        monthLabel.setBounds(28, 10, 80, 25);
        monthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        topCard.add(monthLabel);

        JLabel dayLabel = new JLabel(
                String.valueOf(now.getDayOfMonth())
                );

        dayLabel.setBounds(35, 38, 80, 35);
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));

        topCard.add(dayLabel);

        JSeparator separator =
                new JSeparator(SwingConstants.VERTICAL);

        separator.setBounds(100, 12, 5, 65);

        topCard.add(separator);

        // =========================
        // BREAK BUTTON
        // =========================

        breakButton = createRoundedButton(
                "Start Meal Break",
                new Color(144, 238, 144),
                new Color(144, 238, 144),
                new Color(40, 40, 40)
                );

        breakButton.setBounds(130, 24, 185, 34);
        breakButton.setEnabled(false);

        topCard.add(breakButton);

        // =========================
        // CLOCK BUTTON
        // =========================

        clockInButton = createRoundedButton(
                "Clock In",
                new Color(70, 130, 180),
                new Color(70, 130, 180),
                Color.WHITE
                );

        clockInButton.setBounds(350, 24, 185, 34);

        topCard.add(clockInButton);
    }

    private void createStatusCard() {
        // =========================
        // STATUS CARD
        // =========================

        JPanel statusCard = new JPanel();

        statusCard.setLayout(null);
        statusCard.setBounds(40, 260, 700, 130);
        statusCard.setBackground(Color.WHITE);

        statusCard.setBorder(
                new LineBorder(
                new Color(220, 220, 220),
                1,
                true
                )
                );

        add(statusCard);

        JLabel clockLabel = new JLabel("Clock In");

        clockLabel.setBounds(20, 25, 120, 25);
        clockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        statusCard.add(clockLabel);

        clockInValue = new JLabel("-");

        clockInValue.setBounds(20, 65, 180, 35);
        clockInValue.setFont(new Font("Segoe UI", Font.PLAIN, 26));

        statusCard.add(clockInValue);

        JSeparator separator2 =
                new JSeparator(SwingConstants.VERTICAL);

        separator2.setBounds(215, 20, 5, 90);

        statusCard.add(separator2);

        JLabel breakLabel = new JLabel("Break");

        breakLabel.setBounds(245, 25, 120, 25);
        breakLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        statusCard.add(breakLabel);

        breakValue = new JLabel("0:00 mins");

        breakValue.setBounds(245, 65, 180, 35);
        breakValue.setFont(new Font("Segoe UI", Font.PLAIN, 26));

        statusCard.add(breakValue);

        JSeparator separator3 =
                new JSeparator(SwingConstants.VERTICAL);

        separator3.setBounds(475, 20, 5, 90);

        statusCard.add(separator3);

        JLabel hoursLabel = new JLabel("Working Hours");

        hoursLabel.setBounds(500, 25, 180, 25);
        hoursLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        statusCard.add(hoursLabel);

        hoursValue = new JLabel("0:00 hours");

        hoursValue.setBounds(500, 65, 180, 35);
        hoursValue.setFont(new Font("Segoe UI", Font.PLAIN, 26));

        statusCard.add(hoursValue);
    }

    private void createTaskSection() {
        // =========================
        // TASK TITLE
        // =========================

        JLabel taskTitle = new JLabel("Your Tasks for Today");

        taskTitle.setBounds(40, 405, 350, 35);
        taskTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        add(taskTitle);

        JLabel scheduleLabel =
                new JLabel("Work Schedule: 9:00 AM - 5:00 PM");

        scheduleLabel.setBounds(40, 440, 350, 25);
        scheduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        add(scheduleLabel);

        // =========================
        // TASK CARD
        // =========================

        JPanel taskCard = new JPanel();

        taskCard.setLayout(null);
        taskCard.setBounds(40, 475, 700, 175);
        taskCard.setBackground(Color.WHITE);

        taskCard.setBorder(
                new LineBorder(
                new Color(220, 220, 220),
                1,
                true
                )
                );

        add(taskCard);

        JLabel timeHeader = new JLabel(
                "Time",
                SwingConstants.CENTER
                );

        timeHeader.setBounds(20, 14, 220, 25);
        timeHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));

        taskCard.add(timeHeader);

        JLabel taskHeader = new JLabel(
                "Task/Activity",
                SwingConstants.CENTER
                );

        taskHeader.setBounds(285, 14, 320, 25);
        taskHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));

        taskCard.add(taskHeader);

        JSeparator taskSeparator =
                new JSeparator(SwingConstants.VERTICAL);

        taskSeparator.setBounds(245, 15, 5, 145);

        taskCard.add(taskSeparator);

        String[][] tasks = {

            {
                "9:00 AM - 10:00 AM",
                        "Review daily emails and schedule updates"
                    },

            {
                "10:00 AM - 11:00 AM",
                        "Work on assigned project tasks"
                    },

            {
                "11:00 AM - 12:00 PM",
                        "Lunch Break"
                    },

            {
                "12:00 PM - 3:00 PM",
                        "Team collaboration and progress review"
                    },

            {
                "3:00 PM - 5:00 PM",
                        "Finalize reports and documentation"
                    }
        };

        int rowY = 35;

        LocalTime currentTime = LocalTime.now();

        for (int i = 0; i < tasks.length; i++) {

            JPanel row = new JPanel();

            row.setLayout(null);
            row.setBounds(15, rowY, 660, 26);

            boolean highlight = false;

            switch (i) {

                case 0:

                    highlight =
                            currentTime.isAfter(LocalTime.of(9, 0))
                            &&
                            currentTime.isBefore(LocalTime.of(10, 0));

                    break;

                case 1:

                    highlight =
                            currentTime.isAfter(LocalTime.of(10, 0))
                            &&
                            currentTime.isBefore(LocalTime.of(11, 0));

                    break;

                case 2:

                    highlight =
                            currentTime.isAfter(LocalTime.of(11, 0))
                            &&
                            currentTime.isBefore(LocalTime.of(12, 0));

                    break;

                case 3:

                    highlight =
                            currentTime.isAfter(LocalTime.of(12, 0))
                            &&
                            currentTime.isBefore(LocalTime.of(15, 0));

                    break;

                case 4:

                    highlight =
                            currentTime.isAfter(LocalTime.of(15, 0))
                            &&
                            currentTime.isBefore(LocalTime.of(17, 0));

                    break;
            }

            if (highlight) {

                row.setBackground(new Color(244, 233, 198));

            } else {

                row.setBackground(Color.WHITE);
            }

            JLabel timeLabel = new JLabel(
                    tasks[i][0],
                    SwingConstants.CENTER
                    );

            timeLabel.setBounds(0, 1, 230, 24);

            timeLabel.setFont(
                    new Font(
                    "Segoe UI",
                    highlight ? Font.BOLD : Font.PLAIN,
                    16
                    )
                    );

            row.add(timeLabel);

            JLabel taskLabel = new JLabel(
                    tasks[i][1],
                    SwingConstants.CENTER
                    );

            taskLabel.setBounds(250, 1, 390, 24);

            taskLabel.setFont(
                    new Font(
                    "Segoe UI",
                    highlight ? Font.BOLD : Font.PLAIN,
                    16
                    )
                    );

            row.add(taskLabel);

            taskCard.add(row);

            rowY += 26;
        }
    }

    private void handleClockButton() {

        if (!attendanceManager.isClockedIn()) {

            attendanceManager.clockIn(employee);

            breakButton.setEnabled(true);
            clockInButton.setText("Clock Out");

            clockInValue.setText(
                    attendanceManager.getClockInTime()
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
                    );

            JOptionPane.showMessageDialog(
                    null,
                    "You have successfully clocked in.",
                    "Clock In Complete",
                    JOptionPane.INFORMATION_MESSAGE
                    );

        } else {

            boolean success = attendanceManager.clockOut(employee);

            if (success) {

                hoursValue.setText(
                        attendanceManager.getWorkedHours()
                        );

                clockInButton.setEnabled(false);
                breakButton.setEnabled(false);
                clockInButton.setText("Clocked Out");
                breakButton.setText("Start Meal Break");
            }
        }
    }

    private void handleBreakButton() {

        if (!attendanceManager.isClockedIn()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please clock in first."
                    );

            return;
        }

        if (!attendanceManager.isBreakStarted()) {

            attendanceManager.startMealBreak(employee);

            breakButton.setText("Stop Meal Break");

            startBreakTimer();

        } else {

            attendanceManager.stopMealBreak(employee);

            if (attendanceManager.isIdle()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Your meal break exceeded 1 hour.\nYour attendance has been marked as IDLE.",
                        "Idle Status",
                        JOptionPane.WARNING_MESSAGE
                        );
            }

            if (breakTimer != null)
                    breakTimer.stop();

            updateBreakDisplay();

            breakButton.setText("Start Meal Break");
        }
    }

    private void startBreakTimer() {

        if(breakTimer != null){

            breakTimer.stop();
        }

        breakTimer =
                new Timer(
                1000,
                e -> updateBreakDisplay()
                );

        breakTimer.start();
        updateBreakDisplay();
    }

    private void updateBreakDisplay() {

        int totalSeconds = attendanceManager.getTotalBreakSeconds();

        int mins =
                totalSeconds / 60;

        int secs =
                totalSeconds % 60;

        breakValue.setText(
                mins
                + ":"
                + String.format("%02d", secs)
                + " mins"
                );
    }

    // =========================
    // UPDATE CURRENT TIME
    // =========================

    private void updateCurrentTime() {

        currentTimeLabel.setText(
                "Current Time: "
                +
                LocalTime.now().format(
                DateTimeFormatter.ofPattern("hh:mm a")
                )
                );
    }

    // =========================
    // BUTTON DESIGN
    // =========================

    private JButton createRoundedButton(
            String text,
            Color bgColor,
            Color borderColor,
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

                if (!isEnabled()) {

                    g2.setColor(new Color(220, 220, 220));

                } else {

                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        20,
                        20
                        );

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {

                Graphics2D g2 = (Graphics2D) g;

                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        20,
                        20
                        );
            }
        };

        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
