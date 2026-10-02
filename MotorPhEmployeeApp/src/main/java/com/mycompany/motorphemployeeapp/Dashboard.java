package com.mycompany.motorphemployeeapp;

import com.mycompany.motorphemployeeapp.components.HomePanel;
import com.mycompany.motorphemployeeapp.components.SidebarPanel;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    // =========================
    // VARIABLES
    // =========================
    private Employee employee;
    private String rolePrefix;

    private JPanel background;
    private JPanel contentPanel;

    private SidebarPanel sidebar;
    private HomePanel homePanel;

    private JButton homeButton;
    private JButton dtrButton;
    private JButton timesheetButton;
    private JButton leaveButton;
    private JButton payslipButton;
    private JButton requestsButton;
    private JButton logoutButton;
    private JButton employeeListButton;
    private JButton payrollButton;
    private JButton approvedOvertimeButton;
    private JButton myOvertimeButton;
    private JButton myDisputesButton;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Dashboard(Employee employee, String rolePrefix) {

        this.employee = employee;
        this.rolePrefix = rolePrefix;

        initializeFrame();
        createBackground();
        createSidebar();
        createContentPanel();
        createHomePanel();
        registerCommonEvents();
        registerRoleEvents();
        setVisible(true);
    }
    // =========================
    // FRAME
    // =========================

    private void initializeFrame() {

        if (rolePrefix.isEmpty()) {

            setTitle("Employee Dashboard");

        } else {

            setTitle(rolePrefix + " Dashboard");

        }

        setSize(
                1000,
                700
                );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
                );

        setLocationRelativeTo(null);

        setResizable(false);

        setLayout(null);

    }

    // =========================
    // BACKGROUND PANEL
    // =========================

    private void createBackground() {

        background =
                new JPanel();

        background.setBounds(
                0,
                0,
                1000,
                700
                );

        background.setBackground(
                new Color(
                227,
                234,
                231
                )
                );

        background.setLayout(null);

        add(background);

    }

    private void createSidebar() {

        sidebar =
                new SidebarPanel(employee);

        background.add(sidebar);

        homeButton =
                sidebar.getHomeButton();

        dtrButton =
                sidebar.getDtrButton();

        timesheetButton =
                sidebar.getTimesheetButton();

        leaveButton =
                sidebar.getLeaveButton();

        payslipButton =
                sidebar.getPayslipButton();

        myOvertimeButton =
                sidebar.getMyOvertimeButton();

        myDisputesButton =
                sidebar.getMyDisputesButton();

        requestsButton =
                sidebar.getRequestsButton();

        employeeListButton =
                sidebar.getEmployeeListButton();

        payrollButton =
                sidebar.getPayrollButton();

        approvedOvertimeButton =
                sidebar.getApprovedOvertimeButton();

        logoutButton =
                sidebar.getLogoutButton();

    }

    private void createContentPanel() {

        contentPanel =
                new JPanel();

        contentPanel.setBounds(
                220,
                0,
                780,
                700
                );

        contentPanel.setLayout(null);

        contentPanel.setOpaque(false);

        background.add(contentPanel);

    }

    private void createHomePanel() {

        homePanel =
                new HomePanel(
                employee,
                rolePrefix
                );

        homePanel.setBounds(
                0,
                0,
                780,
                700
                );

        contentPanel.add(homePanel);

    }

    // =========================
    // COMMON EVENTS
    // =========================

    private void registerCommonEvents() {

        logoutButton.addActionListener(e -> {

                    new LoginFrame();

                    dispose();

                });

        homeButton.addActionListener(e -> {

                    sidebar.setActiveButton(homeButton);

                    showPanel(homePanel);

                });

        dtrButton.addActionListener(e -> {

                    sidebar.setActiveButton(dtrButton);

                    DailyTimeRecordPanel dtrPanel =
                    new DailyTimeRecordPanel(employee);

                    showPanel(dtrPanel);

                });

        timesheetButton.addActionListener(e -> {

                    sidebar.setActiveButton(timesheetButton);

                    TimeSheetPanel timeSheetPanel =
                    new TimeSheetPanel(employee);

                    showPanel(timeSheetPanel);

                });

        leaveButton.addActionListener(e -> {

                    sidebar.setActiveButton(leaveButton);

                    TimeOffRequestPanel leavePanel =
                    new TimeOffRequestPanel(employee);

                    showPanel(leavePanel);

                });

        payslipButton.addActionListener(e -> {

                    sidebar.setActiveButton(payslipButton);

                    MyPayslipPanel payslipPanel =
                    new MyPayslipPanel(employee);

                    showPanel(payslipPanel);

                });

        if (myOvertimeButton != null) {

            myOvertimeButton.addActionListener(e -> {

                        sidebar.setActiveButton(myOvertimeButton);

                        MyOvertimePanel overtimePanel =
                        new MyOvertimePanel(employee);

                        showPanel(overtimePanel);

                    });

        }

        if (myDisputesButton != null) {

            myDisputesButton.addActionListener(e -> {

                        sidebar.setActiveButton(myDisputesButton);

                        MyDisputesPanel disputesPanel =
                        new MyDisputesPanel(employee);

                        showPanel(disputesPanel);

                    });

        }
    }

    // =========================
    // SHOW PANEL
    // =========================

    private void showPanel(JPanel panel) {

        panel.setBounds(
                0,
                0,
                780,
                700
                );

        contentPanel.removeAll();

        contentPanel.add(panel);

        contentPanel.revalidate();

        contentPanel.repaint();

    }

    // =========================
    // ROLE EVENTS
    // =========================

    private void registerRoleEvents() {

        switch (rolePrefix) {

            case "HR":

                registerHREvents();

                break;

            case "Payroll":

                registerPayrollEvents();

                break;

            default:

                registerEmployeeEvents();

                break;

        }

    }

    private void registerEmployeeEvents() {
        if (requestsButton != null) {

            requestsButton.addActionListener(e -> {

                        sidebar.setActiveButton(requestsButton);

                        SupervisorRequestsPanel requestsPanel =
                        new SupervisorRequestsPanel(employee);

                        showPanel(requestsPanel);

                    });

        }
    }

    private void registerHREvents() {

        if (employeeListButton != null) {

            employeeListButton.addActionListener(e -> {

                        sidebar.setActiveButton(employeeListButton);

                        HREmployeeListPanel employeeListPanel =
                        new HREmployeeListPanel(employee);

                        showPanel(employeeListPanel);

                    });

        }

        if (requestsButton != null) {

            requestsButton.addActionListener(e -> {

                        sidebar.setActiveButton(requestsButton);

                        SupervisorRequestsPanel requestsPanel =
                        new SupervisorRequestsPanel(employee);

                        showPanel(requestsPanel);

                    });

        }

    }

    private void registerPayrollEvents() {

        if (payrollButton != null) {

            payrollButton.addActionListener(e -> {

                        sidebar.setActiveButton(payrollButton);

                        PayrollPanel payrollPanel = new PayrollPanel(employee);

                        showPanel(payrollPanel);

                    });

        }

        if (requestsButton != null) {

            requestsButton.addActionListener(e -> {

                        sidebar.setActiveButton(requestsButton);

                        PayrollRequestsPanel requestsPanel = new PayrollRequestsPanel(employee);

                        showPanel(requestsPanel);

                    });

        }

    }
}
