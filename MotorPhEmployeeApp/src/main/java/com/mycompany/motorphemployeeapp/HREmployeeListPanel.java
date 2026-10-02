package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.FocusAdapter;

public class HREmployeeListPanel extends JPanel {

    private JTable employeeTable;
    private JTextField searchField;
    private JButton addEmployeeButton;
    private JButton addBulkButton;
    private JButton deleteEmployeeButton;
    private JButton viewEmployeeButton;
    private Employee loggedInEmployee;

    public HREmployeeListPanel(Employee loggedInEmployee) {

        this.loggedInEmployee = loggedInEmployee;

        setLayout(null);

        setBackground(new Color(227, 234, 231));

        setBounds(0, 0, 1030, 720);

        // =========================
        // BREADCRUMB
        // =========================

        JLabel breadcrumb =
                new JLabel(
                "Home > Employee List"
                );

        breadcrumb.setBounds(40, 45, 250, 20);

        breadcrumb.setForeground(
                new Color(120, 120, 120)
                );

        breadcrumb.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                12
                )
                );

        add(breadcrumb);

        // =========================
        // TITLE
        // =========================

        JLabel title =
                new JLabel(
                "Employee List"
                );

        title.setBounds(40, 90, 350, 40);

        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                32
                )
                );

        add(title);

        // =========================
        // SEARCH FIELD
        // =========================

        searchField =
                new PlaceholderTextField("Search");

        searchField.setBounds(
                40,
                145,
                230,
                36
                );

        searchField.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        searchField.setBorder(
                new CompoundBorder(
                new LineBorder(
                new Color(180, 180, 180)
                ),
                new EmptyBorder(
                0,
                10,
                0,
                10
                )
                )
                );

        add(searchField);

        // =========================
        // ADD EMPLOYEE BUTTON
        // =========================

        addEmployeeButton =
                createRoundedButton(
                "+ Add New Employee",
                new Color(33, 150, 243),
                Color.WHITE
                );

        addEmployeeButton.setBounds(
                280,
                145,
                170,
                36
                );

        add(addEmployeeButton);

        // =========================
        // ADD BULK BUTTON
        // =========================

        addBulkButton =
                createRoundedButton(
                "+ Add Bulk",
                new Color(18, 55, 120),
                Color.WHITE
                );

        addBulkButton.setBounds(
                460,
                145,
                110,
                36
                );

        add(addBulkButton);

        // =========================
        // DELETE EMPLOYEE BUTTON
        // =========================

        deleteEmployeeButton =
                createRoundedButton(
                "Remove",
                new Color(220,60,60),
                Color.WHITE
                );

        deleteEmployeeButton.setBounds(
                580,
                145,
                85,
                36
                );

        add(deleteEmployeeButton);

        // =========================
        // VIEW EMPLOYEE BUTTON
        // =========================
        viewEmployeeButton =
                createRoundedButton(
                "View",
                new Color(46,125,50),
                Color.WHITE
                );

        viewEmployeeButton.setBounds(
                675,
                145,
                75,
                36
                );

        add(viewEmployeeButton);

        // =========================
        // BUTTON EVENTS
        // =========================

        addEmployeeButton.addActionListener(
                e -> {

                    try {

                        new AddEmployeeDialog(this);

                    } catch (IOException ex) {

                        ex.printStackTrace();

                        JOptionPane.showMessageDialog(
                        this,
                        "Unable to open Add Employee window.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                );

        addBulkButton.addActionListener(e -> {

                    new BulkAddEmployeeDialog(
                    SwingUtilities.getWindowAncestor(this),
                    this
                    );
                });

        deleteEmployeeButton.addActionListener(e ->
                EmployeeRemoval.deactivateEmployee(
                employeeTable,
                this,
                this
                )
                );
        viewEmployeeButton.addActionListener(e -> {

                    int selectedRow =
                    employeeTable.getSelectedRow();

                    if(selectedRow == -1){

                        JOptionPane.showMessageDialog(
                        this,
                        "Please select an employee."
                        );

                        return;
                    }

                    selectedRow =
                    employeeTable.convertRowIndexToModel(
                    selectedRow
                    );

                    Window parent = SwingUtilities.getWindowAncestor(this);

                    new ViewEmployee(
                    parent,
                    employeeTable,
                    selectedRow,
                    this,
                    loggedInEmployee
                    );
                });

        // =========================
        // TABLE
        // =========================

        String[] columns = {

            "Employee No.",
                    "Name",
                    "Birthday",
                    "Address",
                    "Phone Number",
                    "SSS No.",
                    "Philhealth No.",
                    "Tin No.",
                    "Pag-ibig No.",
                    "Status",
                    "Position",
                    "Supervisor",
                    "Basic Salary",
                    "Rice Subsidy",
                    "Phone Allowance",
                    "Clothing Allowance",
                    "Gross Semi Monthly",
                    "Hourly Rate"
                };

        Object[][] data = loadEmployeeData();

        DefaultTableModel tableModel =
                new DefaultTableModel(
                columns,
                0
                ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
                    ) {
                return false;
            }
        };

        for(Object[] row : data){

            tableModel.addRow(row);
        }

        employeeTable = new JTable(tableModel) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
                    ) {
                return false;
            }
        };

        employeeTable.setDefaultEditor(
                Object.class,
                null
                );

        employeeTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
                );

        // =========================
        // LIVE SEARCH
        // =========================

        searchField.addKeyListener(
                new KeyAdapter() {

                    @Override
            public void keyReleased(
                    KeyEvent e
                    ) {

                        String search =
                        searchField.getText()
                        .trim();

                        TableRowSorter<DefaultTableModel> sorter =
                        new TableRowSorter<>(
                        tableModel
                        );

                        employeeTable.setRowSorter(sorter);

                        if(search.length() == 0){

                            sorter.setRowFilter(null);

                        } else {

                            sorter.setRowFilter(
                            RowFilter.regexFilter(
                            "(?i)" + search
                            )
                            );
                        }
                    }
                }
                );

        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(120);

        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(180);

        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(110);

        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(380);

        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(130);

        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(160);

        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(160);

        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(160);

        employeeTable.getColumnModel().getColumn(8).setPreferredWidth(160);

        employeeTable.getColumnModel().getColumn(9).setPreferredWidth(100);

        employeeTable.getColumnModel().getColumn(10).setPreferredWidth(180);

        employeeTable.getColumnModel().getColumn(11).setPreferredWidth(180);

        employeeTable.getColumnModel().getColumn(12).setPreferredWidth(130);

        employeeTable.getColumnModel().getColumn(13).setPreferredWidth(130);

        employeeTable.getColumnModel().getColumn(14).setPreferredWidth(130);

        employeeTable.getColumnModel().getColumn(15).setPreferredWidth(160);

        employeeTable.getColumnModel().getColumn(16).setPreferredWidth(160);

        employeeTable.getColumnModel().getColumn(17).setPreferredWidth(100);

        employeeTable.setRowHeight(44);

        employeeTable.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        employeeTable.getTableHeader().setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                15
                )
                );

        employeeTable.getTableHeader().setBackground(
                new Color(220, 220, 220)
                );

        employeeTable.getTableHeader().setOpaque(true);

        employeeTable.setShowGrid(false);

        employeeTable.setIntercellSpacing(
                new Dimension(0, 0)
                );

        employeeTable.setSelectionBackground(
                new Color(230, 240, 255)
                );

        JScrollPane scrollPane =
                new JScrollPane(employeeTable);

        scrollPane.setBounds(
                40,
                210,
                700,
                400
                );

        scrollPane.setBorder(
                new LineBorder(
                new Color(220, 220, 220)
                )
                );

        add(scrollPane);

        // =========================
        // ROW COLORS
        // =========================

        employeeTable.setDefaultRenderer(
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

                            String status =
                            table.getValueAt(row, 9).toString();

                            if(status.equalsIgnoreCase("Inactive")){

                                c.setBackground(
                                new Color(220,220,220)
                                );

                                c.setForeground(
                                Color.GRAY
                                );

                            } else {

                                c.setForeground(
                                Color.BLACK
                                );

                                if (row % 2 == 0) {

                                    c.setBackground(
                                    new Color(245, 245, 245)
                                    );

                                } else {

                                    c.setBackground(
                                    new Color(230, 230, 230)
                                    );
                                }
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
        // PAGINATION
        // =========================

        JLabel pagination =
                new JLabel();

        pagination.setBounds(
                620,
                635,
                260,
                20
                );

        pagination.setForeground(
                new Color(100, 100, 100)
                );

        pagination.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                12
                )
                );

        add(pagination);
    }

    // =========================
    // LOAD CSV DATA
    // =========================

    private Object[][] loadEmployeeData() {

        ArrayList<Object[]> rows =
                new ArrayList<>();

        DecimalFormat moneyFormat =
                new DecimalFormat("₱ #,##0.00");

        try {

            String[] files = {

                "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    };

            for(String filePath : files){

                File file =
                        new File(filePath);

                if(!file.exists()){
                    continue;
                }

                try(
                        BufferedReader reader =
                        new BufferedReader(
                        new FileReader(file)
                        )
                        ){

                    String line;

                    reader.readLine();

                    while((line = reader.readLine()) != null){

                        String[] data =
                                line.split(
                                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
                                );

                        if(data.length < 19){
                            continue;
                        }

                        if(data[10].trim().equalsIgnoreCase("Inactive")){
                            continue;
                        }

                        String employeeNumber =
                                data[0];

                        String fullName =
                                data[2] + " " + data[1];

                        String birthday =
                                data[3];

                        String address =
                                cleanCSVField(data[4]);

                        String phoneNumber =
                                data[5];

                        String sssNumber =
                                formatSSS(data[6]);

                        String philhealthNumber =
                                formatGovernmentId(data[7]);

                        String tinNumber =
                                formatTIN(data[8]);

                        String pagibigNumber =
                                formatGovernmentId(data[9]);

                        String status =
                                "Active";

                        String position =
                                data[11];

                        String supervisor =
                                cleanCSVField(data[12]);

                        String basicSalary =
                                moneyFormat.format(
                                Double.parseDouble(
                                cleanCSVField(data[13])
                                )
                                );

                        String riceSubsidy =
                                moneyFormat.format(
                                Double.parseDouble(
                                cleanCSVField(data[14])
                                )
                                );

                        String phoneAllowance =
                                moneyFormat.format(
                                Double.parseDouble(
                                cleanCSVField(data[15])
                                )
                                );

                        String clothingAllowance =
                                moneyFormat.format(
                                Double.parseDouble(
                                cleanCSVField(data[16])
                                )
                                );

                        String grossSemiMonthly =
                                moneyFormat.format(
                                Double.parseDouble(
                                cleanCSVField(data[17])
                                )
                                );

                        String hourlyRate =
                                moneyFormat.format(
                                Double.parseDouble(
                                data[18]
                                )
                                );

                        rows.add(
                                new Object[]{

                                    employeeNumber,
                                    fullName,
                                    birthday,
                                    address,
                                    phoneNumber,
                                    sssNumber,
                                    philhealthNumber,
                                    tinNumber,
                                    pagibigNumber,
                                    status,
                                    position,
                                    supervisor,
                                    basicSalary,
                                    riceSubsidy,
                                    phoneAllowance,
                                    clothingAllowance,
                                    grossSemiMonthly,
                                    hourlyRate
                                }
                                );
                    }
                }
            }

        } catch(Exception e){

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Failed to load employee data."
                    );
        }

        Object[][] employeeData =
                new Object[rows.size()][18];

        for(int i = 0; i < rows.size(); i++){

            employeeData[i] =
                    rows.get(i);
        }

        return employeeData;
    }

    // =========================
    // REFRESH TABLE
    // =========================
    public void refreshTable() {

        DefaultTableModel model =
                (DefaultTableModel)
                employeeTable.getModel();

        model.setRowCount(0);

        Object[][] data =
                loadEmployeeData();

        for(Object[] row : data){

            model.addRow(row);
        }
    }

    // =========================
    // PLACEHOLDER SEARCH
    // =========================

    public static class PlaceholderTextField
            extends JTextField {

        private final String placeholder;

        public PlaceholderTextField(
                String placeholder
                ){

            this.placeholder =
                    placeholder;

            addFocusListener(
                    new FocusAdapter() {

                        @Override
                public void focusGained(
                        FocusEvent e
                        ){
                            repaint();
                        }

                        @Override
                public void focusLost(
                        FocusEvent e
                        ){
                            repaint();
                        }
                    }
                    );
        }

        @Override
        protected void paintComponent(
                Graphics g
                ){

            super.paintComponent(g);

            if(getText().isEmpty()
                    && !isFocusOwner()){

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setColor(
                        new Color(170,170,170)
                        );

                g2.drawString(
                        placeholder,
                        getInsets().left + 5,
                        g.getFontMetrics()
                        .getMaxAscent()
                        + getInsets().top + 2
                        );

                g2.dispose();
            }
        }
    }

    //FORMAT GOVERNMENT ID
    private String formatGovernmentId(String value) {

        value = value.trim();

        if(value.contains("E") || value.contains("e")) {

            return String.format(
                    "%.0f",
                    Double.parseDouble(value)
                    );
        }

        return value;
    }

    //FORMAT SSS AND TIN
    private String formatSSS(String value) {

        String digits =
                value.replaceAll("[^0-9]", "");

        if (digits.length() == 10) {

            return digits.substring(0, 2)
                    + "-"
                    + digits.substring(2, 9)
                    + "-"
                    + digits.substring(9);
        }

        return value;
    }

    private String formatTIN(String value) {

        String digits =
                value.replaceAll("[^0-9]", "");

        if (digits.length() == 12) {

            return digits.substring(0, 3)
                    + "-"
                    + digits.substring(3, 6)
                    + "-"
                    + digits.substring(6, 9)
                    + "-"
                    + digits.substring(9);
        }

        return value;
    }

    // =========================
    // CLEAN CSV HELPER
    // =========================

    private String cleanCSVField(String value) {

        return value
                .replace("\"", "")
                .replace(",","")
                .trim();
    }

    private JButton createRoundedButton(
            String text,
            Color background,
            Color foreground
            ) {

        JButton button = new JButton(text);

        button.setBackground(background);
        button.setForeground(foreground);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                13
                )
                );

        button.setCursor(
                new Cursor(
                Cursor.HAND_CURSOR
                )
                );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                8,
                15,
                8,
                15
                )
                );

        return button;
    }
}
