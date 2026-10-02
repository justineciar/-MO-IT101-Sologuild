package com.mycompany.motorphemployeeapp;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ViewEmployee extends JDialog {

    private HREmployeeListPanel employeePanel;
    private Employee loggedInEmployee;

    public ViewEmployee(
            Window parent,
            JTable employeeTable,
            int selectedRow,
            HREmployeeListPanel employeePanel,
            Employee loggedInEmployee
            ) {

        super(
                parent,
                "Employee Information",
                ModalityType.APPLICATION_MODAL
                );

        this.loggedInEmployee = loggedInEmployee;

        this.employeePanel = employeePanel;
        setSize(850, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel =
                new JLabel("Employee Information");

        titleLabel.setBounds(
                0,
                20,
                850,
                40
                );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                26
                )
                );

        mainPanel.add(titleLabel);

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel(
                new GridBagLayout()
                );

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
                )
                );

        addField(
                formPanel,
                "Employee Number",
                employeeTable.getModel().getValueAt(selectedRow,0).toString()
                );

        addField(
                formPanel,
                "Name",
                employeeTable.getModel().getValueAt(selectedRow,1).toString()
                );

        addField(
                formPanel,
                "Birthday",
                employeeTable.getModel().getValueAt(selectedRow,2).toString()
                );

        addField(
                formPanel,
                "Address",
                employeeTable.getModel().getValueAt(selectedRow,3).toString()
                );

        addField(
                formPanel,
                "Phone Number",
                employeeTable.getModel().getValueAt(selectedRow,4).toString()
                );

        addField(
                formPanel,
                "SSS Number",
                employeeTable.getModel().getValueAt(selectedRow,5).toString()
                );

        addField(
                formPanel,
                "PhilHealth Number",
                employeeTable.getModel().getValueAt(selectedRow,6).toString()
                );

        addField(
                formPanel,
                "TIN Number",
                employeeTable.getModel().getValueAt(selectedRow,7).toString()
                );

        addField(
                formPanel,
                "Pag-IBIG Number",
                employeeTable.getModel().getValueAt(selectedRow,8).toString()
                );

        addField(
                formPanel,
                "Status",
                employeeTable.getModel().getValueAt(selectedRow,9).toString()
                );

        addField(
                formPanel,
                "Position",
                employeeTable.getModel().getValueAt(selectedRow,10).toString()
                );

        addField(
                formPanel,
                "Supervisor",
                employeeTable.getModel().getValueAt(selectedRow,11).toString()
                );

        addField(
                formPanel,
                "Basic Salary",
                employeeTable.getModel().getValueAt(selectedRow,12).toString()
                );

        addField(
                formPanel,
                "Rice Subsidy",
                employeeTable.getModel().getValueAt(selectedRow,13).toString()
                );

        addField(
                formPanel,
                "Phone Allowance",
                employeeTable.getModel().getValueAt(selectedRow,14).toString()
                );

        addField(
                formPanel,
                "Clothing Allowance",
                employeeTable.getModel().getValueAt(selectedRow,15).toString()
                );

        addField(
                formPanel,
                "Gross Semi Monthly",
                employeeTable.getModel().getValueAt(selectedRow,16).toString()
                );

        addField(
                formPanel,
                "Hourly Rate",
                employeeTable.getModel().getValueAt(selectedRow,17).toString()
                );

        JScrollPane scrollPane =
                new JScrollPane(
                formPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );

        scrollPane.setBounds(
                20,
                70,
                790,
                500
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                Color.BLACK,
                1
                )
                );

        mainPanel.add(scrollPane);

        // =========================
        // BUTTONS
        // =========================

        JButton editButton =
                createRoundedButton(
                "Edit",
                new Color(46,125,50),
                Color.WHITE
                );

        editButton.setBounds(
                280,
                595,
                140,
                45
                );

        editButton.addActionListener(e -> {

                    String selectedEmployeeId =
                    employeeTable.getValueAt(selectedRow, 0).toString();

                    if (selectedEmployeeId.equals(loggedInEmployee.getEmployeeNumber())) {

                        JOptionPane.showMessageDialog(
                        this,
                        "You cannot edit your own employee record.",
                        "Access Denied",
                        JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }

                    new EditEmployee(
                    (Frame) getParent(),
                    employeeTable,
                    selectedRow,
                    this,
                    employeePanel,
                    loggedInEmployee
                    );

                    dispose();

                });

        JButton exitButton =
                createRoundedButton(
                "Exit",
                new Color(220,60,60),
                Color.WHITE
                );

        exitButton.setBounds(
                440,
                595,
                140,
                45
                );

        exitButton.addActionListener(
                e -> dispose()
                );

        mainPanel.add(editButton);
        mainPanel.add(exitButton);

        add(mainPanel);

        setVisible(true);
    }

    private void addField(
            JPanel panel,
            String labelText,
            String value
            ) {

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        label.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                Color.BLACK
                ),
                BorderFactory.createEmptyBorder(
                0,
                10,
                0,
                10
                )
                )
                );

        label.setOpaque(true);

        label.setBackground(
                new Color(
                245,
                245,
                245
                )
                );

        label.setPreferredSize(
                new Dimension(
                200,
                60
                )
                );

        JPanel valuePanel =
                new JPanel(
                new BorderLayout()
                );

        valuePanel.setBackground(
                Color.WHITE
                );

        valuePanel.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                Color.BLACK
                ),
                BorderFactory.createEmptyBorder(
                0,
                10,
                0,
                10
                )
                )
                );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        JPanel wrapper = new JPanel(
                new BorderLayout()
                );

        wrapper.setBackground(Color.WHITE);

        wrapper.setBorder(
                BorderFactory.createEmptyBorder(
                0,
                10,
                0,
                10
                )
                );

        wrapper.add(
                valueLabel,
                BorderLayout.WEST
                );

        valuePanel.add(
                wrapper,
                BorderLayout.CENTER
                );
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = panel.getComponentCount() / 2;

        gbc.gridx = 0;
        gbc.weightx = 0.40;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.60;

        panel.add(valuePanel, gbc);
    }

    private JButton createRoundedButton(
            String text,
            Color backgroundColor,
            Color foregroundColor
            ) {

        JButton button = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                        );

                g2.setColor(getBackground());

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        25,
                        25
                        );

                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };

        button.setContentAreaFilled(false);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setBackground(backgroundColor);

        button.setForeground(foregroundColor);

        button.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                16
                )
                );

        button.setCursor(
                new Cursor(
                Cursor.HAND_CURSOR
                )
                );

        return button;
    }
}
