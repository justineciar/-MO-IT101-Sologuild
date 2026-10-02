package com.mycompany.motorphemployeeapp;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;

public class EmployeeDataManager {

    private ArrayList<Employee> employeeData;
    private ArrayList<AttendanceRecord> attendanceData;

    // =========================
    // CONSTRUCTOR
    // =========================

    public EmployeeDataManager() {

        employeeData = new ArrayList<>();

        attendanceData = new ArrayList<>();
    }

    // =========================
    // LOAD EMPLOYEE CSV
    // =========================

    public void loadEmployeeData(String csvFile) {

        try (
                BufferedReader br =
                new BufferedReader(
                new FileReader(csvFile)
                )
                ) {

            String line;

            // Skip CSV Header
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
                        );

                // Prevent Array Index Errors

                if (data.length < 19) {

                    continue;
                }

                // Employee information

                String employeeNumber =
                        data[0].trim();

                String lastName =
                        data[1].trim();

                String firstName =
                        data[2].trim();

                String birthday =
                        data[3].trim();

                // Full address from CSV

                String address =
                        data[4]
                        .replace("\"", "")
                        .trim();

                // Position

                String position =
                        data[11].trim();

                // Supervisor from CSV

                String supervisor =
                        data[12]
                        .replace("\"", "")
                        .trim();

                String dateHired =
                        data.length >= 20
                        ? data[19].trim()
                        : "";

                // Hourly rate

                double hourlyRate = 0;

                try {

                    hourlyRate =
                            Double.parseDouble(
                            data[18]
                            .replace("\"", "")
                            .trim()
                            );

                } catch (NumberFormatException e) {

                    hourlyRate = 0;
                }

                // Default password

                String password = "12345";

                // Create Employee Object

                double basicSalary = 0;
                double riceSubsidy = 0;
                double phoneAllowance = 0;
                double clothingAllowance = 0;
                double grossSemiMonthlyRate = 0;

                try {

                    basicSalary =
                            parseMoneyValue(data[13]);

                    riceSubsidy =
                            parseMoneyValue(data[14]);

                    phoneAllowance =
                            parseMoneyValue(data[15]);

                    clothingAllowance =
                            parseMoneyValue(data[16]);

                    grossSemiMonthlyRate =
                            parseMoneyValue(data[17]);

                } catch(Exception e){

                    basicSalary = 0;
                    riceSubsidy = 0;
                    phoneAllowance = 0;
                    clothingAllowance = 0;
                    grossSemiMonthlyRate = 0;
                }

                Employee employee =
                        new Employee(
                        employeeNumber,
                        firstName,
                        lastName,
                        birthday,
                        hourlyRate,
                        basicSalary,
                        riceSubsidy,
                        phoneAllowance,
                        clothingAllowance,
                        grossSemiMonthlyRate,
                        position,
                        password,
                        address,
                        supervisor,
                        dateHired
                        );

                // Add employee to list

                employeeData.add(employee);

                // Console checker

                System.out.println(
                        employeeNumber
                        + " -> "
                        + position
                        );
            }

            System.out.println(
                    "Employee data loaded successfully."
                    );

        } catch (Exception e) {

            System.out.println(
                    "Error loading CSV: "
                    + e.getMessage()
                    );
        }
    }

    // =========================
    // LOAD ATTENDANCE
    // =========================

    public void loadAttendanceData(String csvFile) {

        attendanceData.clear();

        try (
                BufferedReader br =
                new BufferedReader(
                new FileReader(csvFile)
                )
                ) {

            String line;

            // Skip header

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length < 6) {

                    continue;
                }

                try {

                    String employeeNumber = data[0].trim();

                    String attendanceDate = data[3].trim();

                    String loginRaw = data[4].trim();

                    String logoutRaw = data[5].trim();

                    int breakMinutes = 0;

                    String status = "Present";

                    if(data.length >= 7 && !data[6].trim().isEmpty()){

                        try {

                            breakMinutes =
                                    Integer.parseInt(
                                    data[6].trim()
                                    );

                        } catch(NumberFormatException e){

                            breakMinutes = 0;
                        }
                    }

                    if (data.length >= 8 && !data[7].trim().isEmpty()) {
                        status = data[7].trim();
                    }

                    // Add leading zero

                    if (loginRaw.length() == 4) {

                        loginRaw = "0" + loginRaw;
                    }

                    if (logoutRaw.length() == 4) {

                        logoutRaw = "0" + logoutRaw;
                    }

                    LocalTime loginTime =
                            LocalTime.parse(loginRaw);

                    LocalTime logoutTime =
                            LocalTime.parse(logoutRaw);

                    AttendanceRecord record =
                            new AttendanceRecord(
                            employeeNumber,
                            attendanceDate,
                            loginTime,
                            logoutTime,
                            breakMinutes
                            );
                    record.setStatus(status);
                    attendanceData.add(record);

                } catch (Exception ex) {

                    System.out.println(
                            "Skipped invalid attendance row."
                            );
                }
            }

            System.out.println(
                    "Attendance data loaded successfully."
                    );

        } catch (Exception e) {

            System.out.println(
                    "Error loading attendance CSV: "
                    + e.getMessage()
                    );
        }
    }

    // =========================
    // FIND EMPLOYEE
    // =========================

    public Employee findEmployeeByNumber(
            String employeeNumber
            ) {

        for (Employee employee : employeeData) {

            if (
                    employee.getEmployeeNumber()
                    .equals(employeeNumber)
                    ) {

                return employee;
            }
        }

        return null;
    }

    // =========================
    // GET EMPLOYEES
    // =========================

    public ArrayList<Employee> getEmployees() {

        return employeeData;
    }

    // =========================
    // GET ATTENDANCE DATA
    // =========================

    public ArrayList<AttendanceRecord> getAttendanceData() {

        return attendanceData;
    }

    // =========================
    // GET HIGHEST EMPLOYEE ID
    // =========================

    public String getNextEmployeeId() {

        int highestId =
                0;

        try (
                BufferedReader reader =
                new BufferedReader(
                new FileReader(
                "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                )
                )
                ) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
                        );

                if (data.length > 0) {

                    try {

                        int currentId =
                                Integer.parseInt(
                                data[0].trim()
                                );

                        if (currentId > highestId) {

                            highestId =
                                    currentId;
                        }

                    } catch (NumberFormatException e) {

                        // Skip invalid employee IDs
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return String.valueOf(highestId + 1);
    }

    // =========================
    // ADD EMPLOYEE TO CSV
    // =========================

    public boolean addEmployeeToCSV(
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
            String status,
            String position,
            String supervisor,
            String salaryText,
            String riceSubsidyText,
            String phoneAllowanceText,
            String clothingAllowanceText
            ) {

        try {

            double salary =
                    Double.parseDouble(salaryText);

            double riceSubsidy =
                    Double.parseDouble(riceSubsidyText);

            double phoneAllowance =
                    Double.parseDouble(phoneAllowanceText);

            double clothingAllowance =
                    Double.parseDouble(clothingAllowanceText);

            double grossSemiMonthly =
                    salary / 2;

            double hourlyRate =
                    Math.round(
                    ((salary * 12 / 313.0) / 8)
                    * 100.0
                    ) / 100.0;

            ensureEmployeeHeaderHasDateHired();

            try(FileWriter writer =
                    new FileWriter(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv",
                    true
                    )) {

                String[] employeeRow = {

                    employeeId,
                            lastName,
                            firstName,
                            birthday,
                            address,
                            phone,
                            sss,
                            philhealth,
                            tin,
                            pagibig,
                            status,
                            position,
                            supervisor,
                            String.valueOf(salary),
                            String.valueOf(riceSubsidy),
                            String.valueOf(phoneAllowance),
                            String.valueOf(clothingAllowance),
                            String.valueOf(grossSemiMonthly),
                            String.valueOf(hourlyRate),
                            LocalDate.now().toString()
                        };

                writer.append(System.lineSeparator());
                writer.append(formatCSVLine(employeeRow));

                return true;
            }

        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================
    // ADD BULK EMPLOYEES
    // =========================

    public String importEmployeesFromCSV(File importFile)
            throws IOException {

        // IMPORT FILE VALIDATION
        if(importFile == null){

            throw new IOException(
                    "No file was selected."
                    );
        }

        if(!importFile.getName()
                .toLowerCase()
                .endsWith(".csv")){

            throw new IOException(
                    "Invalid file type.\nPlease select a CSV file."
                    );
        }

        if(!importFile.exists()){

            throw new IOException(
                    "Selected file does not exist."
                    );
        }

        if(importFile.length() == 0){

            throw new IOException(
                    "Selected file is empty."
                    );
        }

        String employeeFile =
                "src/main/resources/MotorPH_Employee Data - Employee Details.csv";

        ensureEmployeeHeaderHasDateHired();

        try (

                BufferedReader reader =
                new BufferedReader(
                new FileReader(importFile)
                );

                FileWriter writer =
                new FileWriter(employeeFile, true);

                ) {

            String line;

            boolean firstLine = true;

            int nextId =
                    Integer.parseInt(
                    getNextEmployeeId()
                    );

            List<String> duplicateCheck =
                    new ArrayList<>();

            List<String> addedEmployees =
                    new ArrayList<>();

            List<String> skippedEmployees =
                    new ArrayList<>();

            while ((line = reader.readLine()) != null) {

                if (firstLine) {

                    firstLine = false;
                    continue;
                }

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if (data.length < 19) {

                    continue;
                }

                String lastName =
                        data[1].trim();

                String firstName =
                        data[2].trim();

                String birthday =
                        data[3].trim();

                String employeeKey =
                        firstName.trim().toLowerCase()
                        + "|"
                        + lastName.trim().toLowerCase()
                        + "|"
                        + birthday.trim();

                if(employeeAlreadyExists(
                        firstName,
                        lastName,
                        birthday
                        ) || duplicateCheck.contains(employeeKey)){

                    skippedEmployees.add(
                            firstName + " " + lastName
                            );

                    continue;
                }

                duplicateCheck.add(employeeKey);

                if(data[10].trim().isEmpty()){

                    data[10] = "Active";
                }

                data[0] =
                        String.valueOf(nextId);

                if(data.length < 20){

                    String[] rowWithDateHired =
                            new String[20];

                    System.arraycopy(
                            data,
                            0,
                            rowWithDateHired,
                            0,
                            data.length
                            );

                    rowWithDateHired[19] =
                            LocalDate.now().toString();

                    data =
                            rowWithDateHired;

                } else if(data[19].trim().isEmpty()){

                    data[19] =
                            LocalDate.now().toString();
                }

                writer.write(
                        System.lineSeparator()
                        + formatCSVLine(data)
                        );

                addedEmployees.add(
                        firstName + " " + lastName
                        );

                nextId++;
            }

            StringBuilder result =
                    new StringBuilder();

            if(!addedEmployees.isEmpty()){

                result.append(
                        "Added Employees:\n\n"
                        );

                for(String employee : addedEmployees){

                    result.append("• ")
                            .append(employee)
                            .append("\n");
                }
            }

            if(!addedEmployees.isEmpty()
                    && !skippedEmployees.isEmpty()){

                result.append("\n");
            }

            if(!skippedEmployees.isEmpty()){

                result.append(
                        "Skipped Employees (Already Exists):\n\n"
                        );

                for(String employee : skippedEmployees){

                    result.append("• ")
                            .append(employee)
                            .append("\n");
                }
            }

            return result.toString();

        }
    }

    // =========================
    // CSV WRITER HELPER
    // =========================

    private String formatCSVLine(String[] data) {

        StringBuilder line =
                new StringBuilder();

        for (int i = 0; i < data.length; i++) {

            String value =
                    data[i];

            if (value == null) {

                value = "";
            }

            value =
                    value.trim();

            if (value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")) {

                value =
                        value.replace("\"", "\"\"");

                value =
                        "\"" + value + "\"";
            }

            line.append(value);

            if (i < data.length - 1) {

                line.append(",");
            }
        }

        return line.toString();
    }

    private void ensureEmployeeHeaderHasDateHired()
            throws IOException {

        File employeeFile =
                new File(
                "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                );

        if(!employeeFile.exists()){
            return;
        }

        List<String> lines =
                Files.readAllLines(
                employeeFile.toPath()
                );

        if(lines.isEmpty()){
            return;
        }

        String header =
                lines.get(0);

        if(header.toLowerCase().contains("date hired")){
            return;
        }

        lines.set(
                0,
                header + ",Date Hired"
                );

        Files.write(
                employeeFile.toPath(),
                lines
                );
    }

    // =========================
    // CHECK IF EMPLOYEE EXISTS
    // =========================

    public boolean employeeExists(String employeeId) {

        try {

            BufferedReader reader =
                    new BufferedReader(
                    new FileReader(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    )
                    );

            String line;

            reader.readLine(); // skip header

                    while((line = reader.readLine()) != null){

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
                        );

                if(data[0].trim().equals(employeeId)){

                    reader.close();

                    return true;
                }
            }

            reader.close();

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    private String normalizeBirthday(String birthday) {

        String[] parts = birthday.trim().split("/");

        if(parts.length != 3){
            return birthday.trim();
        }

        String month =
                String.format(
                "%02d",
                Integer.parseInt(parts[0])
                );

        String day =
                String.format(
                "%02d",
                Integer.parseInt(parts[1])
                );

        String year =
                parts[2];

        return month + "/" + day + "/" + year;
    }

    // =========================
    // CHECK DUPLICATE EMPLOYEE
    // =========================

    public boolean employeeAlreadyExists(
            String firstName,
            String lastName,
            String birthday
            ) {

        String[] files = {

            "src/main/resources/MotorPH_Employee Data - Employee Details.csv",

                    "src/main/resources/InactiveEmployees.csv"
                };

        firstName = firstName.trim().toLowerCase();
        lastName = lastName.trim().toLowerCase();
        birthday = normalizeBirthday(birthday);

        try {

            for(String employeeFile : files){

                File file = new File(employeeFile);

                if(!file.exists()){
                    continue;
                }

                BufferedReader reader =
                        new BufferedReader(
                        new FileReader(file)
                        );

                String line;

                reader.readLine();

                while((line = reader.readLine()) != null){

                    String[] data =
                            line.split(
                            ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                            -1
                            );

                    if(data.length < 4){
                        continue;
                    }

                    String existingLastName =
                            data[1]
                            .replace("\"","")
                            .trim()
                            .toLowerCase();

                    String existingFirstName =
                            data[2]
                            .replace("\"","")
                            .trim()
                            .toLowerCase();

                    String existingBirthday =
                            normalizeBirthday(
                            data[3]
                            .replace("\"","")
                            .trim()
                            );

                    if(existingFirstName.equals(firstName)
                            && existingLastName.equals(lastName)
                            && existingBirthday.equals(birthday)){

                        reader.close();

                        return true;
                    }
                }

                reader.close();
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    public boolean isValidSupervisor(String supervisorName) {

        if(supervisorName == null
                || supervisorName.trim().isEmpty()) {

            return false;
        }

        try(BufferedReader reader =
                new BufferedReader(
                new FileReader(
                "src/main/resources/MotorPH_Employee Data - Employee Details.csv"))) {

            reader.readLine(); // Skip header

                    String line;

            while((line = reader.readLine()) != null) {

                String[] data = line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length < 13) {
                    continue;
                }

                String employeeName =
                        data[2].replace("\"","").trim()
                        + " "
                        + data[1].replace("\"","").trim();

                String position =
                        data[11].replace("\"","").trim().toLowerCase();

                if(employeeName.equalsIgnoreCase(supervisorName.trim())) {

                    return position.contains("chief")
                            || position.contains("manager")
                            || position.contains("head")
                            || position.contains("leader")
                            || position.contains("lead");
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // UPDATE EMPLOYEE
    // =========================

    public boolean updateEmployee(
            String employeeId,
            String address,
            String phone,
            String status,
            String position,
            String supervisor,
            String salary,
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance
            ) {

        try {

            File employeeFile =
                    new File(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    );

            List<String> lines =
                    Files.readAllLines(
                    employeeFile.toPath()
                    );

            boolean found = false;

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        lines.get(i).split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length < 19){
                    continue;
                }

                if(data[0].trim().equals(employeeId.trim())){

                    data[4] = address;
                    data[5] = phone;

                    data[10] = status;
                    data[11] = position;
                    data[12] = supervisor;

                    data[13] = salary;
                    data[14] = riceSubsidy;
                    data[15] = phoneAllowance;
                    data[16] = clothingAllowance;

                    double salaryValue =
                            Double.parseDouble(salary);

                    double grossSemiMonthly =
                            salaryValue / 2;

                    double hourlyRate =
                            Math.round(
                            ((salaryValue * 12 / 313.0) / 8)
                            * 100.0
                            ) / 100.0;

                    data[17] =
                            String.valueOf(
                            grossSemiMonthly
                            );

                    data[18] =
                            String.valueOf(
                            hourlyRate
                            );

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }

            Files.write(
                    employeeFile.toPath(),
                    lines
                    );

            return true;

        } catch(Exception e){

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    e.toString(),
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE
                    );

            return false;
        }
    }

    public boolean createSalaryChangeRequest(
            String employeeId,
            String employeeName,
            String oldSalary,
            String newSalary,
            String oldRiceSubsidy,
            String newRiceSubsidy,
            String oldPhoneAllowance,
            String newPhoneAllowance,
            String oldClothingAllowance,
            String newClothingAllowance,
            String requestedBy
            ) {

        try {

            File requestFile =
                    new File(
                    "src/main/resources/SalaryChangeRequests.csv"
                    );

            boolean fileExists =
                    requestFile.exists();

            try(FileWriter writer =
                    new FileWriter(
                    requestFile,
                    true
                    )){

                if(!fileExists){

                    writer.write(
                            "Request ID,Employee ID,Employee Name,Old Basic Salary,New Basic Salary,Old Rice Subsidy,New Rice Subsidy,Old Phone Allowance,New Phone Allowance,Old Clothing Allowance,New Clothing Allowance,Requested By,Requested Date,Status,Reviewed By,Review Date"
                            );
                }

                String requestId =
                        String.valueOf(
                        System.currentTimeMillis()
                        );

                String[] row = {
                    requestId,
                            employeeId,
                            employeeName,
                            oldSalary,
                            newSalary,
                            oldRiceSubsidy,
                            newRiceSubsidy,
                            oldPhoneAllowance,
                            newPhoneAllowance,
                            oldClothingAllowance,
                            newClothingAllowance,
                            requestedBy,
                            java.time.LocalDateTime.now().toString(),
                            "Pending",
                            "",
                            ""
                        };

                writer.write(
                        System.lineSeparator()
                        + formatCSVLine(row)
                        );
            }

            return true;

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    public List<String[]> getPendingSalaryChangeRequests() {

        List<String[]> requests =
                new ArrayList<>();

        File requestFile =
                new File(
                "src/main/resources/SalaryChangeRequests.csv"
                );

        if(!requestFile.exists()){
            return requests;
        }

        try(BufferedReader reader =
                new BufferedReader(
                new FileReader(requestFile)
                )){

            String line;

            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length >= 16
                        && data[13].trim().equalsIgnoreCase("Pending")){

                    requests.add(data);
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return requests;
    }

    public boolean approveSalaryChangeRequest(
            String requestId,
            String reviewedBy
            ) {

        return reviewSalaryChangeRequest(
                requestId,
                reviewedBy,
                true
                );
    }

    public boolean rejectSalaryChangeRequest(
            String requestId,
            String reviewedBy
            ) {

        return reviewSalaryChangeRequest(
                requestId,
                reviewedBy,
                false
                );
    }

    private boolean reviewSalaryChangeRequest(
            String requestId,
            String reviewedBy,
            boolean approved
            ) {

        try {

            File requestFile =
                    new File(
                    "src/main/resources/SalaryChangeRequests.csv"
                    );

            if(!requestFile.exists()){
                return false;
            }

            List<String> lines =
                    Files.readAllLines(
                    requestFile.toPath()
                    );

            boolean found =
                    false;

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        lines.get(i).split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length < 16){
                    continue;
                }

                if(data[0].trim().equals(requestId.trim())
                        && data[13].trim().equalsIgnoreCase("Pending")){

                    if(approved){

                        boolean salaryUpdated =
                                updateSalaryAndAllowances(
                                data[1],
                                data[4],
                                data[6],
                                data[8],
                                data[10]
                                );

                        if(!salaryUpdated){
                            return false;
                        }

                        data[13] = "Approved";

                    }else{

                        data[13] = "Rejected";
                    }

                    data[14] = reviewedBy;
                    data[15] = java.time.LocalDateTime.now().toString();

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    found = true;
                    break;
                }
            }

            if(!found){
                return false;
            }

            Files.write(
                    requestFile.toPath(),
                    lines
                    );

            return true;

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    private boolean updateSalaryAndAllowances(
            String employeeId,
            String salary,
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance
            ) {

        try {

            File employeeFile =
                    new File(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    );

            List<String> lines =
                    Files.readAllLines(
                    employeeFile.toPath()
                    );

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        lines.get(i).split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length < 19){
                    continue;
                }

                if(data[0].trim().equals(employeeId.trim())){

                    data[13] = salary;
                    data[14] = riceSubsidy;
                    data[15] = phoneAllowance;
                    data[16] = clothingAllowance;

                    double salaryValue =
                            Double.parseDouble(salary);

                    data[17] =
                            String.valueOf(
                            salaryValue / 2
                            );

                    data[18] =
                            String.valueOf(
                            Math.round(
                            ((salaryValue * 12 / 313.0) / 8)
                            * 100.0
                            ) / 100.0
                            );

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    Files.write(
                            employeeFile.toPath(),
                            lines
                            );

                    return true;
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    public boolean createPayrollDisputeRequest(
            String employeeId,
            String employeeName,
            String cutoffPeriod,
            String recordDate,
            String timeIn,
            String timeOut,
            String overtimeHours,
            String category,
            String evidence,
            String remarks
            ) {

        try {

            File requestFile =
                    new File(
                    "src/main/resources/PayrollDisputeRequests.csv"
                    );

            boolean fileExists =
                    requestFile.exists();

            try(FileWriter writer =
                    new FileWriter(
                    requestFile,
                    true
                    )){

                if(!fileExists){

                    writer.write(
                            "Dispute ID,Employee ID,Employee Name,Cutoff Period,Date of Record,Time In,Time Out,Overtime Hours,Category,Evidence,Remarks,Submitted Date,Status,Reviewed By,Review Date"
                            );
                }

                String disputeId =
                        String.valueOf(
                        System.currentTimeMillis()
                        );

                String[] row = {
                    disputeId,
                            employeeId,
                            employeeName,
                            cutoffPeriod,
                            recordDate,
                            timeIn,
                            timeOut,
                            overtimeHours,
                            category,
                            evidence,
                            remarks,
                            java.time.LocalDateTime.now().toString(),
                            "Pending",
                            "",
                            ""
                        };

                writer.write(
                        System.lineSeparator()
                        + formatCSVLine(row)
                        );
            }

            return true;

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    public List<String[]> getPendingPayrollDisputeRequests() {

        List<String[]> requests =
                new ArrayList<>();

        File requestFile =
                new File(
                "src/main/resources/PayrollDisputeRequests.csv"
                );

        if(!requestFile.exists()){
            return requests;
        }

        try(BufferedReader reader =
                new BufferedReader(
                new FileReader(requestFile)
                )){

            String line;

            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length >= 15
                        && data[12].trim().equalsIgnoreCase("Pending")){

                    requests.add(data);
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return requests;
    }

    public boolean approvePayrollDisputeRequest(
            String disputeId,
            String reviewedBy
            ) {

        return reviewPayrollDisputeRequest(
                disputeId,
                reviewedBy,
                true
                );
    }

    public boolean rejectPayrollDisputeRequest(
            String disputeId,
            String reviewedBy
            ) {

        return reviewPayrollDisputeRequest(
                disputeId,
                reviewedBy,
                false
                );
    }

    private boolean reviewPayrollDisputeRequest(
            String disputeId,
            String reviewedBy,
            boolean approved
            ) {

        try {

            File requestFile =
                    new File(
                    "src/main/resources/PayrollDisputeRequests.csv"
                    );

            if(!requestFile.exists()){
                return false;
            }

            List<String> lines =
                    Files.readAllLines(
                    requestFile.toPath()
                    );

            boolean found =
                    false;

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        lines.get(i).split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length < 15){
                    continue;
                }

                if(data[0].trim().equals(disputeId.trim())
                        && data[12].trim().equalsIgnoreCase("Pending")){

                    data[12] =
                            approved
                            ? "Approved"
                            : "Rejected";

                    data[13] =
                            reviewedBy;

                    data[14] =
                            java.time.LocalDateTime.now().toString();

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    found = true;
                    break;
                }
            }

            if(!found){
                return false;
            }

            Files.write(
                    requestFile.toPath(),
                    lines
                    );

            return true;

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // DELETE EMPLOYEE FROM CSV
    // =========================

    public boolean deleteEmployeeFromCSV(
            String employeeId) {

        try {

            File inputFile =
                    new File(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    );

            File deletedFile =
                    new File(
                    "src/main/resources/Separated_Employees.csv"
                    );

            List<String> lines =
                    Files.readAllLines(
                    inputFile.toPath()
                    );

            List<String> updatedLines =
                    new ArrayList<>();

            String header =
                    lines.get(0);

            String deletedEmployeeLine =
                    null;

            for(String line : lines){

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length > 0
                        && data[0].trim().equals(employeeId.trim())){
                    deletedEmployeeLine = line;
                    continue;
                }

                updatedLines.add(line);
            }

            if (deletedEmployeeLine == null) {

                return false;
            }

            if (!deletedFile.exists()) {

                List<String> deletedLines =
                        new ArrayList<>();

                deletedLines.add(header);
                deletedLines.add(deletedEmployeeLine);

                Files.write(
                        deletedFile.toPath(),
                        deletedLines
                        );

            } else {

                try (FileWriter writer =
                        new FileWriter(deletedFile, true)) {

                    writer.write(
                            System.lineSeparator()
                            + deletedEmployeeLine
                            );
                }
            }

            Files.write(
                    inputFile.toPath(),
                    updatedLines
                    );
            return true;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    public boolean deactivateEmployee(
            String employeeId
            ){

        try{

            File activeFile =
                    new File(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    );

            File inactiveFile =
                    new File(
                    "src/main/resources/InactiveEmployees.csv"
                    );

            List<String> lines =
                    Files.readAllLines(
                    activeFile.toPath()
                    );

            if(lines.isEmpty()){
                return false;
            }

            String header =
                    lines.get(0);

            List<String> activeEmployees =
                    new ArrayList<>();

            activeEmployees.add(header);

            String inactiveEmployee =
                    null;

            for(int i = 1; i < lines.size(); i++){

                String line =
                        lines.get(i);

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if(data.length == 0){
                    continue;
                }

                if(data[0].trim().equals(employeeId.trim())){

                    inactiveEmployee = line;

                }else{

                    activeEmployees.add(line);
                }
            }

            if(inactiveEmployee == null){
                return false;
            }

            if(!inactiveFile.exists()){

                List<String> inactiveLines =
                        new ArrayList<>();

                inactiveLines.add(header);
                inactiveLines.add(inactiveEmployee);

                Files.write(
                        inactiveFile.toPath(),
                        inactiveLines
                        );

            }else{

                try(FileWriter writer =
                        new FileWriter(
                        inactiveFile,
                        true
                        )){

                    writer.write(
                            System.lineSeparator()
                            + inactiveEmployee
                            );
                }
            }

            Files.write(
                    activeFile.toPath(),
                    activeEmployees
                    );

            return true;

        }catch(Exception e){

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    e.toString()
                    );

            return false;
        }
    }

    private double parseMoneyValue(
            String value
            ) {

        if(value == null){
            return 0;
        }

        return Double.parseDouble(
                value.replace("\"", "")
                .replace(",", "")
                .trim()
                );
    }
}
