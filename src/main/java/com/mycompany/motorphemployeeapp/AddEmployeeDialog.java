package com.mycompany.motorphemployeeapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddEmployeeDialog extends JDialog {

    private HREmployeeListPanel parentPanel;

    public AddEmployeeDialog(
            HREmployeeListPanel parentPanel
            ) throws IOException {

        this.parentPanel = parentPanel;

        EmployeeDataManager manager = new EmployeeDataManager();

        JTextField employeeIdField = new JTextField(manager.getNextEmployeeId());

        employeeIdField.setEditable(false);

        employeeIdField.setToolTipText(
                "Automatically assigned"
                );

        JTextField firstNameField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Given Name"
                );

        firstNameField.setToolTipText(
                "First Name"
                );

        JTextField lastNameField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Family Name"
                );

        lastNameField.setToolTipText(
                "Last Name"
                );

        KeyAdapter lettersOnly =
                new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {

                char c = e.getKeyChar();

                if(!Character.isLetter(c)
                        && c != ' '
                        && c != '-'
                        && c != '\''
                        && c != KeyEvent.VK_BACK_SPACE) {

                    e.consume();
                }
            }
        };

        firstNameField.addKeyListener(lettersOnly);

        addFocusValidation(
                firstNameField,
                value -> value.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$"),
                "First name must contain letters only."
                );

        lastNameField.addKeyListener(lettersOnly);

        addFocusValidation(
                lastNameField,
                value -> value.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$"),
                "Last name must contain letters only."
                );

        JTextField birthdayField =
                new HREmployeeListPanel.PlaceholderTextField(
                "MM/DD/YYYY"
                );

        birthdayField.setToolTipText(
                "MM/DD/YYYY"
                );

        birthdayField.addFocusListener(
                new FocusAdapter() {

                    @Override
            public void focusLost(
                    FocusEvent e
                    ) {

                        String birthday = birthdayField.getText().trim();

                        // Accept birthdays without slashes
                        if (birthday.matches("\\d{8}")) {
                            birthday = birthday.substring(0, 2) + "/"
                            + birthday.substring(2, 4) + "/"
                            + birthday.substring(4);

                            birthdayField.setText(birthday);
                        }

                        if(birthday.isEmpty()){

                            return;
                        }

                        String[] parts =
                        birthday.split("/");

                        if(parts.length != 3){

                            JOptionPane.showMessageDialog(
                            null,
                            "Birthday must be in MM/DD/YYYY format.",
                            "Invalid Birthday",
                            JOptionPane.ERROR_MESSAGE
                            );

                            birthdayField.requestFocus();

                            return;
                        }

                        try{

                            int month =
                            Integer.parseInt(parts[0]);

                            int day =
                            Integer.parseInt(parts[1]);

                            int year =
                            Integer.parseInt(parts[2]);

                            if(month < 1 || month > 12){

                                JOptionPane.showMessageDialog(
                                null,
                                "Month must be between 1 and 12.",
                                "Invalid Birthday",
                                JOptionPane.ERROR_MESSAGE
                                );

                                birthdayField.requestFocus();

                                return;
                            }

                            LocalDate birthDate =
                            LocalDate.of(
                            year,
                            month,
                            day
                            );

                            int age =
                            Period.between(
                            birthDate,
                            LocalDate.now()
                            ).getYears();

                            if(age < 18){

                                JOptionPane.showMessageDialog(
                                null,
                                "Employee must be at least 18 years old.",
                                "Invalid Birthday",
                                JOptionPane.ERROR_MESSAGE
                                );

                                birthdayField.requestFocus();

                                return;
                            }

                            if(age > 70){

                                JOptionPane.showMessageDialog(
                                null,
                                "Employee exceeds working age.",
                                "Invalid Birthday",
                                JOptionPane.ERROR_MESSAGE
                                );

                                birthdayField.requestFocus();

                                return;
                            }

                        }catch(java.time.DateTimeException ex){

                            JOptionPane.showMessageDialog(
                            null,
                            "The day entered does not exist for that month.",
                            "Invalid Birthday",
                            JOptionPane.ERROR_MESSAGE
                            );

                            birthdayField.requestFocus();

                        }catch(NumberFormatException ex){

                            JOptionPane.showMessageDialog(
                            null,
                            "Birthday must contain numbers only.",
                            "Invalid Birthday",
                            JOptionPane.ERROR_MESSAGE
                            );

                            birthdayField.requestFocus();
                        }
                    }
                }
                );

        birthdayField.setToolTipText(
                "MM/DD/YYYY"
                );

        JTextField addressField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Home Address"
                );

        addressField.setToolTipText(
                "House No., Street, City"
                );

        addFocusValidation(
                addressField,
                value -> value.matches("^(?=.*[A-Za-z])[A-Za-z0-9][A-Za-z0-9\\s,./#-]{7,99}$"),
                "Please enter a valid address (8-100 characters)."
                );

        JTextField phoneField =
                new HREmployeeListPanel.PlaceholderTextField(
                "09XXXXXXXXX"
                );

        phoneField.setToolTipText(
                "09XXXXXXXXX"
                );

        JTextField sssField =
                new HREmployeeListPanel.PlaceholderTextField(
                "##-#######-#"
                );

        sssField.setToolTipText(
                "Ex. 1234567891"
                );

        JTextField philhealthField =
                new HREmployeeListPanel.PlaceholderTextField(
                "##-#########-#"
                );

        philhealthField.setToolTipText(
                "123456789123"
                );

        JTextField tinField =
                new HREmployeeListPanel.PlaceholderTextField(
                "###-###-###-###"
                );

        tinField.setToolTipText(
                "123456789123"
                );

        JTextField pagibigField =
                new HREmployeeListPanel.PlaceholderTextField(
                "####-####-####"
                );

        pagibigField.setToolTipText(
                "123456789123"
                );

        JComboBox<String> statusComboBox =
                new JComboBox<>(new String[]{
                    "Regular",
                    "Probationary",
                    "Contractual"
                });

        JComboBox<String> positionComboBox =
                new JComboBox<>(new String[]{
                    "Chief Executive Officer",
                    "Chief Operating Officer",
                    "Chief Finance Officer",
                    "Chief Marketing Officer",
                    "IT Operations and Systems",
                    "HR Manager",
                    "HR Team Leader",
                    "HR Rank and File",
                    "Accounting Head",
                    "Payroll Manager",
                    "Payroll Team Leader",
                    "Payroll Rank and File",
                    "Account Manager",
                    "Account Team Leader",
                    "Account Rank and File",
                    "Sales & Marketing",
                    "Supply Chain and Logistics",
                    "Customer Service and Relations"
                });

        JTextField supervisorField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Immediate Supervisor"
                );

        supervisorField.setToolTipText(
                "Supervisor Name"
                );

        JTextField salaryField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Salary"
                );

        salaryField.setToolTipText(
                "Example: 30000"
                );

        JTextField riceSubsidyField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Rice Subsidy"
                );

        riceSubsidyField.setToolTipText(
                "Example: 1500"
                );

        JTextField phoneAllowanceField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Phone Allowance"
                );

        phoneAllowanceField.setToolTipText(
                "Example: 1000"
                );

        JTextField clothingAllowanceField =
                new HREmployeeListPanel.PlaceholderTextField(
                "Clothing Allowance"
                );

        clothingAllowanceField.setToolTipText(
                "Example: 1000"
                );

        KeyAdapter numbersOnly =
                new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {

                char c = e.getKeyChar();

                if(!Character.isDigit(c)
                        && c != KeyEvent.VK_BACK_SPACE) {

                    e.consume();
                }
            }
        };

        phoneField.addKeyListener(numbersOnly);

        addFocusValidation(
                phoneField,
                value -> {
                    String phone = value.replaceAll("[^0-9]", "");
                    return phone.matches("09\\d{9}")
                    || phone.matches("032\\d{7}")
                    || phone.matches("\\d{7,8}");
                },
                "Phone number must be a valid mobile (09XXXXXXXXX) or telephone number."
                );

        sssField.addKeyListener(numbersOnly);

        addFocusValidation(
                sssField,
                value -> value.replaceAll("[^0-9]", "").matches("\\d{10}"),
                "SSS number must contain exactly 10 digits."
                );

        philhealthField.addKeyListener(numbersOnly);

        addFocusValidation(
                philhealthField,
                value -> value.replaceAll("[^0-9]", "").matches("\\d{12}"),
                "PhilHealth number must contain exactly 12 digits."
                );

        tinField.addKeyListener(numbersOnly);

        addFocusValidation(
                tinField,
                value -> value.replaceAll("[^0-9]", "").matches("\\d{12}"),
                "TIN number must contain exactly 12 digits."
                );

        pagibigField.addKeyListener(numbersOnly);

        addFocusValidation(
                pagibigField,
                value -> value.replaceAll("[^0-9]", "").matches("\\d{12}"),
                "Pag-IBIG number must contain exactly 12 digits."
                );

        salaryField.addKeyListener(numbersOnly);
        riceSubsidyField.addKeyListener(numbersOnly);
        phoneAllowanceField.addKeyListener(numbersOnly);
        clothingAllowanceField.addKeyListener(numbersOnly);


        autoScrollOnFocus(employeeIdField);
        autoScrollOnFocus(lastNameField);
        autoScrollOnFocus(firstNameField);
        autoScrollOnFocus(birthdayField);
        autoScrollOnFocus(addressField);
        autoScrollOnFocus(phoneField);
        autoScrollOnFocus(sssField);
        autoScrollOnFocus(philhealthField);
        autoScrollOnFocus(tinField);
        autoScrollOnFocus(pagibigField);
        autoScrollOnFocus(supervisorField);
        autoScrollOnFocus(salaryField);
        autoScrollOnFocus(riceSubsidyField);
        autoScrollOnFocus(phoneAllowanceField);
        autoScrollOnFocus(clothingAllowanceField);



        JPanel formPanel = new JPanel(
                new GridLayout(0, 2, 0, 0)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15
                )
                );

        formPanel.add(createLabelCell("Employee ID"));
        formPanel.add(createInputCell(employeeIdField));

        formPanel.add(createLabelCell("First Name"));
        formPanel.add(createInputCell(firstNameField));

        formPanel.add(createLabelCell("Last Name"));
        formPanel.add(createInputCell(lastNameField));

        formPanel.add(createLabelCell("Birthday"));
        formPanel.add(createInputCell(birthdayField));

        formPanel.add(createLabelCell("Address"));
        formPanel.add(createInputCell(addressField));

        formPanel.add(createLabelCell("Phone Number"));
        formPanel.add(createInputCell(phoneField));

        formPanel.add(createLabelCell("SSS"));
        formPanel.add(createInputCell(sssField));

        formPanel.add(createLabelCell("PhilHealth"));
        formPanel.add(createInputCell(philhealthField));

        formPanel.add(createLabelCell("TIN"));
        formPanel.add(createInputCell(tinField));

        formPanel.add(createLabelCell("Pag-IBIG"));
        formPanel.add(createInputCell(pagibigField));

        formPanel.add(createLabelCell("Status"));
        formPanel.add(createInputCell(statusComboBox));

        formPanel.add(createLabelCell("Position"));
        formPanel.add(createInputCell(positionComboBox));

        formPanel.add(createLabelCell("Immediate Supervisor"));
        formPanel.add(createInputCell(supervisorField));

        formPanel.add(createLabelCell("Basic Salary"));
        formPanel.add(createInputCell(salaryField));

        formPanel.add(createLabelCell("Rice Subsidy"));
        formPanel.add(createInputCell(riceSubsidyField));

        formPanel.add(createLabelCell("Phone Allowance"));
        formPanel.add(createInputCell(phoneAllowanceField));

        formPanel.add(createLabelCell("Clothing Allowance"));
        formPanel.add(createInputCell(clothingAllowanceField));

        JLabel titleLabel =
                new JLabel(
                "Employee Information",
                SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                24
                )
                );

        JPanel mainPanel =
                new JPanel(
                new BorderLayout(10,10)
                );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
                );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
                );

        JScrollPane scrollPane =
                new JScrollPane(
                mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                15,
                40,
                15,
                40
                )
                );

        scrollPane.setPreferredSize(
                new Dimension(
                700,
                550
                )
                );

        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.getVerticalScrollBar().setBlockIncrement(120);

        setTitle("Add New Employee");
        setModal(true);
        setLayout(new BorderLayout());

        add(
                scrollPane,
                BorderLayout.CENTER
                );

        JPanel buttonPanel =
                new JPanel(
                new FlowLayout(
                FlowLayout.CENTER,
                15,
                15
                )
                );

        JButton addButton =
                createRoundedButton(
                "Add",
                new Color(21,95,204),
                Color.WHITE
                );

        JButton cancelButton =
                createRoundedButton(
                "Cancel",
                new Color(220,60,60),
                Color.WHITE
                );

        addButton.setPreferredSize(
                new Dimension(150,50)
                );

        cancelButton.setPreferredSize(
                new Dimension(150,50)
                );

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH
                );

        pack();

        setSize(
                850,
                700
                );

        setLocationRelativeTo(parentPanel);

        cancelButton.addActionListener(
                e -> dispose()
                );

        addButton.addActionListener(e -> {

                    String validationMessage =
                    validateNewEmployeeInput(
                    manager,
                    employeeIdField.getText().trim(),
                    lastNameField.getText().trim(),
                    firstNameField.getText().trim(),
                    birthdayField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    sssField.getText().trim(),
                    philhealthField.getText().trim(),
                    tinField.getText().trim(),
                    pagibigField.getText().trim(),
                    supervisorField.getText().trim(),
                    salaryField.getText().trim(),
                    riceSubsidyField.getText().trim(),
                    phoneAllowanceField.getText().trim(),
                    clothingAllowanceField.getText().trim()
                    );

                    if(!validationMessage.isEmpty()){

                        JOptionPane.showMessageDialog(
                        this,
                        validationMessage,
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    // REQUIRED FIELDS VALIDATION
                    if(lastNameField.getText().trim().isEmpty()
                    || firstNameField.getText().trim().isEmpty()
                    || birthdayField.getText().trim().isEmpty()
                    || addressField.getText().trim().isEmpty()
                    || phoneField.getText().trim().isEmpty()
                    || sssField.getText().trim().isEmpty()
                    || philhealthField.getText().trim().isEmpty()
                    || tinField.getText().trim().isEmpty()
                    || pagibigField.getText().trim().isEmpty()
                    || supervisorField.getText().trim().isEmpty()
                    || salaryField.getText().trim().isEmpty()
                    || riceSubsidyField.getText().trim().isEmpty()
                    || phoneAllowanceField.getText().trim().isEmpty()
                    || clothingAllowanceField.getText().trim().isEmpty()) {

                        JOptionPane.showMessageDialog(
                        this,
                        "All fields are required.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    // SALARY VALIDATION
                    try {

                        double salary =
                        Double.parseDouble(
                        salaryField.getText().trim());

                        double riceSubsidy =
                        Double.parseDouble(
                        riceSubsidyField.getText().trim());

                        double phoneAllowance =
                        Double.parseDouble(
                        phoneAllowanceField.getText().trim());

                        double clothingAllowance =
                        Double.parseDouble(
                        clothingAllowanceField.getText().trim());

                        if(salary <= 0
                        || riceSubsidy < 0
                        || phoneAllowance < 0
                        || clothingAllowance < 0){

                            JOptionPane.showMessageDialog(
                            this,
                            "Salary and allowances cannot be negative.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                    } catch(NumberFormatException ex){

                        JOptionPane.showMessageDialog(
                        this,
                        "Salary and allowances must contain valid numbers.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }


                    // EMPLOYEE ID VALIDATION

                    if(!employeeIdField.getText().trim().matches("\\d+")) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Employee ID must contain numbers only.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    // ADDRESS VALIDATION

                    String address =
                    addressField.getText().trim();

                    if (!address.matches("^(?=.*[A-Za-z])[A-Za-z0-9][A-Za-z0-9\\s,./#-]{7,99}$")) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid address (8-100 characters).",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        addressField.requestFocus();
                        return;
                    }

                    // PHONE NUMBER VALIDATION

                    String phone =
                    phoneField.getText()
                    .replaceAll("[^0-9]", "");

                    if(!(phone.matches("09\\d{9}")
                    || phone.matches("032\\d{7}")
                    || phone.matches("\\d{7,8}"))) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Phone number must be a valid mobile (09XXXXXXXXX) or telephone number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }


                    // BIRTHDAY FORMAT VALIDATION
                    String birthday =
                    birthdayField.getText().trim();

                    try {

                        DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                        "MM/dd/yyyy"
                        );

                        LocalDate birthDate =
                        LocalDate.parse(
                        birthday,
                        formatter
                        );

                        int age =
                        Period.between(
                        birthDate,
                        LocalDate.now()
                        ).getYears();

                        if(age < 18){

                            JOptionPane.showMessageDialog(
                            this,
                            "Employee must be at least 18 years old.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                        if(age > 70){

                            JOptionPane.showMessageDialog(
                            this,
                            "Employee exceeds working age.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                    } catch(DateTimeParseException ex){

                        JOptionPane.showMessageDialog(
                        this,
                        "Invalid birthday. Use MM/DD/YYYY.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }


                    if(!firstNameField.getText().trim().matches(
                    "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$"
                    )){

                        JOptionPane.showMessageDialog(
                        this,
                        "First name must contain letters only.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }



                    if(!lastNameField.getText().trim().matches(
                    "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$"
                    )){

                        JOptionPane.showMessageDialog(
                        this,
                        "Last name must contain letters only.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    // SSS VALIDATION
                    String sss = sssField.getText().trim().replaceAll("[^0-9]", "");

                    if(!sss.matches("\\d{10}")) {

                        JOptionPane.showMessageDialog(
                        this,
                        "SSS number must contain exactly 10 digits.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        sssField.requestFocus();
                        return;
                    }

                    // PHILHEALTH VALIDATION
                    String philHealth = philhealthField.getText()
                    .trim()
                    .replaceAll("[^0-9]", "");

                    if(!philHealth.matches("\\d{12}")) {

                        JOptionPane.showMessageDialog(
                        this,
                        "PhilHealth number must contain exactly 12 digits.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        philhealthField.requestFocus();

                        return;
                    }

                    // TIN VALIDATION

                    String tin =
                    tinField.getText()
                    .trim()
                    .replaceAll("[^0-9]", "");

                    if(!tin.matches("\\d{12}")) {

                        JOptionPane.showMessageDialog(
                        this,
                        "TIN number must contain exactly 12 digits.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        tinField.requestFocus();

                        return;
                    }

                    // PAG-IBIG VALIDATION

                    String pagibig =
                    pagibigField.getText()
                    .trim()
                    .replaceAll("[^0-9]", "");

                    if(!pagibig.matches("\\d{12}")) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Pag-IBIG number must contain exactly 12 digits.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        pagibigField.requestFocus();

                        return;
                    }


                    // DUPLICATE EMPLOYEE ID VALIDATION

                    if(manager.employeeExists(
                    employeeIdField.getText().trim()
                    )) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Employee ID already exists.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    // DUPLICATE EMPLOYEE First Name, Last Name, Birthday
                    if(manager.employeeAlreadyExists(
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    birthdayField.getText().trim()
                    )) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Employee already exists in the employee list.",
                        "Duplicate Employee",
                        JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }
                    // SUPERVISOR VALIDATION

                    String supervisor =
                    supervisorField.getText().trim();

                    if(!supervisor.equalsIgnoreCase("N/A")
                    && !manager.isValidSupervisor(supervisor)) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Supervisor does not exist or is not assigned a supervisory position.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                        );

                        supervisorField.requestFocus();
                        return;
                    }

                    boolean success =
                    manager.addEmployeeToCSV(
                    employeeIdField.getText().trim(),
                    lastNameField.getText().trim(),
                    firstNameField.getText().trim(),
                    birthdayField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    sss,
                    philHealth,
                    tin,
                    pagibig,
                    statusComboBox.getSelectedItem().toString(),
                    positionComboBox.getSelectedItem().toString(),
                    supervisorField.getText().trim(),
                    salaryField.getText().trim(),
                    riceSubsidyField.getText().trim(),
                    phoneAllowanceField.getText().trim(),
                    clothingAllowanceField.getText().trim()
                    );

                    if (success){

                        JOptionPane.showMessageDialog(
                        this,
                        "Employee added successfully."
                        );

                        parentPanel.refreshTable();
                        dispose();

                    } else {

                        JOptionPane.showMessageDialog(
                        this,
                        "Employee was not saved. Please check the employee details and try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                        );
                    }

                });

        setVisible(true);
    }

    // =========================
    // SCROLL HELPER
    // =========================
    private void autoScrollOnFocus(JComponent component) {

        component.addFocusListener(new FocusAdapter() {

                    public void focusGained(FocusEvent e) {

                        SwingUtilities.invokeLater(() -> {

                            component.scrollRectToVisible(
                            component.getBounds()
                            );
                        });
                    }
                });

    }

    private String validateNewEmployeeInput(
            EmployeeDataManager manager,
            String employeeId,
            String lastName,
            String firstName,
            String birthday,
            String address,
            String phone,
            String sss,
            String philhealth,
            String tin,
            String pagibig,
            String supervisor,
            String salary,
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance
            ) {

        StringBuilder errors =
                new StringBuilder();

        if(lastName.isEmpty()
                || firstName.isEmpty()
                || birthday.isEmpty()
                || address.isEmpty()
                || phone.isEmpty()
                || sss.isEmpty()
                || philhealth.isEmpty()
                || tin.isEmpty()
                || pagibig.isEmpty()
                || supervisor.isEmpty()
                || salary.isEmpty()
                || riceSubsidy.isEmpty()
                || phoneAllowance.isEmpty()
                || clothingAllowance.isEmpty()){

            errors.append("- All fields are required.\n");
        }

        if(!employeeId.isEmpty()
                && !employeeId.matches("\\d+")){

            errors.append("- Employee ID must contain numbers only.\n");
        }

        if(!lastName.isEmpty()
                && !lastName.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$")){

            errors.append("- Last name must contain letters only.\n");
        }

        if(!firstName.isEmpty()
                && !firstName.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$")){

            errors.append("- First name must contain letters only.\n");
        }

        validateBirthday(
                errors,
                birthday
                );

        if(!address.isEmpty()
                && !address.matches("^(?=.*[A-Za-z])[A-Za-z0-9][A-Za-z0-9\\s,./#-]{7,99}$")){

            errors.append("- Address must be 8-100 characters and contain only letters, numbers, spaces, commas, periods, hyphens, slashes, or #.\n");
        }

        String digitsOnlyPhone =
                phone.replaceAll("[^0-9]", "");

        if(!phone.isEmpty()
                && !(digitsOnlyPhone.matches("09\\d{9}")
                || digitsOnlyPhone.matches("032\\d{7}")
                || digitsOnlyPhone.matches("\\d{7,8}"))){

            errors.append("- Phone number must be 09XXXXXXXXX, 032XXXXXXX, or a 7-8 digit telephone number.\n");
        }

        String cleanSSS = sss.replaceAll("[^0-9]", "");

        if(!sss.isEmpty()
                && !cleanSSS.matches("\\d{10}")){

            errors.append("- SSS number must contain exactly 10 digits.\n");
        }

        String cleanPhilHealth = philhealth.replaceAll("[^0-9]", "");

        if(!philhealth.isEmpty()
                && !cleanPhilHealth.matches("\\d{12}")){

            errors.append("- PhilHealth number must contain exactly 12 digits.\n");
        }

        String cleanTIN =
                tin.replaceAll("[^0-9]", "");

        if(!tin.isEmpty()
                && !cleanTIN.matches("\\d{12}")){

            errors.append("- TIN number must contain exactly 12 digits.\n");
        }

        String cleanPagibig =
                pagibig.replaceAll("[^0-9]", "");

        if(!pagibig.isEmpty()
                && !cleanPagibig.matches("\\d{12}")){

            errors.append("- Pag-IBIG number must contain exactly 12 digits.\n");
        }

        if(!supervisor.isEmpty()
                && !supervisor.equalsIgnoreCase("N/A")
                && !manager.isValidSupervisor(supervisor)){

            errors.append("- Supervisor does not exist or is not assigned a supervisory position.\n");
        }

        validateMoneyField(
                errors,
                salary,
                "Basic Salary",
                true
                );

        validateMoneyField(
                errors,
                riceSubsidy,
                "Rice Subsidy",
                false
                );

        validateMoneyField(
                errors,
                phoneAllowance,
                "Phone Allowance",
                false
                );

        validateMoneyField(
                errors,
                clothingAllowance,
                "Clothing Allowance",
                false
                );

        if(errors.length() == 0
                && manager.employeeExists(employeeId)){

            errors.append("- Employee ID already exists.\n");
        }

        if(errors.length() == 0
                && manager.employeeAlreadyExists(
                firstName,
                lastName,
                birthday
                )){

            errors.append("- Employee already exists in the employee list.\n");
        }

        return errors.toString();
    }

    private void validateBirthday(
            StringBuilder errors,
            String birthday
            ) {

        if(birthday.isEmpty()){
            return;
        }

        try {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MM/dd/yyyy");

            LocalDate birthDate =
                    LocalDate.parse(
                    birthday,
                    formatter
                    );

            int age =
                    Period.between(
                    birthDate,
                    LocalDate.now()
                    ).getYears();

            if(age < 18){

                errors.append("- Employee must be at least 18 years old.\n");
            }

            if(age > 70){

                errors.append("- Employee exceeds working age.\n");
            }

        } catch(DateTimeParseException ex){

            errors.append("- Birthday must be a real date in MM/DD/YYYY format.\n");
        }
    }

    private void validateMoneyField(
            StringBuilder errors,
            String value,
            String fieldName,
            boolean mustBeGreaterThanZero
            ) {

        if(value.isEmpty()){
            return;
        }

        try {

            double amount =
                    Double.parseDouble(value);

            if(mustBeGreaterThanZero
                    && amount <= 0){

                errors.append("- ")
                        .append(fieldName)
                        .append(" must be greater than zero.\n");
            }

            if(!mustBeGreaterThanZero
                    && amount < 0){

                errors.append("- ")
                        .append(fieldName)
                        .append(" cannot be negative.\n");
            }

        } catch(NumberFormatException ex){

            errors.append("- ")
                    .append(fieldName)
                    .append(" must be a valid number.\n");
        }
    }

    private JPanel createLabelCell(String text){

        JPanel panel =
                new JPanel(
                new BorderLayout()
                );

        panel.setBorder(
                BorderFactory.createLineBorder(
                Color.BLACK
                )
                );

        panel.setBackground(
                new Color(240,240,240)
                );

        panel.setPreferredSize(
                new Dimension(
                220,
                50
                )
                );

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        label.setBorder(
                BorderFactory.createEmptyBorder(
                0,10,0,0
                )
                );

        panel.add(
                label,
                BorderLayout.CENTER
                );

        return panel;
    }

    private JPanel createInputCell(
            JComponent component
            ){

        JPanel panel =
                new JPanel(
                new BorderLayout()
                );

        panel.setBackground(
                new Color(245, 245, 245)
                );

        panel.setBorder(
                BorderFactory.createLineBorder(
                new Color(180, 180, 180))
                );

        JPanel wrapper =
                new JPanel(
                new BorderLayout()
                );

        wrapper.setOpaque(false);

        wrapper.setBorder(
                BorderFactory.createEmptyBorder(
                5,
                15,
                5,
                15
                )
                );


        if(component instanceof JTextField){

            JTextField field =
                    (JTextField) component;

            field.setBorder(
                    BorderFactory.createLineBorder(
                    new Color(220,220,220)
                    )
                    );

            field.setFont(
                    new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    13
                    )
                    );
        }

        wrapper.add(
                component,
                BorderLayout.CENTER
                );

        panel.add(
                wrapper,
                BorderLayout.CENTER
                );

        return panel;
    }

    // =========================
    // ROUNDED BUTTON
    // =========================

    private JButton createRoundedButton(
            String text,
            Color bgColor,
            Color textColor
            ) {

        JButton button =
                new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g;

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
                        20,
                        20
                        );

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setColor(bgColor);

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

        button.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                13
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
    // INSTANT VALIDATION HELPER
    // =========================

    private void addInstantValidation(
            JTextField field,
            JLabel errorLabel,
            Predicate<String> validator,
            String errorMessage
            ) {

        field.getDocument().addDocumentListener(new DocumentListener() {

                    private void validateField() {

                        String value =
                        field.getText().trim();

                        if (value.isEmpty()) {

                            errorLabel.setText(" ");

                        } else if (!validator.test(value)) {

                            errorLabel.setText(errorMessage);

                        } else {

                            errorLabel.setText(" ");
                        }
                    }

                    public void insertUpdate(DocumentEvent e) {
                        validateField();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        validateField();
                    }

                    public void changedUpdate(DocumentEvent e) {
                        validateField();
                    }
                });
    }


    private void addFocusValidation(
            JTextField field,
            Predicate<String> validator,
            String errorMessage
            ) {

        field.addFocusListener(new FocusAdapter() {

                    @Override
            public void focusLost(FocusEvent e) {

                        String value = field.getText().trim();

                        if (value.isEmpty()) {
                            return;
                        }

                        if (!validator.test(value)) {

                            JOptionPane.showMessageDialog(
                            AddEmployeeDialog.this,
                            errorMessage,
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                            );

                            field.selectAll();
                        }
                    }
                });
    }

    // =========================
    // ERROR LABEL HELPER
    // =========================

    private JLabel createErrorLabel() {

        JLabel label =
                new JLabel(" ");

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
}
