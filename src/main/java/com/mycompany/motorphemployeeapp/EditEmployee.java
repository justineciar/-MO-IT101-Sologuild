package com.mycompany.motorphemployeeapp;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditEmployee extends JDialog {

    private JTextField employeeIdField;
    private JTextField firstNameField;
    private JTextField birthdayField;
    private JTextField addressField;
    private JTextField phoneField;

    private JTextField sssField;
    private JTextField philhealthField;
    private JTextField tinField;
    private JTextField pagibigField;

    private JComboBox<String> statusField;
    private JComboBox<String> positionField;
    private JTextField supervisorField;

    private JTextField salaryField;
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;
    private JButton saveButton;

    private JLabel phoneErrorLabel;
    private JLabel salaryErrorLabel;
    private JLabel riceErrorLabel;
    private JLabel phoneAllowanceErrorLabel;
    private JLabel clothingErrorLabel;

    private ViewEmployee viewEmployee;
    private JTable employeeTable;
    private int selectedRow;
    private Employee loggedInEmployee;

    private HREmployeeListPanel employeePanel;

    public EditEmployee(
            Frame parent,
            JTable employeeTable,
            int selectedRow,
            ViewEmployee viewEmployee,
            HREmployeeListPanel employeePanel,
            Employee loggedInEmployee
            ) {

        super(
                parent,
                "Edit Employee",
                true
                );

        this.loggedInEmployee = loggedInEmployee;
        this.viewEmployee = viewEmployee;
        this.employeeTable = employeeTable;
        this.selectedRow = selectedRow;
        this.employeePanel = employeePanel;

        setSize(850,700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(0,0,850,700);

        add(panel);

        JPanel formPanel =
                new JPanel(
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

        formPanel.setPreferredSize(
                new Dimension(
                760,
                1100
                )
                );

        JScrollPane scrollPane =
                new JScrollPane(formPanel);

        scrollPane.setBounds(
                20,
                70,
                790,
                500
                );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                Color.BLACK,
                1
                )
                );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(scrollPane);

        JLabel title =
                new JLabel("Edit Employee");

        title.setBounds(
                0,
                20,
                900,
                40
                );

        title.setHorizontalAlignment(
                SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                26
                )
                );

        panel.add(title);

        employeeIdField =
                addField(
                formPanel,
                "Employee ID",
                employeeTable.getModel().getValueAt(
                selectedRow,
                0
                ).toString()
                );

        employeeIdField.setEditable(false);

        employeeIdField.setBackground(
                new Color(240,240,240)
                );

        employeeIdField.setForeground(
                Color.DARK_GRAY
                );

        employeeIdField.setBackground(
                new Color(235,235,235)
                );

        firstNameField =
                addField(
                formPanel,
                "First Name",
                employeeTable.getModel().getValueAt(
                selectedRow,
                1
                ).toString()
                );

        firstNameField.setEditable(false);
        firstNameField.setBackground(
                new Color(235,235,235)
                );

        birthdayField =
                addField(
                formPanel,
                "Birthday",
                employeeTable.getModel().getValueAt(
                selectedRow,
                2
                ).toString()
                );

        birthdayField.setEditable(false);

        birthdayField.setBackground(
                new Color(235,235,235)
                );

        birthdayField.setForeground(
                Color.DARK_GRAY
                );

        addressField =
                addField(
                formPanel,
                "Address",
                employeeTable.getModel().getValueAt(
                selectedRow,
                3
                ).toString()
                );

        addFocusValidation(
                addressField,
                value -> value.matches(
                "^(?=.*[A-Za-z])[A-Za-z0-9][A-Za-z0-9\\s,./#-]{7,99}$"
                ),
                "Please enter a valid address (8-100 characters).\n"
                + "Example: 123 Mabini St., Cebu City"
                );

        phoneField =
                addField(
                formPanel,
                "Phone Number",
                employeeTable.getModel().getValueAt(
                selectedRow,
                4
                ).toString()
                );

        phoneField.addKeyListener(
                new java.awt.event.KeyAdapter() {

                    @Override
            public void keyTyped(
                    java.awt.event.KeyEvent e
                    ) {

                        char c = e.getKeyChar();

                        if(
                        !Character.isDigit(c)
                        && c != '\b'
                        ){
                            e.consume();
                        }
                    }
                }
                );

        sssField =
                addField(
                formPanel,
                "SSS Number",
                employeeTable.getModel().getValueAt(
                selectedRow,
                5
                ).toString()
                );

        sssField.setEditable(false);
        sssField.setBackground(
                new Color(235,235,235)
                );

        philhealthField =
                addField(
                formPanel,
                "PhilHealth Number",
                employeeTable.getModel().getValueAt(
                selectedRow,
                6
                ).toString()
                );

        philhealthField.setEditable(false);
        philhealthField.setBackground(
                new Color(235,235,235)
                );

        tinField =
                addField(
                formPanel,
                "TIN Number",
                employeeTable.getModel().getValueAt(
                selectedRow,
                7
                ).toString()
                );

        tinField.setEditable(false);
        tinField.setBackground(
                new Color(235,235,235)
                );

        pagibigField =
                addField(
                formPanel,
                "Pag-IBIG Number",
                employeeTable.getModel().getValueAt(
                selectedRow,
                8
                ).toString()
                );

        pagibigField.setEditable(false);
        pagibigField.setBackground(
                new Color(235,235,235)
                );

        statusField = new JComboBox<>(
                new String[]{
                    "Regular",
                    "Probationary",
                    "Contractual"
                }
                );

        statusField.setSelectedItem(
                employeeTable.getModel().getValueAt(
                selectedRow,
                9
                ).toString()
                );

        addComboField(
                formPanel,
                "Status",
                statusField
                );

        positionField = new JComboBox<>(
                new String[]{
                    "Chief Executive Officer",
                    "Chief Operating Officer",
                    "Chief Finance Officer",
                    "HR Manager",
                    "HR Team Leader",
                    "Payroll Manager",
                    "Payroll Team Leader",
                    "Accounting Head",
                    "Accounting Staff",
                    "Employee"
                }
                );

        positionField.setSelectedItem(
                employeeTable.getModel().getValueAt(
                selectedRow,
                10
                ).toString()
                );

        addComboField(
                formPanel,
                "Position",
                positionField
                );

        supervisorField =
                addField(
                formPanel,
                "Immediate Supervisor",
                employeeTable.getModel().getValueAt(
                selectedRow,
                11
                ).toString()
                );

        salaryField =
                addField(
                formPanel,
                "Basic Salary",
                employeeTable.getModel().getValueAt(
                selectedRow,
                12
                ).toString()
                );
        allowNumbersOnly(salaryField);

        riceSubsidyField =
                addField(
                formPanel,
                "Rice Subsidy",
                employeeTable.getModel().getValueAt(
                selectedRow,
                13
                ).toString()
                );
        allowNumbersOnly(riceSubsidyField);

        phoneAllowanceField =
                addField(
                formPanel,
                "Phone Allowance",
                employeeTable.getModel().getValueAt(
                selectedRow,
                14
                ).toString()
                );
        allowNumbersOnly(phoneAllowanceField);

        clothingAllowanceField =
                addField(
                formPanel,
                "Clothing Allowance",
                employeeTable.getModel().getValueAt(
                selectedRow,
                15
                ).toString()
                );
        allowNumbersOnly(clothingAllowanceField);

        addFocusValidation(
                salaryField,
                this::isValidMoney,
                "Basic Salary must be numeric."
                );

        addFocusValidation(
                riceSubsidyField,
                this::isValidMoney,
                "Rice Subsidy must be numeric."
                );

        addFocusValidation(
                phoneAllowanceField,
                this::isValidMoney,
                "Phone Allowance must be numeric."
                );

        addFocusValidation(
                phoneField,
                text -> {

                    text = text.replaceAll("[^0-9]", "");

                    return text.matches("\\d{7,11}");
                },
                "Enter a valid mobile or telephone number."
                );;

        addFocusValidation(
                clothingAllowanceField,
                this::isValidMoney,
                "Clothing Allowance must be numeric."
                );

        saveButton =
                createRoundedButton(
                "Save",
                new Color(46,125,50),
                Color.WHITE
                );

        saveButton.setBounds(
                260,
                585,
                140,
                45
                );

        saveButton.setBackground(
                new Color(46,125,50)
                );

        saveButton.setForeground(
                Color.WHITE
                );

        panel.add(saveButton);

        saveButton.addActionListener(e -> {

                    StringBuilder errors = new StringBuilder();

                    String phone =
                    phoneField.getText()
                    .replaceAll("[^0-9]", "");

                    if (
                    !phone.matches("\\d{7,11}")
                    ) {

                        errors.append(
                        "• Enter a valid mobile or telephone number\n"
                        );
                    }

                    String address =
                    addressField.getText().trim();

                    if (addressField.getText().trim().isEmpty()) {
                        errors.append(
                        "• Address is required\n"
                        );
                    } else if (!address.matches(".*[a-zA-Z].*")) {

                        errors.append("• Address must contain letters\n");
                    }

                    if (
                    statusField.getSelectedItem() == null
                    ) {

                        errors.append(
                        "• Status is required\n"
                        );
                    }

                    if (
                    positionField.getSelectedItem() == null
                    ) {

                        errors.append(
                        "• Position is required\n"
                        );
                    }

                    String supervisor =
                    supervisorField.getText().trim();

                    if (
                    supervisorField.getText().trim().isEmpty()){

                        errors.append(
                        "• Immediate Supervisor is required\n"
                        );
                    } else if (!supervisor.matches("[a-zA-Z .,'-]+")) {

                        errors.append("• Supervisor name must contain letters only\n");
                    }

                    if (
                    !isValidMoney(
                    salaryField.getText()
                    )
                    ) {

                        errors.append(
                        "• Invalid Basic Salary\n"
                        );
                    }

                    if (
                    !isValidMoney(
                    riceSubsidyField.getText()
                    )
                    ) {

                        errors.append(
                        "• Invalid Rice Subsidy\n"
                        );
                    }

                    if (
                    !isValidMoney(
                    phoneAllowanceField.getText()
                    )
                    ) {

                        errors.append(
                        "• Invalid Phone Allowance\n"
                        );
                    }

                    if (
                    !isValidMoney(
                    clothingAllowanceField.getText()
                    )
                    ) {

                        errors.append(
                        "• Invalid Clothing Allowance\n"
                        );
                    }

                    if (errors.length() > 0) {

                        JOptionPane.showMessageDialog(
                        EditEmployee.this,
                        errors.toString(),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    EmployeeDataManager manager =
                    new EmployeeDataManager();

                    String salary =
                    salaryField.getText()
                    .replace("₱", "")
                    .replace(",", "")
                    .trim();

                    String rice =
                    riceSubsidyField.getText()
                    .replace("₱", "")
                    .replace(",", "")
                    .trim();

                    String phoneAllowance =
                    phoneAllowanceField.getText()
                    .replace("₱", "")
                    .replace(",", "")
                    .trim();

                    String clothing =
                    clothingAllowanceField.getText()
                    .replace("₱", "")
                    .replace(",", "")
                    .trim();

                    String originalSalary =
                    cleanMoney(
                    employeeTable.getModel().getValueAt(
                    selectedRow,
                    12
                    ).toString()
                    );

                    String originalRice =
                    cleanMoney(
                    employeeTable.getModel().getValueAt(
                    selectedRow,
                    13
                    ).toString()
                    );

                    String originalPhoneAllowance =
                    cleanMoney(
                    employeeTable.getModel().getValueAt(
                    selectedRow,
                    14
                    ).toString()
                    );

                    String originalClothing =
                    cleanMoney(
                    employeeTable.getModel().getValueAt(
                    selectedRow,
                    15
                    ).toString()
                    );

                    boolean salaryChangeRequested =
                    !salary.equals(originalSalary)
                    || !rice.equals(originalRice)
                    || !phoneAllowance.equals(originalPhoneAllowance)
                    || !clothing.equals(originalClothing);

                    boolean updated =
                    manager.updateEmployee(
                    employeeIdField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    statusField.getSelectedItem().toString(),
                    positionField.getSelectedItem().toString(),
                    supervisorField.getText(),
                    salaryChangeRequested ? originalSalary : salary,
                    salaryChangeRequested ? originalRice : rice,
                    salaryChangeRequested ? originalPhoneAllowance : phoneAllowance,
                    salaryChangeRequested ? originalClothing : clothing
                    );

                    if(!updated){

                        JOptionPane.showMessageDialog(
                        this,
                        "Failed to update employee."
                        );

                        return;
                    }

                    if(salaryChangeRequested){

                        boolean requestSaved =
                        manager.createSalaryChangeRequest(
                        employeeIdField.getText().trim(),
                        firstNameField.getText().trim(),
                        originalSalary,
                        salary,
                        originalRice,
                        rice,
                        originalPhoneAllowance,
                        phoneAllowance,
                        originalClothing,
                        clothing,
                        "HR Staff"
                        );

                        if(!requestSaved){

                            JOptionPane.showMessageDialog(
                            this,
                            "Employee details were updated, but the salary change request was not saved.",
                            "Salary Change Request Error",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }
                    }

                    JOptionPane.showMessageDialog(
                    this,
                    salaryChangeRequested
                    ? "Employee details updated. Salary and allowance changes were submitted for approval."
                    : "Employee updated successfully."
                    );

                    employeePanel.refreshTable();

                    int updatedRow =
                    findEmployeeRowById(
                    employeeIdField.getText().trim()
                    );

                    viewEmployee.dispose();

                    dispose();

                    if(updatedRow != -1){

                        new ViewEmployee(
                        SwingUtilities.getWindowAncestor(employeeTable),
                        employeeTable,
                        updatedRow,
                        employeePanel,
                        loggedInEmployee
                        );
                    }

                });

        JButton cancelButton =
                createRoundedButton(
                "Cancel",
                new Color(220,60,60),
                Color.WHITE
                );

        cancelButton.setBounds(
                430,
                585,
                140,
                45
                );

        cancelButton.setBackground(
                new Color(220,60,60)
                );

        cancelButton.setForeground(
                Color.WHITE
                );

        cancelButton.addActionListener(
                e -> dispose()
                );

        panel.add(cancelButton);

        setVisible(true);
    }

    private void addFocusValidation(
            JTextField field,
            java.util.function.Predicate<String> validator,
            String errorMessage
            ) {

        field.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    @Override
            public void focusLost(
                    java.awt.event.FocusEvent e
                    ) {

                        String text =
                        field.getText().trim();

                        if(text.isEmpty()){
                            return;
                        }

                        if(!validator.test(text)){

                            field.setBorder(
                            BorderFactory.createLineBorder(
                            Color.RED,
                            2
                            )
                            );

                            JOptionPane.showMessageDialog(
                            EditEmployee.this,
                            errorMessage,
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                            );

                            SwingUtilities.invokeLater(() -> field.requestFocusInWindow());

                        }else{

                            field.setBorder(
                            BorderFactory.createLineBorder(
                            new Color(200,200,200)
                            )
                            );
                        }
                    }
                }
                );
    }

    private void allowNumbersOnly(
            JTextField field
            ) {

        field.addKeyListener(
                new java.awt.event.KeyAdapter() {

                    @Override
            public void keyTyped(
                    java.awt.event.KeyEvent e
                    ) {

                        char c = e.getKeyChar();

                        if (!Character.isDigit(c)
                        && c != '.'
                        && c != '\b') {

                            e.consume();
                        }

                        if (c == '.'
                        && field.getText().contains(".")) {

                            e.consume();
                        }
                    }
                }
                );
    }

    private JTextField addField(
            JPanel panel,
            String labelText,
            String value
            ) {

        JLabel label =
                new JLabel(labelText);

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

        JPanel valuePanel =
                new JPanel(
                new BorderLayout()
                );

        valuePanel.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                Color.BLACK
                ),
                BorderFactory.createEmptyBorder(
                10,
                15,
                10,
                10
                )
                )
                );

        JTextField field =
                new JTextField(value);

        field.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                new Color(200,200,200)
                ),
                BorderFactory.createEmptyBorder(
                5,
                8,
                5,
                8
                )
                )
                );

        field.setBackground(Color.WHITE);

        valuePanel.setLayout(
                new BorderLayout()
                );

        valuePanel.add(
                field,
                BorderLayout.CENTER
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridy =
                panel.getComponentCount() / 2;

        gbc.gridx = 0;
        gbc.weightx = 0.50;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        panel.add(
                label,
                gbc
                );

        gbc.gridx = 1;
        gbc.weightx = 0.50;

        panel.add(
                valuePanel,
                gbc
                );

        return field;
    }

    private void addComboField(
            JPanel panel,
            String labelText,
            JComboBox<String> combo
            ) {

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
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

        JPanel valuePanel =
                new JPanel(
                new BorderLayout()
                );

        valuePanel.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                Color.BLACK
                ),
                BorderFactory.createEmptyBorder(
                10,
                15,
                10,
                10
                )
                )
                );

        combo.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        valuePanel.add(
                combo,
                BorderLayout.CENTER
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridy =
                panel.getComponentCount() / 2;

        gbc.gridx = 0;
        gbc.weightx = 0.40;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        panel.add(
                label,
                gbc
                );

        gbc.gridx = 1;
        gbc.weightx = 0.60;

        panel.add(
                valuePanel,
                gbc
                );
    }

    private JLabel createErrorLabel() {

        JLabel label = new JLabel(" ");

        label.setForeground(Color.RED);

        label.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                11
                )
                );

        return label;
    }

    private void addInstantValidation(
            JTextField field,
            java.util.function.Predicate<String> validator
            ) {

        field.getDocument().addDocumentListener(
                new DocumentListener() {

                    private void validateField() {

                        if (!validator.test(field.getText().trim())) {

                            field.setBorder(
                            BorderFactory.createLineBorder(
                            Color.RED,
                            2
                            )
                            );

                        } else {

                            field.setBorder(
                            BorderFactory.createLineBorder(
                            new Color(200,200,200)
                            )
                            );
                        }

                    }

                    @Override
            public void insertUpdate(DocumentEvent e) {
                        validateField();
                    }

                    @Override
            public void removeUpdate(DocumentEvent e) {
                        validateField();
                    }

                    @Override
            public void changedUpdate(DocumentEvent e) {
                        validateField();
                    }
                }
                );
    }

    private boolean isValidMoney(String text) {

        text = text.trim();

        if (text.isEmpty()) {
            return false;
        }

        text = text.replace("₱", "")
                .replace(",", "")
                .trim();

        try {

            Double.parseDouble(text);

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

    private String cleanMoney(String text) {

        return text
                .replace("₱", "")
                .replace("â‚±", "")
                .replace(",", "")
                .trim();
    }

    private int findEmployeeRowById(String employeeId) {

        for(int row = 0; row < employeeTable.getModel().getRowCount(); row++){

            Object value =
                    employeeTable.getModel().getValueAt(
                    row,
                    0
                    );

            if(value != null
                    && value.toString().trim().equals(employeeId)){

                return row;
            }
        }

        return -1;
    }

    private JButton createRoundedButton(
            String text,
            Color bg,
            Color fg
            ) {

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

                g2.setColor(getBackground());

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

            @Override
            protected void paintBorder(
                    Graphics g
                    ){

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setColor(getBackground());

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

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);

        button.setBackground(bg);
        button.setForeground(fg);

        button.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        return button;
    }
}
