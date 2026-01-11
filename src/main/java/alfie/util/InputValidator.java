/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.util;

/**
 *
 * @author Alfie
 */

import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import javax.swing.*;

public class InputValidator {
    
    // Methods for Overloaded reset using a Map
    public static void resetFieldBackgrounds(Map<String, JTextField> fieldsMap) {
        for (JTextField field : fieldsMap.values()) {
            field.setBackground(Color.WHITE);
        }
    }
    
    // Methods to reset all field background
    public static void resetFieldBackgrounds(JTextField... fields) {
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
        }
    }
    
    // Methods to validate required fields using a Map (with field name labels)
    public static boolean validateRequiredFields(Component parent, JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                field.setBackground(Color.PINK);
                field.requestFocus();
                JOptionPane.showMessageDialog(parent, "is required.", "Missing Field", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public static boolean validatePasswordField(JFrame parent, JPasswordField field) {
        if (field.getPassword().length == 0) {
            JOptionPane.showMessageDialog(parent, "Password is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validateMinLength(JFrame parent, JTextField field, int minLength, String fieldName) {
        if (field.getText().trim().length() < minLength) {
            JOptionPane.showMessageDialog(parent, fieldName + " must be at least " + minLength + " characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validateEmailFormat(JFrame parent, JTextField emailField) {
        String email = emailField.getText().trim();
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(parent, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        return true;
    }
    
}
