package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PayrollPanel extends JPanel {

    private JTable payrollTable;
    private JTextField searchField;
    private JComboBox<String> cutoffDropdown;
    private JButton processPayrollButton;
    private DefaultTableModel tableModel;
    private JLabel employeeNumbers;
    private JLabel payrollMonth;
    private Employee employee;
    private JButton downloadPdfButton;
    private JLabel totalGrossPayLabel;
    private JLabel totalDeductionsLabel;
    private JLabel averageNetPayLabel;

    private final String[] NORMAL_COLUMNS = {
        "Employee ID",
                "Name",
                "Hourly Rate",
                "Hours Worked",
                "OT Hours",
                "OT Pay",
                "Gross Pay",
                "Allowance",
                "Deductions",
                "Net Salary",
                "Status"
            };

    private final String[] ALL_COLUMNS = {
        "Employee Name",
                "Employee ID",
                "Cutoff",
                "Hourly Rate",
                "Hours Worked",
                "OT Hours",
                "OT Pay",
                "Gross Pay",
                "Allowance",
                "Deductions",
                "Net Salary",
                "Status"
            };

    public PayrollPanel(Employee employee) {

        this.employee = employee;

        setLayout(null);

        setBackground(new Color(227, 234, 231));

        setBounds(0, 0, 1030, 720);

        // =========================================
        // TITLE
        // =========================================

        JLabel title = new JLabel("Payroll");

        title.setBounds(40, 30, 250, 45);

        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                38
                )
                );

        add(title);

        // =========================================
        // DOWNLOAD PDF BUTTON
        // =========================================

        downloadPdfButton =
                createRoundedButton(
                "Download PDF",
                new Color(79, 141, 168),
                Color.WHITE
                );

        downloadPdfButton.setBounds(
                580,
                75,
                145,
                36
                );

        add(downloadPdfButton);

        downloadPdfButton.addActionListener(e -> {
                    downloadPayrollPDF();
                });

        // =========================================
        // PAYROLL CARD
        // =========================================

        JPanel payrollCard =
                new JPanel();

        payrollCard.setLayout(null);

        payrollCard.setBackground(Color.WHITE);

        payrollCard.setBounds(
                40,
                115,
                210,
                150
                );

        payrollCard.setBorder(
                new LineBorder(
                new Color(220,220,220),
                1,
                true
                )
                );

        add(payrollCard);

        JLabel payrollExpenseLabel = new JLabel("Number of Employees");

        payrollExpenseLabel.setBounds(
                15,
                15,
                180,
                30
                );

        payrollExpenseLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                16
                )
                );

        payrollCard.add(payrollExpenseLabel);



        employeeNumbers = new JLabel("0");
        employeeNumbers.setHorizontalAlignment(SwingConstants.CENTER);

        employeeNumbers.setBounds(
                0,
                50,
                210,
                50
                );

        employeeNumbers.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                32
                )
                );

        payrollCard.add(employeeNumbers);

        payrollMonth = new JLabel("No cutoff selected");

        payrollMonth.setBounds(
                10,
                115,
                230,
                20
                );

        payrollMonth.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        payrollMonth.setForeground(
                new Color(90,90,90)
                );

        payrollCard.add(payrollMonth);

        // =========================================
        // CUTOFF PANEL
        // =========================================

        JPanel cutoffCard =
                new JPanel();

        cutoffCard.setLayout(null);

        cutoffCard.setBackground(Color.WHITE);

        cutoffCard.setBounds(
                250,
                115,
                490,
                150
                );

        cutoffCard.setBorder(
                new LineBorder(
                new Color(220,220,220),
                1,
                true
                )
                );

        add(cutoffCard);

        JLabel cutoffLabel =
                new JLabel(
                "Select Cutoff Period:"
                );

        cutoffLabel.setBounds(
                15,
                20,
                180,
                25
                );

        cutoffLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                16
                )
                );

        cutoffCard.add(cutoffLabel);

        cutoffDropdown = new JComboBox<>(getCutoffOptions());
        ((JLabel) cutoffDropdown.getRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        cutoffDropdown.setBounds(
                175,
                18,
                150,
                32
                );

        cutoffDropdown.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                13
                )
                );

        cutoffCard.add(cutoffDropdown);

        // =========================================
        // PROCESS PAYROLL BUTTON
        // =========================================

        processPayrollButton =
                createRoundedButton(
                "Process Payroll",
                new Color(10,180,50),
                Color.WHITE
                );

        processPayrollButton.setBounds(
                330,
                18,
                145,
                32
                );

        cutoffCard.add(processPayrollButton);

        JPopupMenu payrollMenu =
                new JPopupMenu();

        JMenuItem singleEmployee =
                new JMenuItem(
                "Single Employee"
                );

        JMenuItem bulkProcessing =
                new JMenuItem(
                "Bulk Processing"
                );

        payrollMenu.add(singleEmployee);

        payrollMenu.add(bulkProcessing);

        processPayrollButton.addActionListener(e -> {

                    String selectedCutoff =
                    cutoffDropdown.getSelectedItem().toString();

                    if(selectedCutoff.equals("All")){

                        tableModel.setColumnIdentifiers(ALL_COLUMNS);

                        SwingUtilities.invokeLater(() -> {
                            setColumnWidths();
                            centerTableText();
                        });

                    }else{

                        tableModel.setColumnIdentifiers(NORMAL_COLUMNS);

                        SwingUtilities.invokeLater(() -> {
                            setColumnWidths();
                            centerTableText();
                        });
                    }

                    if(selectedCutoff.equals("All")){

                        // Automatically process all employees
                        bulkProcessing.doClick();

                    }else{

                        // Show processing options
                        payrollMenu.show(
                        processPayrollButton,
                        0,
                        processPayrollButton.getHeight()
                        );
                    }
                });

        // =========================================
        // TIMELINE BAR
        // =========================================

        JPanel timelineBar =
                new JPanel();

        timelineBar.setLayout(null);

        timelineBar.setBackground(
                new Color(25,160,210)
                );

        timelineBar.setBounds(
                10,
                65,
                470,
                40
                );

        timelineBar.setBorder(
                new LineBorder(
                Color.BLACK,
                1,
                true
                )
                );

        cutoffCard.add(timelineBar);

        JLabel period1 =
                new JLabel(
                getSelectedCutoff()
                );

        period1.setForeground(Color.WHITE);

        period1.setBounds(
                15,
                8,
                160,
                25
                );

        period1.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                15
                )
                );

        timelineBar.add(period1);

        JPanel dateCircle =
                new JPanel();

        dateCircle.setLayout(null);

        dateCircle.setBackground(
                new Color(100,190,220)
                );

        dateCircle.setBounds(
                160,
                -8,
                60,
                60
                );

        dateCircle.setBorder(
                new LineBorder(
                Color.BLACK,
                2,
                true
                )
                );

        timelineBar.add(dateCircle);

        JLabel dateLabel =
                new JLabel(
                getCurrentDate()
                );

        dateLabel.setBounds(
                8,
                17,
                55,
                20
                );

        dateLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        dateCircle.add(dateLabel);

        JLabel period2 =
                new JLabel(
                getProcessingPeriodDynamic()
                );

        period2.setHorizontalAlignment(SwingConstants.CENTER);
        period2.setForeground(Color.WHITE);

        period2.setBounds(
                205,
                8,
                160,
                25
                );

        period2.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                15
                )
                );

        timelineBar.add(period2);

        JLabel paydayLabel =
                new JLabel(
                getPaydayDynamic()
                );

        paydayLabel.setForeground(Color.WHITE);

        paydayLabel.setBounds(
                410,
                8,
                60,
                25
                );

        paydayLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                16
                )
                );

        timelineBar.add(paydayLabel);

        // =========================================
        // TIMELINE LABELS
        // =========================================

        JLabel cutoffText = new JLabel("Cutoff Period");

        cutoffText.setBounds(
                45,
                110,
                120,
                20
                );

        cutoffText.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        cutoffText.setForeground(
                new Color(70,70,70)
                );

        cutoffCard.add(cutoffText);

        JLabel processingText =
                new JLabel("Processing Period");

        processingText.setBounds(
                245,
                110,
                140,
                20
                );

        processingText.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        processingText.setForeground(
                new Color(70,70,70)
                );

        cutoffCard.add(processingText);

        JLabel paydayText =
                new JLabel("Payday");

        paydayText.setBounds(
                405,
                110,
                80,
                20
                );

        paydayText.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        paydayText.setForeground(
                new Color(70,70,70)
                );

        cutoffCard.add(paydayText);

        cutoffDropdown.addActionListener(e -> {

                    period1.setText(
                    getSelectedCutoff()
                    );

                    period2.setText(
                    getProcessingPeriodDynamic()
                    );

                    paydayLabel.setText(
                    getPaydayDynamic()
                    );
                });

        // =========================================
        // SEARCH FIELD
        // =========================================

        searchField = new JTextField();

        searchField.setBounds(
                40,
                280,
                220,
                38
                );

        searchField.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        searchField.setText("Enter ID, Name, Position");
        searchField.setForeground(Color.GRAY);

        searchField.setBorder(
                new CompoundBorder(
                new LineBorder(
                new Color(180,180,180)
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

        searchField.addFocusListener(new FocusAdapter(){
                    @Override
            public void focusGained(
                    FocusEvent e
                    ){

                        if(searchField.getText().equals(
                        "Enter ID, Name, Position"
                        )){

                            searchField.setText("");

                            searchField.setForeground(
                            Color.BLACK
                            );
                        }
                    }

                    @Override
            public void focusLost(
                    FocusEvent e
                    ){

                        if(searchField.getText()
                        .trim()
                        .isEmpty()){

                            searchField.setText(
                            "Enter ID, Name, Position"
                            );

                            searchField.setForeground(
                            Color.GRAY
                            );
                        }
                    }
                }
                );

        // =========================================
        // SORT DROPDOWN
        // =========================================

        JComboBox<String> sortDropdown =
                new JComboBox<>(
                new String[]{

                    "Sort by",
                    "Employee ID",
                    "Name",
                    "Salary",
                    "Status"
                }
                );

        sortDropdown.setBounds(
                275,
                280,
                120,
                38
                );

        add(sortDropdown);

        // =========================================
        // TABLE
        // =========================================

        String[] columns = {

            "Employee ID",
                    "Name",
                    "Hourly Rate",
                    "Hours Worked",
                    "OT Hours",
                    "OT Pay",
                    "Gross Pay",
                    "Allowance",
                    "Deductions",
                    "Net Salary",
                    "Status"
                };

        Object[][] data = {};

        tableModel =
                new DefaultTableModel(
                data,
                columns
                ){

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
                    ){

                return column == 10;
            }
        };

        payrollTable = new JTable(tableModel);

        setColumnWidths();

        centerTableText();

        payrollTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JComboBox<String> statusDropdown =
                new JComboBox<>(
                new String[]{

                    "Validated",
                    "Disputed",
                    "Pending"
                }
                );

        payrollTable.getColumnModel()
                .getColumn(10)
                .setCellEditor(
                new DefaultCellEditor(statusDropdown)
                );

        payrollTable.setRowHeight(52);

        payrollTable.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                13
                )
                );

        payrollTable.getTableHeader().setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        payrollTable.getTableHeader().setBackground(
                new Color(220,220,220)
                );

        payrollTable.setShowGrid(false);

        payrollTable.setIntercellSpacing(
                new Dimension(0,0)
                );

        JScrollPane scrollPane =
                new JScrollPane(
                payrollTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
                );

        scrollPane.setBounds(
                40,
                325,
                700,
                275
                );

        add(scrollPane);

        // =========================================
        // PAYROLL SUMMARY
        // =========================================

        JLabel grossTitle = new JLabel("Total Gross Pay:");
        grossTitle.setBounds(40, 610, 140, 25);
        grossTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(grossTitle);

        totalGrossPayLabel = new JLabel("₱0.00");
        totalGrossPayLabel.setBounds(160, 610, 120, 25);
        totalGrossPayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(totalGrossPayLabel);

        JLabel deductionTitle = new JLabel("Total Deductions:");
        deductionTitle.setBounds(300, 610, 140, 25);
        deductionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(deductionTitle);

        totalDeductionsLabel = new JLabel("₱0.00");
        totalDeductionsLabel.setBounds(430, 610, 120, 25);
        totalDeductionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(totalDeductionsLabel);

        JLabel averageTitle = new JLabel("Average Net Pay:");
        averageTitle.setBounds(530, 610, 140, 25);
        averageTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(averageTitle);

        averageNetPayLabel = new JLabel("₱0.00");
        averageNetPayLabel.setBounds(660, 610, 120, 25);
        averageNetPayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(averageNetPayLabel);

        // =========================================
        // LIVE SEARCH
        // =========================================

        searchField.addKeyListener(
                new KeyAdapter(){

                    @Override
            public void keyReleased(
                    KeyEvent e
                    ){

                        String search =
                        searchField.getText()
                        .trim();

                        if(search.equals("Enter ID, Name, Position" )){
                            search = "";
                        }

                        TableRowSorter<DefaultTableModel> sorter =
                        new TableRowSorter<>(
                        tableModel
                        );

                        payrollTable.setRowSorter(sorter);

                        if(search.isEmpty()){

                            sorter.setRowFilter(null);

                        } else {
                            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search));
                        }
                    }
                }
                );

        // =========================================
        // SORTING
        // =========================================

        sortDropdown.addActionListener(e -> {

                    String selected =
                    sortDropdown.getSelectedItem().toString();

                    TableRowSorter<DefaultTableModel> sorter =
                    new TableRowSorter<>(tableModel);

                    payrollTable.setRowSorter(sorter);

                    List<RowSorter.SortKey> sortKeys =
                    new ArrayList<>();

                    switch(selected){

                        case "Employee ID":

                            sortKeys.add(
                            new RowSorter.SortKey(
                            0,
                            SortOrder.ASCENDING
                            )
                            );

                            break;

                        case "Name":

                            sortKeys.add(
                            new RowSorter.SortKey(
                            1,
                            SortOrder.ASCENDING
                            )
                            );

                            break;

                        case "Salary":

                            sortKeys.add(
                            new RowSorter.SortKey(
                            8,
                            SortOrder.DESCENDING
                            )
                            );

                            break;

                        case "Status":

                            sortKeys.add(
                            new RowSorter.SortKey(
                            10,
                            SortOrder.ASCENDING
                            )
                            );

                            break;
                    }

                    sorter.setSortKeys(sortKeys);
                });

        // =========================================
        // SINGLE EMPLOYEE PROCESS
        // =========================================

        singleEmployee.addActionListener(e -> {

                    String employeeNumber =
                    JOptionPane.showInputDialog(
                    null,
                    "Enter Employee Number"
                    );

                    if(employeeNumber == null){

                        return;
                    }

                    tableModel.setRowCount(0);

                    try {

                        java.io.BufferedReader reader =
                        new java.io.BufferedReader(
                        new java.io.FileReader(
                        "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                        )
                        );

                        String line;

                        reader.readLine();

                        boolean found = false;

                        String selectedCutoff =
                        cutoffDropdown
                        .getSelectedItem()
                        .toString();

                        EmployeeDataManager manager = new EmployeeDataManager();

                        manager.loadAttendanceData(
                        "src/main/resources/MotorPH_Employee Data - Attendance Record.csv"
                        );

                        ArrayList<AttendanceRecord> attendanceRecords = manager.getAttendanceData();



                        while((line = reader.readLine()) != null){

                            String[] rowData =
                            line.split(
                            ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"
                            );

                            if(rowData.length >= 14){

                                String employeeId =
                                rowData[0].trim();

                                if(employeeId.equals(employeeNumber)){

                                    found = true;

                                    String firstName =
                                    rowData[1].trim();

                                    String lastName =
                                    rowData[2].trim();

                                    String fullName =
                                    firstName
                                    + " "
                                    + lastName;

                                    double hourlyRate = Double.parseDouble(
                                    rowData[18]
                                    .replace("\"","")
                                    .replace(",","")
                                    .trim()
                                    );

                                    double basicSalary = Double.parseDouble(
                                    rowData[17]
                                    .replace("\"", "")
                                    .replace(",", "")
                                    .trim()
                                    );

                                    double hoursWorked = 0;
                                    double overtimeHours = 0;

                                    for(AttendanceRecord record : attendanceRecords){

                                        if(record.getEmployeeNumber().equals(employeeId)
                                        && isRecordInCutoff(record, selectedCutoff)){
                                            hoursWorked += record.getTotalHoursWorked();
                                            overtimeHours += record.getOvertimeHours();
                                        }
                                    }

                                    double riceSubsidy = Double.parseDouble(
                                    rowData[14]
                                    .replace("\"","")
                                    .replace(",","")
                                    .trim()
                                    );

                                    double phoneAllowance = Double.parseDouble(
                                    rowData[15]
                                    .replace("\"","")
                                    .replace(",","")
                                    .trim()
                                    );

                                    double clothingAllowance = Double.parseDouble(
                                    rowData[16]
                                    .replace("\"","")
                                    .replace(",","")
                                    .trim()
                                    );

                                    double allowance = 0;

                                    if(hoursWorked > 0 || overtimeHours > 0){

                                        allowance =
                                        (riceSubsidy
                                        + phoneAllowance
                                        + clothingAllowance) / 2;
                                    }


                                    double overtimePay =
                                    overtimeHours
                                    * hourlyRate
                                    * 1.25;

                                    double grossPay =
                                    SalaryComputationModule.computeGrossPay(
                                    hourlyRate,
                                    hoursWorked,
                                    overtimeHours
                                    );


                                    Deduction deduction =
                                    new Deduction();

                                    double sss = 0;
                                    double philHealth = 0;
                                    double pagIbig = 0;
                                    double tax = 0;

                                    if(grossPay > 0){

                                        double grossCompensation = grossPay + allowance;

                                        sss = deduction.calculateSSS(basicSalary);

                                        philHealth = deduction.calculatePhilHealth(basicSalary);

                                        pagIbig = deduction.calculatePagIbig(basicSalary);

                                        double taxableIncome =
                                        grossCompensation
                                        - sss
                                        - philHealth
                                        - pagIbig;

                                        tax = deduction.calculateTax(taxableIncome);
                                    }

                                    double deductions =
                                    sss
                                    + philHealth
                                    + pagIbig
                                    + tax;

                                    double totalSalary =
                                    SalaryComputationModule.computeNetPay(
                                    grossPay,
                                    allowance,
                                    deductions
                                    );

                                    totalGrossPayLabel.setText(
                                    String.format("₱%,.2f", grossPay));

                                    totalDeductionsLabel.setText(
                                    String.format("₱%,.2f", deductions));

                                    averageNetPayLabel.setText(
                                    String.format("₱%,.2f", totalSalary));

                                    updateEmployeeCount(1);
                                    updateProcessingTimestamp();

                                    tableModel.addRow(
                                    new Object[]{

                                        employeeId,

                                        fullName,

                                        String.format("₱%,.2f", hourlyRate ),

                                        String.format("%.2f", hoursWorked),

                                        String.format("%.2f", overtimeHours),

                                        String.format("₱%,.2f", overtimePay),

                                        String.format("₱%,.2f", grossPay),

                                        String.format("₱%,.2f", allowance),

                                        String.format("₱%,.2f",deductions),

                                        String.format("₱%,.2f",totalSalary),

                                        "Validated"
                                    }
                                    );

                                    break;
                                }
                            }
                        }

                        reader.close();

                        if(found){

                            JOptionPane.showMessageDialog(
                            null,
                            "Payroll processed successfully."
                            );

                        } else {

                            JOptionPane.showMessageDialog(
                            null,
                            "Employee number not found."
                            );
                        }

                    } catch(Exception ex){

                        ex.printStackTrace();
                    }
                });

        // =========================================
        // BULK PROCESS
        // =========================================

        bulkProcessing.addActionListener(e -> {

                    tableModel.setRowCount(0);

                    String selectedCutoff = cutoffDropdown.getSelectedItem().toString();

                    if(selectedCutoff.equals("All")){

                        tableModel.setColumnIdentifiers(ALL_COLUMNS);
                        processAllCutoffs();
                        return;

                    }

                    tableModel.setColumnIdentifiers(NORMAL_COLUMNS);
                    setColumnWidths();
                    centerTableText();

                    try {

                        double totalGrossPay = 0;
                        double totalDeductions = 0;
                        double totalNetPay = 0;
                        int employeeCount = 0;

                        BufferedReader employeeReader =
                        new BufferedReader(
                        new java.io.FileReader(
                        "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                        )
                        );

                        String employeeLine;

                        employeeReader.readLine();

                        EmployeeDataManager manager = new EmployeeDataManager();

                        manager.loadAttendanceData(
                        "src/main/resources/MotorPH_Employee Data - Attendance Record.csv"
                        );

                        ArrayList<AttendanceRecord> attendanceRecords = manager.getAttendanceData();

                        while((employeeLine = employeeReader.readLine()) != null){

                            String[] rowData =
                            employeeLine.split(
                            ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"
                            );

                            if(rowData.length >= 14){

                                String employeeId =
                                rowData[0].trim();

                                String firstName =
                                rowData[1].trim();

                                String lastName =
                                rowData[2].trim();

                                String fullName =
                                firstName
                                + " "
                                + lastName;

                                double hourlyRate = Double.parseDouble(
                                rowData[18]
                                .replace("\"","")
                                .replace(",","")
                                .trim()
                                );

                                double basicSalary = Double.parseDouble(
                                rowData[17]
                                .replace("\"", "")
                                .replace(",", "")
                                .trim()
                                );

                                double hoursWorked = 0;
                                double overtimeHours = 0;

                                for(AttendanceRecord record : attendanceRecords){

                                    if(record.getEmployeeNumber().equals(employeeId)
                                    && isRecordInCutoff(record, selectedCutoff)){
                                        hoursWorked += record.getTotalHoursWorked();
                                        overtimeHours += record.getOvertimeHours();
                                    }
                                }

                                double overtimePay = overtimeHours * hourlyRate * 1.25;

                                double grossPay =
                                SalaryComputationModule.computeGrossPay(
                                hourlyRate,
                                hoursWorked,
                                overtimeHours
                                );

                                double riceSubsidy = Double.parseDouble(
                                rowData[14]
                                .replace("\"","")
                                .replace(",","")
                                .trim()
                                );

                                double phoneAllowance =
                                Double.parseDouble(
                                rowData[15]
                                .replace("\"","")
                                .replace(",","")
                                .trim()
                                );

                                double clothingAllowance =
                                Double.parseDouble(
                                rowData[16]
                                .replace("\"","")
                                .replace(",","")
                                .trim()
                                );

                                double allowance = 0;

                                if(hoursWorked > 0 || overtimeHours > 0){

                                    allowance =
                                    (riceSubsidy
                                    + phoneAllowance
                                    + clothingAllowance) / 2;
                                }

                                Deduction deduction = new Deduction();

                                double sss = 0;
                                double philHealth = 0;
                                double pagIbig = 0;
                                double tax = 0;

                                if(grossPay > 0){

                                    double grossCompensation = grossPay + allowance;

                                    sss = deduction.calculateSSS(basicSalary);

                                    philHealth = deduction.calculatePhilHealth(basicSalary);

                                    pagIbig = deduction.calculatePagIbig(basicSalary);

                                    double taxableIncome =
                                    grossCompensation
                                    - sss
                                    - philHealth
                                    - pagIbig;

                                    tax = deduction.calculateTax(taxableIncome);
                                }

                                double deductions =
                                sss
                                + philHealth
                                + pagIbig
                                + tax;

                                double totalSalary =
                                SalaryComputationModule.computeNetPay(
                                grossPay,
                                allowance,
                                deductions
                                );


                                totalGrossPay += grossPay;
                                totalDeductions += deductions;
                                totalNetPay += totalSalary;
                                employeeCount++;

                                tableModel.addRow(
                                new Object[]{

                                    employeeId,

                                    fullName,

                                    String.format("₱%,.2f", hourlyRate),

                                    String.format("%.2f", hoursWorked),

                                    String.format("%.2f", overtimeHours),

                                    String.format("₱%,.2f",overtimePay),

                                    String.format("₱%,.2f", grossPay),

                                    String.format("₱%,.2f", allowance),

                                    String.format("₱%,.2f", deductions),

                                    String.format("₱%,.2f", totalSalary),

                                    "Validated"
                                }
                                );
                            }
                        }

                        employeeReader.close();

                        updatePayrollSummary(
                        employeeCount,
                        totalGrossPay,
                        totalDeductions,
                        totalNetPay);

                        JOptionPane.showMessageDialog(
                        null,
                        "Bulk payroll processing completed."
                        );

                    } catch(Exception ex){

                        ex.printStackTrace();

                        JOptionPane.showMessageDialog(
                        null,
                        "Error processing payroll:\n"
                        + ex.getMessage()
                        );
                    }
                });
    }

    private boolean isRecordInCutoff(AttendanceRecord record, String selectedCutoff){

        if(selectedCutoff.equals("All")){
            return true;
        }

        String date = record.getDate();

        String[] parts = date.split("/");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        String[] cutoffParts = selectedCutoff.split(" ");

        String monthText = cutoffParts[0];

        int cutoffYear = Integer.parseInt(cutoffParts[2]);

        int cutoffMonth = switch(monthText){

            case "Jan" -> 1;
            case "Feb" -> 2;
            case "Mar" -> 3;
            case "Apr" -> 4;
            case "May" -> 5;
            case "Jun" -> 6;
            case "Jul" -> 7;
            case "Aug" -> 8;
            case "Sep" -> 9;
            case "Oct" -> 10;
            case "Nov" -> 11;
            case "Dec" -> 12;
            default -> 0;
        };

        if(month != cutoffMonth){
            return false;
        }

        if(year != cutoffYear){
            return false;
        }

        if(selectedCutoff.contains("1st-15th")){
            return day >= 1 && day <= 15;
        } else {
            return day >= 16;
        }
    }

    // =========================================
    // DATE METHODS
    // =========================================

    private String getCurrentMonth(){

        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
        return now.format(formatter);
    }

    private String getCurrentDate(){
        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        return now.format(formatter);
    }

    private String getAttendanceYear() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                    getClass().getResourceAsStream(
                    "/MotorPH_Employee Data - Attendance Record.csv"
                    )
                    )
                    );

            reader.readLine(); // skip header

                    String line = reader.readLine();

            if(line != null){

                String[] data = line.split(",");
                String dateText = data[3].trim();
                String[] parts = dateText.split("/");
                reader.close();
                return parts[2];
            }

            reader.close();

        } catch(Exception ex){

            ex.printStackTrace();
        }

        return String.valueOf(
                LocalDate.now().getYear()
                );
    }

    private String getSelectedCutoff(){

        return cutoffDropdown
                .getSelectedItem()
                .toString();
    }

    private String getProcessingPeriodDynamic(){

        String selected =
                cutoffDropdown
                .getSelectedItem()
                .toString();

        if(selected.contains("1st-15th")){

            return getCurrentMonth() + " 16th-30th";

        } else {

            return getCurrentMonth() + " 1st-15th";
        }
    }

    private String getPaydayDynamic(){

        String selected =
                cutoffDropdown
                .getSelectedItem()
                .toString();

        if(selected.contains("1st-15th")){

            return "30";

        } else {

            return "15";
        }
    }

    private void updateProcessingTimestamp() {

        payrollMonth.setText(
                "Cutoff: " + cutoffDropdown.getSelectedItem().toString()
                );
    }


    // =========================================
    // BUTTON DESIGN
    // =========================================

    private JButton createRoundedButton(
            String text,
            Color bgColor,
            Color textColor
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
            protected void paintBorder(
                    Graphics g
                    ){

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
                14
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

    private void processAllCutoffs() {

        tableModel.setRowCount(0);

        String[] allCutoffs = getCutoffOptions();

        List<String> cutoffList = new ArrayList<>();

        for (String cutoff : allCutoffs) {

            if (!cutoff.equals("All")) {
                cutoffList.add(cutoff);
            }
        }

        try {



            double totalGrossPay = 0;
            double totalDeductions = 0;
            double totalNetPay = 0;

            BufferedReader employeeReader =
                    new BufferedReader(
                    new java.io.FileReader(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"));

            employeeReader.readLine();

            EmployeeDataManager manager = new EmployeeDataManager();
            manager.loadAttendanceData(
                    "src/main/resources/MotorPH_Employee Data - Attendance Record.csv");

            ArrayList<AttendanceRecord> attendanceRecords =
                    manager.getAttendanceData();

            String employeeLine;

            int employeeCount = 0;

            while ((employeeLine = employeeReader.readLine()) != null) {

                String[] rowData =
                        employeeLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (rowData.length < 19) {
                    continue;
                }

                String employeeId = rowData[0].trim();
                employeeCount++;

                String fullName = rowData[1].trim() + " " + rowData[2].trim();

                double hourlyRate = Double.parseDouble(
                        rowData[18]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());

                double basicSalary = Double.parseDouble(
                        rowData[17]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim()
                        );

                double riceSubsidy = Double.parseDouble(
                        rowData[14]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());

                double phoneAllowance = Double.parseDouble(
                        rowData[15]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());

                double clothingAllowance = Double.parseDouble(
                        rowData[16]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());

                for (String cutoff : cutoffList) {

                    double hoursWorked = 0;
                    double overtimeHours = 0;

                    for (AttendanceRecord record : attendanceRecords) {

                        if (record.getEmployeeNumber().equals(employeeId)
                                && isRecordInCutoff(record, cutoff)) {

                            hoursWorked += record.getTotalHoursWorked();
                            overtimeHours += record.getOvertimeHours();
                        }
                    }

                    double overtimePay =
                            overtimeHours * hourlyRate * 1.25;

                    double grossPay =
                            SalaryComputationModule.computeGrossPay(
                            hourlyRate,
                            hoursWorked,
                            overtimeHours);

                    double allowance = 0;
                    double sss = 0;
                    double philHealth = 0;
                    double pagIbig = 0;
                    double tax = 0;

                    if (hoursWorked > 0 || overtimeHours > 0) {

                        allowance =
                                (riceSubsidy
                                + phoneAllowance
                                + clothingAllowance) / 2;

                        Deduction deduction = new Deduction();

                        double grossCompensation = grossPay + allowance;

                        sss = deduction.calculateSSS(basicSalary);

                        philHealth = deduction.calculatePhilHealth(basicSalary);

                        pagIbig = deduction.calculatePagIbig(basicSalary);

                        double taxableIncome =
                                grossCompensation
                                - sss
                                - philHealth
                                - pagIbig;

                        tax = deduction.calculateTax(taxableIncome);
                    }

                    double deductions =
                            sss
                            + philHealth
                            + pagIbig
                            + tax;

                    double totalSalary =
                            SalaryComputationModule.computeNetPay(
                            grossPay,
                            allowance,
                            deductions);



                    totalGrossPay += grossPay;
                    totalDeductions += deductions;
                    totalNetPay += totalSalary;

                    tableModel.addRow(new Object[]{
                                fullName,
                                employeeId,
                                cutoff,
                                String.format("₱%,.2f", hourlyRate),
                                String.format("%.2f", hoursWorked),
                                String.format("%.2f", overtimeHours),
                                String.format("₱%,.2f", overtimePay),
                                String.format("₱%,.2f", grossPay),
                                String.format("₱%,.2f", allowance),
                                String.format("₱%,.2f", deductions),
                                String.format("₱%,.2f", totalSalary),
                                "Validated"
                            });
                }
            }

            employeeReader.close();

            updatePayrollSummary(
                    employeeCount,
                    totalGrossPay,
                    totalDeductions,
                    totalNetPay);

            JOptionPane.showMessageDialog(
                    null,
                    "Payroll report generated successfully.");

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error generating report:\n" + ex.getMessage());
        }
    }

    private void setColumnWidths() {

        TableColumnModel columns = payrollTable.getColumnModel();

        if (columns.getColumnCount() == 11) {

            columns.getColumn(0).setPreferredWidth(120); // Employee ID
                    columns.getColumn(1).setPreferredWidth(180); // Name
                    columns.getColumn(2).setPreferredWidth(120); // Hourly Rate
                    columns.getColumn(3).setPreferredWidth(130); // Hours Worked
                    columns.getColumn(4).setPreferredWidth(100); // OT Hours
                    columns.getColumn(5).setPreferredWidth(100); // OT Pay
                    columns.getColumn(6).setPreferredWidth(120); // Gross Pay
                    columns.getColumn(7).setPreferredWidth(110); // Allowance
                    columns.getColumn(8).setPreferredWidth(120); // Deductions
                    columns.getColumn(9).setPreferredWidth(120); // Net Salary
                    columns.getColumn(10).setPreferredWidth(100); // Status

                } else if (columns.getColumnCount() == 12) {

            columns.getColumn(0).setPreferredWidth(180); // Employee Name
                    columns.getColumn(1).setPreferredWidth(120); // Employee ID
                    columns.getColumn(2).setPreferredWidth(150); // Cutoff
                    columns.getColumn(3).setPreferredWidth(120); // Hourly Rate
                    columns.getColumn(4).setPreferredWidth(130); // Hours Worked
                    columns.getColumn(5).setPreferredWidth(100); // OT Hours
                    columns.getColumn(6).setPreferredWidth(100); // OT Pay
                    columns.getColumn(7).setPreferredWidth(120); // Gross Pay
                    columns.getColumn(8).setPreferredWidth(110); // Allowance
                    columns.getColumn(9).setPreferredWidth(120); // Deductions
                    columns.getColumn(10).setPreferredWidth(120); // Net Salary
                    columns.getColumn(11).setPreferredWidth(100); // Status
                }
    }

    private void centerTableText() {

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < payrollTable.getColumnCount(); i++) {
            payrollTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);
        }
    }

    private String[] getCutoffOptions() {

        LinkedHashSet<String> cutoffs = new LinkedHashSet<>();

        cutoffs.add("All");

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                    getClass().getResourceAsStream(
                    "/MotorPH_Employee Data - Attendance Record.csv"
                    )
                    )
                    );

            reader.readLine(); // skip header

                    String line;

            int rowCount = 0;

            while ((line = reader.readLine()) != null) {

                // Remove leading/trailing spaces
                line = line.trim();

                // Skip blank lines
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");

                String[] date = data[3].trim().split("/");

                int month = Integer.parseInt(date[0]);
                String year = date[2];

                String monthName = switch (month) {
                    case 1 -> "Jan";
                    case 2 -> "Feb";
                    case 3 -> "Mar";
                    case 4 -> "Apr";
                    case 5 -> "May";
                    case 6 -> "Jun";
                    case 7 -> "Jul";
                    case 8 -> "Aug";
                    case 9 -> "Sep";
                    case 10 -> "Oct";
                    case 11 -> "Nov";
                    case 12 -> "Dec";
                    default -> "";
                };

                cutoffs.add(monthName + " 1st-15th " + year);
                cutoffs.add(monthName + " 16th-30th " + year);
            }

            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<String> cutoffList = new ArrayList<>(cutoffs);

        // Keep "All" at the top
        cutoffList.remove("All");

        // Reverse the remaining cutoffs
        Collections.reverse(cutoffList);

        // Put "All" back at the beginning
        cutoffList.add(0, "All");

        return cutoffList.toArray(new String[0]);
    }

    private void downloadPayrollPDF() {

        if (payrollTable.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No payroll data available.",
                    "Download PDF",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Save Payroll Report");

        chooser.setFileFilter(
                new FileNameExtensionFilter(
                "PDF Files (*.pdf)",
                "pdf"));

        chooser.setSelectedFile(new File("Payroll_Report.pdf"));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".pdf")) {

                file = new File(file.getAbsolutePath() + ".pdf");
            }

            exportTableToPDF(file);
        }
    }

    private void exportTableToPDF(File file) {

        try {

            Document document = new Document(PageSize.A4.rotate());

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file));

            document.setMargins(15, 15, 20, 20);

            document.open();

            document.add(new Paragraph(
                    "MotorPH Payroll Report",
                    FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    18)));

            document.add(new Paragraph(
                    "Cutoff: "
                    + cutoffDropdown.getSelectedItem()));

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Number of Employees: " + employeeNumbers.getText()
                    + " | "
                    + "Total Gross Pay: " + totalGrossPayLabel.getText()
                    + " | "
                    + "Total Deductions: " + totalDeductionsLabel.getText()
                    + " | "
                    + "Average Net Pay: " + averageNetPayLabel.getText()
                    ));

            document.add(new Paragraph(" "));

            PdfPTable pdfTable =
                    new PdfPTable(
                    payrollTable.getColumnCount());

            pdfTable.setWidthPercentage(100);

            float[] widths =
                    new float[payrollTable.getColumnCount()];

            for (int i = 0; i < widths.length; i++) {

                widths[i] =
                        payrollTable
                        .getColumnModel()
                        .getColumn(i)
                        .getWidth();
            }

            pdfTable.setWidths(widths);

            // Headers

            for (int i = 0; i < payrollTable.getColumnCount(); i++) {

                PdfPCell cell =
                        new PdfPCell(
                        new com.itextpdf.text.Phrase(
                        payrollTable.getColumnName(i)));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setFixedHeight(40f);

                pdfTable.addCell(cell);
            }

            // Rows

            for (int row = 0;
                    row < payrollTable.getRowCount();
                    row++) {

                for (int col = 0;
                        col < payrollTable.getColumnCount();
                        col++) {

                    Object value =
                            payrollTable.getValueAt(row, col);

                    PdfPCell cell =
                            new PdfPCell(
                            new com.itextpdf.text.Phrase(
                            value == null
                            ? ""
                            : value.toString()));

                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(40f);

                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);

            document.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Payroll PDF exported successfully.");

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to export PDF.\n"
                    + ex.getMessage());
        }
    }

    // =========================================
    // FORMATTING METHODS
    // =========================================

    private String formatMoney(double value) {
        return String.format("₱%,.2f", value);
    }

    private String formatHours(double value) {
        return String.format("%.2f", value);
    }

    // =========================================
    // PAYROLL SUMMARY
    // =========================================

    private void updatePayrollSummary(
            int employeeCount,
            double totalGrossPay,
            double totalDeductions,
            double totalNetPay) {

        updateEmployeeCount(employeeCount);

        totalGrossPayLabel.setText(formatMoney(totalGrossPay));

        totalDeductionsLabel.setText(formatMoney(totalDeductions));

        if (employeeCount == 0) {
            averageNetPayLabel.setText("₱0.00");
        } else {
            averageNetPayLabel.setText(
                    formatMoney(totalNetPay / employeeCount));
        }

        updateProcessingTimestamp();
    }

    private void updateEmployeeCount(int employeeCount) {

        employeeNumbers.setText(String.valueOf(employeeCount));
    }
}
