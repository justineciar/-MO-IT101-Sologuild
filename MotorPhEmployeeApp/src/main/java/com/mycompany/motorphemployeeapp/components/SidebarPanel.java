package com.mycompany.motorphemployeeapp.components;

import com.mycompany.motorphemployeeapp.Employee;
import com.mycompany.motorphemployeeapp.OvertimeAccess;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class SidebarPanel extends JPanel {

    // =========================
    // VARIABLES
    // =========================
    private final Font regularFont;
    private final Font boldFont;

    // Common to all users
    private final JButton homeButton;
    private final JButton dtrButton;
    private final JButton timesheetButton;
    private final JButton leaveButton;
    private final JButton payslipButton;
    private final JButton myOvertimeButton;
    private final JButton logoutButton;

    // Role-specific
    private JButton employeeListButton;
    private JButton requestsButton;
    private JButton payrollButton;
    private JButton approvedOvertimeButton;
    private JButton myDisputesButton;

    private JButton activeButton;

    // =========================
    // CONSTRUCTOR
    // =========================

    public SidebarPanel(Employee employee) {

        regularFont =
                new Font(
                "Segoe UI",
                Font.PLAIN,
                16
                );

        boldFont =
                new Font(
                "Segoe UI",
                Font.BOLD,
                16
                );

        setBounds(
                0,
                0,
                220,
                700
                );

        setBackground(
                new Color(248,248,248)
                );

        setLayout(null);

        // =========================
        // LOGO
        // =========================

        ImageIcon logoIcon =
                new ImageIcon(
                getClass().getResource(
                "/Images/logo.png"
                )
                );

        Image scaledLogo =
                logoIcon.getImage().getScaledInstance(
                70,
                70,
                Image.SCALE_SMOOTH
                );

        JLabel logoLabel =
                new JLabel(
                new ImageIcon(scaledLogo)
                );

        logoLabel.setBounds(
                15,
                25,
                70,
                70
                );

        add(logoLabel);

        // =========================
        // COMPANY LABEL
        // =========================

        JLabel companyLabel =
                new JLabel("MotorPH");

        companyLabel.setBounds(
                85,
                38,
                200,
                40
                );

        companyLabel.setFont(
                boldFont.deriveFont(28f)
                );

        add(companyLabel);

        // =========================
        // SIDEBAR BUTTONS
        // =========================

        int y = 105;
        final int GAP = 55;

        homeButton = createSidebarButton("Home", y);
        y += GAP;

        dtrButton = createSidebarButton("Daily Time Record", y);
        y += GAP;

        timesheetButton = createSidebarButton("Timesheet", y);
        y += GAP;

        leaveButton = createSidebarButton("Time Off Request", y);
        y += GAP;

        payslipButton = createSidebarButton("My Payslip", y);
        y += GAP;

        myOvertimeButton = createSidebarButton("My Overtime", y);
        y += GAP;

        myDisputesButton = createSidebarButton("My Disputes", y);
        y += GAP;

        sidebarAdd(homeButton);
        sidebarAdd(dtrButton);
        sidebarAdd(timesheetButton);
        sidebarAdd(leaveButton);
        sidebarAdd(payslipButton);
        sidebarAdd(myOvertimeButton);
        sidebarAdd(myDisputesButton);

        String position = employee.getPosition().toLowerCase();

        // Payroll users
        if (position.contains("payroll")) {

            employeeListButton = null;

            payrollButton = createSidebarButton("Payroll", y);
            y += GAP;

            requestsButton =
                    createSidebarButton("Requests", y);
            y += GAP;

            approvedOvertimeButton = null;

            sidebarAdd(payrollButton);
            sidebarAdd(requestsButton);

            // All HR users
        } else if (position.contains("hr")) {

            payrollButton = null;
            approvedOvertimeButton = null;

            employeeListButton =
                    createSidebarButton("Employee List", y);
            y += GAP;

            sidebarAdd(employeeListButton);

            requestsButton =
                    createSidebarButton("Requests", y);
            y += GAP;

            sidebarAdd(requestsButton);

            // Managers / Team Leaders
        } else if (OvertimeAccess.canReviewOvertime(employee)) {

            payrollButton = null;
            approvedOvertimeButton = null;

            employeeListButton = null;

            requestsButton =
                    createSidebarButton("Requests", y);
            y += GAP;

            sidebarAdd(requestsButton);

            // Regular employees
        } else {

            payrollButton = null;
            approvedOvertimeButton = null;

            employeeListButton = null;
            requestsButton = null;
        }

        // =========================
        // LOGOUT BUTTON
        // =========================

        logoutButton =
                new JButton("LOG OUT"){

            @Override
            protected void paintComponent(
                    Graphics g
                    ){

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                        );

                g2.setColor(
                        new Color(255,229,138)
                        );

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth()-1,
                        getHeight()-1,
                        25,
                        25
                        );

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(
                    Graphics g
                    ){

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setColor(
                        new Color(233,185,58)
                        );

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth()-1,
                        getHeight()-1,
                        25,
                        25
                        );
            }
        };

        logoutButton.setBounds(
                30,
                610,
                135,
                42
                );

        logoutButton.setFont(
                boldFont.deriveFont(16f)
                );

        logoutButton.setForeground(Color.BLACK);

        logoutButton.setFocusPainted(false);

        logoutButton.setContentAreaFilled(false);

        logoutButton.setBorderPainted(false);

        logoutButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
                );

        add(logoutButton);
    }

    private void sidebarAdd(JButton button){

        if(button != null){

            add(button);
        }
    }

    // =========================
    // SIDEBAR BUTTON
    // =========================

    private JButton createSidebarButton(
            String text,
            int y
            ){

        JButton button =
                new JButton(text){

            @Override
            protected void paintComponent(
                    Graphics g
                    ){

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                        );

                if(this == activeButton){

                    g2.setColor(
                            new Color(225,225,225)
                            );

                    g2.fillRoundRect(
                            10,
                            0,
                            getWidth()-20,
                            getHeight(),
                            18,
                            18
                            );
                }

                super.paintComponent(g);
            }
        };

        button.setBounds(
                10,
                y,
                200,
                55
                );

        button.setFont(
                regularFont.deriveFont(17f)
                );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
                );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                0,
                25,
                0,
                0
                )
                );

        button.setFocusPainted(false);

        button.setContentAreaFilled(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
                );

        return button;
    }

    // =========================
    // ACTIVE BUTTON
    // =========================

    public void setActiveButton(
            JButton clickedButton
            ){

        activeButton = clickedButton;

        repaint();
    }

    // =========================
    // GETTERS
    // =========================

    public JButton getHomeButton() {
        return homeButton;
    }

    public JButton getDtrButton() {
        return dtrButton;
    }

    public JButton getTimesheetButton() {
        return timesheetButton;
    }

    public JButton getLeaveButton() {
        return leaveButton;
    }

    public JButton getPayslipButton() {
        return payslipButton;
    }

    public JButton getEmployeeListButton() {
        return employeeListButton;
    }

    public JButton getRequestsButton() {
        return requestsButton;
    }

    public JButton getPayrollButton() {
        return payrollButton;
    }

    public JButton getApprovedOvertimeButton() {
        return approvedOvertimeButton;
    }

    public JButton getMyOvertimeButton() {
        return myOvertimeButton;
    }

    public JButton getMyDisputesButton() {
        return myDisputesButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}
