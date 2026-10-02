package com.mycompany.motorphemployeeapp.components;

import com.mycompany.motorphemployeeapp.Employee;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class HomePanel extends JPanel {

    private final Employee employee;

    private final Font regularFont;

    private final Font boldFont;

    public HomePanel(Employee employee, String rolePrefix) {

        this.employee = employee;

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
                780,
                700
                );

        setOpaque(false);

        setLayout(null);

        String greeting = "Hey ";

        if (!rolePrefix.isBlank()) {

            greeting += rolePrefix + " ";
        }

        greeting += employee.getFirstName() + ", welcome back!";

        JLabel welcomeLabel =
                new JLabel(greeting);

        welcomeLabel.setBounds(
                25,
                40,
                500,
                40
                );

        welcomeLabel.setFont(
                boldFont.deriveFont(22f)
                );

        add(welcomeLabel);

        JPanel mainCard =
                new JPanel();

        mainCard.setBounds(
                20,
                100,
                720,
                540
                );

        mainCard.setBackground(Color.WHITE);

        mainCard.setLayout(null);

        mainCard.setBorder(
                new LineBorder(
                new Color(235,235,235),
                1,
                true
                )
                );

        add(mainCard);

        createProfile(mainCard);

        createEmployeeInfo(mainCard);

        createContactPanel(mainCard);
    }

    private void createProfile(JPanel mainCard) {

        ImageIcon profileIcon =
                new ImageIcon(
                getClass().getResource(
                "/Images/profile.png"
                )
                );

        Image scaledProfile =
                profileIcon.getImage().getScaledInstance(
                250,
                250,
                Image.SCALE_SMOOTH
                );

        JLabel profileLabel =
                new JLabel(
                new ImageIcon(scaledProfile)
                );

        profileLabel.setBounds(
                20,
                40,
                250,
                250
                );

        mainCard.add(profileLabel);
    }

    private void createEmployeeInfo(JPanel mainCard) {

        JLabel fullName =
                new JLabel(
                "Full Name: "
                + employee.getFirstName()
                + " "
                + employee.getLastName()
                );

        fullName.setBounds(
                320,
                60,
                400,
                35
                );

        fullName.setFont(
                boldFont.deriveFont(20f)
                );

        mainCard.add(fullName);

        JLabel employeeID =
                new JLabel(
                "Employee ID: "
                + employee.getEmployeeNumber()
                );

        employeeID.setBounds(
                320,
                120,
                350,
                35
                );

        employeeID.setFont(
                boldFont.deriveFont(20f)
                );

        mainCard.add(employeeID);

        JLabel positionLabel =
                new JLabel(
                "Job Title: "
                + employee.getPosition()
                );

        positionLabel.setBounds(
                320,
                185,
                350,
                28
                );

        positionLabel.setFont(
                regularFont.deriveFont(18f)
                );

        mainCard.add(positionLabel);

        JLabel birthdayLabel =
                new JLabel(
                "Birthday: "
                + employee.getBirthday()
                );

        birthdayLabel.setBounds(
                320,
                235,
                350,
                28
                );

        birthdayLabel.setFont(
                regularFont.deriveFont(18f)
                );

        mainCard.add(birthdayLabel);

        JLabel supervisorLabel =
                new JLabel(
                "Supervisor: "
                + employee.getSupervisor()
                );

        supervisorLabel.setBounds(
                320,
                285,
                350,
                28
                );

        supervisorLabel.setFont(
                regularFont.deriveFont(18f)
                );

        mainCard.add(supervisorLabel);
    }

    private void createContactPanel(JPanel mainCard) {

        JPanel contactPanel =
                new JPanel();

        contactPanel.setBounds(
                20,
                355,
                630,
                125
                );

        contactPanel.setBackground(
                new Color(245,245,245)
                );

        contactPanel.setLayout(null);

        contactPanel.setBorder(
                new LineBorder(
                new Color(225,225,225),
                1,
                true
                )
                );

        mainCard.add(contactPanel);

        JLabel contactTitle =
                new JLabel(
                "Contact Information"
                );

        contactTitle.setBounds(
                20,
                8,
                300,
                30
                );

        contactTitle.setFont(
                boldFont.deriveFont(20f)
                );

        contactPanel.add(contactTitle);

        JLabel emailLabel =
                new JLabel(
                "Email: "
                + employee.getFirstName().toLowerCase()
                + "@motorph.com"
                );

        emailLabel.setBounds(
                20,
                48,
                590,
                25
                );

        emailLabel.setFont(
                regularFont.deriveFont(15f)
                );

        contactPanel.add(emailLabel);

        JTextArea addressLabel =
                new JTextArea(
                "Address: "
                + employee.getAddress()
                );

        addressLabel.setBounds(
                20,
                85,
                590,
                30
                );

        addressLabel.setFont(
                regularFont.deriveFont(15f)
                );

        addressLabel.setOpaque(false);

        addressLabel.setEditable(false);

        addressLabel.setFocusable(false);

        addressLabel.setLineWrap(true);

        addressLabel.setWrapStyleWord(true);

        addressLabel.setCaretPosition(0);

        contactPanel.add(addressLabel);
    }
}
