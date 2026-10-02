package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.print.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyPayslipPanel extends JPanel {
    private JPanel payslipCard;
    private JComboBox<String> periodBox;
    private Employee employee;

    public MyPayslipPanel(Employee employee) {

        this.employee = employee;
        setBounds(0, 0, 780, 700);
        setBackground(new Color(227, 234, 231));
        setLayout(null);

        // =========================
        // BREADCRUMB
        // =========================
        JLabel breadcrumb = new JLabel("Home > My Payslip");
        breadcrumb.setBounds(40,45,250,20);
        breadcrumb.setForeground(new Color(120, 120, 120));
        breadcrumb.setFont(new Font("Segoe UI",Font.PLAIN,12));
        add(breadcrumb);

        // =========================
        // TITLE
        // =========================
        JLabel title = new JLabel("My Payslip");
        title.setBounds(40,75,300,45);
        title.setFont(new Font("Segoe UI",Font.BOLD,34));
        add(title);

        // =========================
        // PAY PERIOD LABEL
        // =========================
        JLabel payPeriodLabel = new JLabel("Choose Pay Period:");
        payPeriodLabel.setBounds(40,145,170,30);
        payPeriodLabel.setFont(new Font("Segoe UI",Font.PLAIN,16));
        add(payPeriodLabel);

        // =========================
        // PAY PERIOD COMBOBOX
        // =========================
        String[] periods =
                buildPayPeriodOptions();

        periodBox = new JComboBox<>(periods);

        if (periods.length == 1 && periods[0].equals("No attendance records")) {
            periodBox.setEnabled(false);
        }

        periodBox.setBounds(190,145,220,32);
        periodBox.setFont(new Font("Segoe UI",Font.BOLD,14));
        add(periodBox);

        selectCurrentPayPeriod();

        // =========================
        // FILE DISPUTE BUTTON
        // =========================
        JButton disputeButton = createRoundedButton("FILE DISPUTE",new Color(244, 205, 72),new Color(180, 150, 40),Color.BLACK);
        if (!periodBox.isEnabled()) {

            disputeButton.setEnabled(false);

        }
        disputeButton.setBounds(430,145,145,32);
        add(disputeButton);

        // =========================
        // DOWNLOAD BUTTON
        // =========================
        JButton downloadButton =createRoundedButton("DOWNLOAD PDF",new Color(79, 141, 168),
                new Color(60, 110, 130),Color.WHITE);

        if (!periodBox.isEnabled()) {
            downloadButton.setEnabled(false);
        }

        downloadButton.setBounds(590,145,150,32);
        add(downloadButton);

        // =========================
        // PAYSLIP CARD
        // =========================
        payslipCard = new JPanel();
        payslipCard.setLayout(null);
        payslipCard.setBackground(Color.WHITE);
        payslipCard.setBorder(
                new CompoundBorder(
                new LineBorder(
                new Color(210, 210, 210),
                1,
                true
                ),
                new EmptyBorder(
                20,
                20,
                20,
                20
                )
                )
                );

        JScrollPane scrollPane = new JScrollPane(payslipCard);
        scrollPane.setBounds(40,200,700,430);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        // =========================
        // INITIAL LOAD
        // =========================
        if (periodBox.isEnabled()) {

            loadPayslip(periodBox.getSelectedItem().toString());

        }

        // =========================
        // PERIOD CHANGE
        // =========================
        periodBox.addActionListener(e -> {

                    if (periodBox.isEnabled()) {

                        loadPayslip(periodBox.getSelectedItem().toString());

                    }

                });

        // =========================
        // DOWNLOAD BUTTON EVENT
        // =========================
        downloadButton.addActionListener(e -> {
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setJobName("MotorPH Payslip");
                    job.setPrintable((graphics, pageFormat, pageIndex) -> {

                        if (pageIndex > 0) {
                            return Printable.NO_SUCH_PAGE;
                        }

                        Graphics2D g2 = (Graphics2D) graphics;

                        Dimension payslipSize =
                        payslipCard.getPreferredSize();

                        double scaleX =
                        pageFormat.getImageableWidth()
                        / payslipCard.getWidth();

                        double scaleY =
                        pageFormat.getImageableHeight()
                        / payslipSize.getHeight();

                        double scale =
                        Math.min(
                        scaleX,
                        scaleY
                        );

                        double centeredX =
                        pageFormat.getImageableX()
                        + (
                        pageFormat.getImageableWidth()
                        - payslipCard.getWidth() * scale
                        ) / 2;

                        g2.translate(
                        centeredX,
                        pageFormat.getImageableY()
                        );

                        g2.scale(scale, scale);

                        payslipCard.setSize(
                        payslipCard.getWidth(),
                        payslipSize.height
                        );

                        payslipCard.paint(g2);

                        return Printable.PAGE_EXISTS;
                    });

                    boolean proceed = job.printDialog();

                    if (proceed) {
                        try {

                            job.print();
                            JOptionPane.showMessageDialog(
                            null,
                            "Payslip downloaded successfully."
                            );

                        } catch (Exception ex) {

                            JOptionPane.showMessageDialog(
                            null,
                            "Unable to download payslip."
                            );
                        }
                    }
                });

        // =========================
        // DISPUTE BUTTON EVENT
        // =========================

        disputeButton.addActionListener(e -> {

                    new PayrollDisputePanel(
                    employee,
                    periodBox.getSelectedItem().toString()
                    ).showDialog();

                });
    }

    // =========================
    // LOAD PAYSLIP
    // =========================

    private void loadPayslip(String selectedPeriod) {

        payslipCard.removeAll();

        PayslipComputation computation =
                computePayslipFromCSV(selectedPeriod);

        double totalHours = computation.totalHours;
        double grossSalary = computation.grossSalary;
        double overtimeHours = computation.overtimeHours;
        double hourlyRate = computation.hourlyRate;
        double sss = computation.sss;
        double philHealth = computation.philHealth;
        double pagibig = computation.pagibig;
        double taxableIncome = computation.taxableIncome;
        double tax = computation.tax;

        double totalDeductions =
                sss + philHealth + pagibig + tax;

        double allowance = computation.allowance;

        double netPay =
                SalaryComputationModule.computeNetPay(
                grossSalary,
                allowance,
                totalDeductions
                );

        JLabel companyLabel =
                new JLabel(
                "MotorPH Payroll Statement"
                );

        companyLabel.setBounds(
                25,
                20,
                500,
                35
                );

        companyLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                28
                )
                );

        payslipCard.add(companyLabel);

        JLabel employeeLabel =
                new JLabel(
                "Employee: "
                + employee.getFirstName()
                + " "
                + employee.getLastName()
                );

        employeeLabel.setBounds(
                25,
                80,
                500,
                25
                );

        employeeLabel.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                16
                )
                );

        payslipCard.add(employeeLabel);

        JLabel cutoffLabel =
                new JLabel(
                "Cutoff Date: "
                + selectedPeriod
                );

        cutoffLabel.setBounds(
                25,
                110,
                500,
                25
                );

        cutoffLabel.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                16
                )
                );

        payslipCard.add(cutoffLabel);

        int y = 180;

        payslipCard.add(
                createPayslipRow(
                "Total Hours Worked",
                String.format("%.2f hrs", totalHours),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Overtime Hours",
                String.format("%.2f hrs", overtimeHours),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Hourly Rate",
                "₱ " + String.format("%,.2f", hourlyRate),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Gross Pay",
                "₱ " + String.format("%,.2f", grossSalary),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Total Allowance",
                "₱ " + String.format("%,.2f", allowance),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "SSS Deduction",
                "₱ " + String.format("%,.2f", sss),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "PhilHealth Deduction",
                "₱ " + String.format("%,.2f", philHealth),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Pag-IBIG Deduction",
                "₱ " + String.format("%,.2f", pagibig),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Taxable Income",
                "₱ " + String.format("%,.2f", taxableIncome),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Withholding Tax",
                "₱ " + String.format("%,.2f", tax),
                y
                )
                );

        y += 50;

        payslipCard.add(
                createPayslipRow(
                "Total Deductions",
                "₱ " + String.format("%,.2f", totalDeductions),
                y
                )
                );

        y += 70;

        payslipCard.add(
                createPayslipRow(
                "NET PAY",
                "₱ " + String.format("%,.2f", netPay),
                y
                )
                );

        payslipCard.setPreferredSize(
                new Dimension(
                660,
                y + 120
                )
                );

        payslipCard.revalidate();

        payslipCard.repaint();
    }

    private PayslipComputation computePayslipFromCSV(
            String selectedPeriod
            ) {

        double totalHours =
                computeAttendanceHours(selectedPeriod);

        double overtimeHours =
                computeApprovedOvertimeHours(selectedPeriod);

        double regularHours =
                Math.max(0, totalHours - overtimeHours);

        double grossSalary =
                SalaryComputationModule.computeGrossPay(
                employee.getHourlyRate(),
                regularHours,
                overtimeHours
                );

        double allowance = 0;

        if (totalHours > 0 || overtimeHours > 0) {

            allowance =
                    (employee.getRiceSubsidy()
                    + employee.getPhoneAllowance()
                    + employee.getClothingAllowance()) / 2;
        }

        Deduction deduction = new Deduction();

        double sss = 0;
        double philHealth = 0;
        double pagibig = 0;
        double taxableIncome = 0;
        double tax = 0;

        if (grossSalary > 0) {

            double grossCompensation = grossSalary + allowance;

            sss = deduction.calculateSSS(employee.getBasicSalary());
            philHealth = deduction.calculatePhilHealth(employee.getBasicSalary());
            pagibig = deduction.calculatePagIbig(employee.getBasicSalary());

            taxableIncome =
                    grossCompensation
                    - sss
                    - philHealth
                    - pagibig;

            tax = deduction.calculateTax(taxableIncome);
        }

        return new PayslipComputation(
                totalHours,
                overtimeHours,
                employee.getHourlyRate(),
                grossSalary,
                allowance,
                sss,
                philHealth,
                pagibig,
                taxableIncome,
                tax
                );
    }

    private double computeApprovedOvertimeHours(
            String selectedPeriod
            ) {

        LocalDate[] range =
                parsePeriodRange(selectedPeriod);

        double overtimeHours = 0;

        try (BufferedReader reader = openAttendanceReader()) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (data.length < 6) {
                    continue;
                }

                if (!data[0].trim().equals(employee.getEmployeeNumber())) {
                    continue;
                }

                LocalDate attendanceDate =
                        LocalDate.parse(
                        data[3].trim(),
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"));

                if (attendanceDate.isBefore(range[0])
                        || attendanceDate.isAfter(range[1])) {
                    continue;
                }

                LocalTime logIn =
                        parseAttendanceTime(data[4].trim());

                LocalTime logOut =
                        parseAttendanceTime(data[5].trim());

                if (logIn == null || logOut == null) {
                    continue;
                }

                long minutes =
                        Duration.between(logIn, logOut).toMinutes();

                if (data.length >= 7 && !data[6].trim().isEmpty()) {

                    try {

                        minutes -= Long.parseLong(data[6].trim());

                    } catch (Exception e) {
                    }
                }

                if (minutes <= 0) {
                    continue;
                }

                double workedHours =
                        minutes / 60.0;

                double actualOvertimeHours =
                        Math.max(0, workedHours - 8.0);

                overtimeHours +=
                        OvertimeRequestService.getPayableOvertimeHours(
                        employee.getEmployeeNumber(),
                        attendanceDate,
                        actualOvertimeHours
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return overtimeHours;
    }

    private String[] buildPayPeriodOptions() {

        TreeMap<LocalDate, String> periods = new TreeMap<>(Collections.reverseOrder());

        try (BufferedReader reader = openAttendanceReader()) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (data.length < 4) {
                    continue;
                }

                if (!data[0].trim().equals(employee.getEmployeeNumber())) {
                    continue;
                }

                LocalDate attendanceDate =
                        LocalDate.parse(
                        data[3].trim(),
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"));

                String month =
                        attendanceDate.format(
                        DateTimeFormatter.ofPattern("MMMM"));

                int year = attendanceDate.getYear();

                int lastDay =
                        YearMonth.of(
                        year,
                        attendanceDate.getMonthValue())
                        .lengthOfMonth();

                if (attendanceDate.getDayOfMonth() <= 15) {

                    LocalDate cutoffStart =
                            LocalDate.of(year, attendanceDate.getMonthValue(), 1);

                    periods.put(
                            cutoffStart,
                            month + " 1 - 15, " + year
                            );

                } else {

                    LocalDate cutoffStart =
                            LocalDate.of(year, attendanceDate.getMonthValue(), 16);

                    periods.put(
                            cutoffStart,
                            month + " 16 - " + lastDay + ", " + year
                            );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        if (periods.isEmpty()) {

            return new String[]{
                "No attendance records"
                    };
        }

        return periods.values().toArray(new String[0]);
    }

    private void selectCurrentPayPeriod() {

        LocalDate today =
                LocalDate.now();

        String monthName =
                today.format(
                DateTimeFormatter.ofPattern("MMMM")
                );

        int lastDay =
                YearMonth.of(
                today.getYear(),
                today.getMonthValue()
                ).lengthOfMonth();

        String currentPeriod =
                today.getDayOfMonth() <= 15
                ? monthName + " 1 - 15, " + today.getYear()
                : monthName + " 16 - " + lastDay + ", " + today.getYear();

        periodBox.setSelectedItem(
                currentPeriod
                );
    }

    private double computeAttendanceHours(
            String selectedPeriod
            ) {

        LocalDate[] range =
                parsePeriodRange(selectedPeriod);

        double totalHours = 0;

        try(BufferedReader reader = openAttendanceReader()){

            String line;

            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length < 6){
                    continue;
                }

                if(!data[0].trim().equals(
                        employee.getEmployeeNumber()
                        )){
                    continue;
                }

                LocalDate attendanceDate =
                        LocalDate.parse(
                        data[3].trim(),
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        );

                if(attendanceDate.isBefore(range[0])
                        || attendanceDate.isAfter(range[1])){
                    continue;
                }

                LocalTime logIn =
                        parseAttendanceTime(data[4].trim());

                LocalTime logOut =
                        parseAttendanceTime(data[5].trim());

                if(logIn == null || logOut == null){
                    continue;
                }

                long minutes =
                        Duration.between(
                        logIn,
                        logOut
                        ).toMinutes();

                if(data.length >= 7
                        && !data[6].trim().isEmpty()){

                    try {

                        minutes -=
                                Long.parseLong(
                                data[6].trim()
                                );

                    } catch(NumberFormatException e){

                        // Keep raw minutes when break data is invalid.
                    }
                }

                if(minutes > 0){

                    double workedHours =
                            minutes / 60.0;

                    double regularHours =
                            Math.min(
                            workedHours,
                            8.0
                            );

                    double actualOvertimeHours =
                            Math.max(
                            0,
                            workedHours - 8.0
                            );

                    double approvedOvertimeHours =
                            OvertimeRequestService.getPayableOvertimeHours(
                            employee.getEmployeeNumber(),
                            attendanceDate,
                            actualOvertimeHours
                            );

                    totalHours +=
                            regularHours
                            + approvedOvertimeHours;
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return totalHours;
    }

    private BufferedReader openAttendanceReader()
            throws Exception {

        File file =
                new File(
                "src/main/resources/MotorPH_Employee Data - Attendance Record.csv"
                );

        if(!file.exists()){

            file =
                    new File(
                    "Mo-IT103-Group4/src/main/resources/MotorPH_Employee Data - Attendance Record.csv"
                    );
        }

        return new BufferedReader(
                new FileReader(file)
                );
    }

    private LocalTime parseAttendanceTime(String time) {

        try {

            return LocalTime.parse(
                    time,
                    DateTimeFormatter.ofPattern("H:mm")
                    );

        } catch(Exception e){

            return null;
        }
    }

    private LocalDate[] parsePeriodRange(
            String selectedPeriod
            ) {

        String[] parts =
                selectedPeriod.split(" - ");

        LocalDate start =
                parsePeriodDate(
                parts[0],
                selectedPeriod
                );

        LocalDate end =
                parsePeriodDate(
                parts[1],
                selectedPeriod
                );

        return new LocalDate[]{
            start,
                    end
                };
    }

    private LocalDate parsePeriodDate(
            String dateText,
            String fullPeriod
            ) {

        String year =
                fullPeriod.substring(
                fullPeriod.lastIndexOf(" ") + 1
                );

        String normalized =
                dateText.trim();

        String month =
                fullPeriod.substring(
                0,
                fullPeriod.indexOf(" ")
                );

        if(Character.isDigit(normalized.charAt(0))){

            normalized =
                    month + " " + normalized;
        }

        if(!normalized.contains(",")){

            normalized =
                    normalized + ", " + year;
        }

        return LocalDate.parse(
                normalized,
                DateTimeFormatter.ofPattern("MMMM d, yyyy")
                );
    }

    private boolean isSecondCutoff(
            LocalDate cutoffStart
            ) {

        return cutoffStart.getDayOfMonth() >= 16;
    }

    private boolean isGovernmentDeductionEligible(
            LocalDate cutoffStart
            ) {

        if(isProtectedExistingEmployee()){
            return true;
        }

        String dateHiredText =
                employee.getDateHired();

        if(dateHiredText == null
                || dateHiredText.trim().isEmpty()){

            return true;
        }

        LocalDate dateHired =
                parseDateHired(dateHiredText.trim());

        if(dateHired == null){
            return true;
        }

        YearMonth hiredMonth =
                YearMonth.from(dateHired);

        YearMonth cutoffMonth =
                YearMonth.from(cutoffStart);

        return cutoffMonth.isAfter(hiredMonth);
    }

    private boolean isProtectedExistingEmployee() {

        try {

            int employeeId =
                    Integer.parseInt(
                    employee.getEmployeeNumber().trim()
                    );

            return employeeId >= 10001
                    && employeeId <= 10034;

        } catch(Exception e){

            return false;
        }
    }

    private LocalDate parseDateHired(
            String dateHiredText
            ) {

        String[] patterns = {
            "yyyy-MM-dd",
                    "MM/dd/yyyy",
                    "M/d/yyyy",
                    "MMMM d, yyyy"
                };

        for(String pattern : patterns){

            try {

                return LocalDate.parse(
                        dateHiredText,
                        DateTimeFormatter.ofPattern(pattern)
                        );

            } catch(Exception e){

                // Try the next supported date format.
            }
        }

        return null;
    }

    private static class PayslipComputation {

        double totalHours;
        double grossSalary;
        double sss;
        double philHealth;
        double pagibig;
        double taxableIncome;
        double tax;
        double allowance;
        double overtimeHours;
        double hourlyRate;

        PayslipComputation(
                double totalHours,
                double overtimeHours,
                double hourlyRate,
                double grossSalary,
                double allowance,
                double sss,
                double philHealth,
                double pagibig,
                double taxableIncome,
                double tax
                ) {
            this.allowance = allowance;
            this.totalHours = totalHours;
            this.overtimeHours = overtimeHours;
            this.hourlyRate = hourlyRate;
            this.grossSalary = grossSalary;
            this.sss = sss;
            this.philHealth = philHealth;
            this.pagibig = pagibig;
            this.taxableIncome = taxableIncome;
            this.tax = tax;
        }
    }

    // =========================
    // PAYSLIP ROW
    // =========================

    private JPanel createPayslipRow(
            String label,
            String value,
            int y
            ) {

        JPanel row =
                new JPanel();

        row.setLayout(null);

        row.setBounds(
                20,
                y,
                620,
                40
                );

        row.setOpaque(false);

        JLabel labelText =
                new JLabel(label);

        labelText.setBounds(
                10,
                5,
                300,
                30
                );

        labelText.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                17
                )
                );

        row.add(labelText);

        JLabel valueText =
                new JLabel(
                value,
                SwingConstants.RIGHT
                );

        valueText.setBounds(
                300,
                5,
                310,
                30
                );

        valueText.setFont(
                new Font(
                "Segoe UI",
                label.equals("NET PAY")
                ? Font.BOLD
                : Font.PLAIN,
                label.equals("NET PAY")
                ? 22
                : 17
                )
                );

        row.add(valueText);

        return row;
    }

    // =========================
    // BUTTON DESIGN
    // =========================

    private JButton createRoundedButton(
            String text,
            Color bgColor,
            Color borderColor,
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

                g2.setColor(borderColor);

                g2.setStroke(
                        new BasicStroke(2)
                        );

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
}
