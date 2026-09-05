/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.view;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR04-Feature 4
 * Purpose:
 *  1.  User interface for adding a new admin
 * 
 */

import alfie.util.FilePathManager;
import alfie.util.SHA256Hasher;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NewAdminForm extends JDialog {

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public NewAdminForm(JFrame parent) {
        super(parent, "Add New Admin", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 1, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton saveButton = new JButton("Save");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(saveButton);

        saveButton.addActionListener(e -> saveAdmin());
    }

    private void saveAdmin() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill out all fields.");
        return;
    }

    String hashedPassword = SHA256Hasher.hash(password);
    String adminFilePath = FilePathManager.getInstance().getAdminFilePath();
        try {
        // Ensure file ends with newline before appending
            File file = new File(adminFilePath);
            boolean fileExists = file.exists();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                if (fileExists && file.length() > 0) {
                    writer.newLine();  // Add newline before writing new entry
                }
                writer.write(username + "," + hashedPassword);
            }

            JOptionPane.showMessageDialog(this, "Admin added successfully.");
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save admin: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
