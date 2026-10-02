# MotorPH Employee App

MotorPH Employee App is a Java Swing-based Human Resource and Payroll Management System developed as the final project for **MO-IT103 Computer Programming 2**.

The application provides an integrated platform for employee management, attendance monitoring, payroll processing, compensation management, leave and overtime requests, payroll disputes, and payslip viewing through a modern Java Swing graphical user interface.

---

# FEATURES IMPLEMENTED

The application implements all approved MotorPH Change Requests:

- Feature 1 – Graphical User Interface (GUI)
- Feature 2 – Employee Record Viewing and Creation
- Feature 3 – Salary Computation
- Feature 4 – Employee Record Update and Deactivation
- Feature 5 – Payroll Summary Display

Additional enhancements were also implemented beyond the required features to improve usability, validation, reporting, and payroll management.

---

# TECHNOLOGIES USED

- Java 17
- Java Swing
- Maven
- Apache NetBeans IDE
- CSV File Storage
- iText PDF

---

# REQUIREMENTS

- Java JDK 17 or later
- Apache NetBeans IDE
- Maven

---

# PROJECT RESOURCES

Ensure the following CSV files are located in:

```
src/main/resources/
```

### Employee Data

- MotorPH_Employee Data - Employee Details.csv

### Attendance Data

- MotorPH_Employee Data - Attendance Record.csv

### Sample Bulk Import File

- JuneNewEmployees.csv

---

# HOW TO RUN

1. Open the project using Apache NetBeans.
2. Allow Maven to download all project dependencies.
3. Run the following main class:

```
com.mycompany.motorphemployeeapp.MotorPHEmployeeApp
```

---

# LOGIN CREDENTIALS

## Default Password

```
12345
```

### Employee Dashboard

Employee Number

```
10001
```

Password

```
12345
```

---

### HR Dashboard

Employee Number

```
10006
```

Password

```
12345
```

---

### Payroll Dashboard

Employee Number

```
10012
```

Password

```
12345
```

---

# SYSTEM MODULES

## Employee Module

The Employee Dashboard includes:

- Dashboard Overview
- Daily Time Record (DTR)
- Timesheet Viewing
- Leave Request Submission
- Overtime Request Submission
- Payslip Viewing
- Payroll Dispute Submission

---

## Human Resource Module

The HR Dashboard includes:

- Employee List
- Employee Search
- Employee Sorting
- Employee Record Viewing
- Add Employee
- Bulk Employee Import
- Edit Employee Information
- Employee Deactivation
- Compensation Management
- Automatic Employee Table Refresh

---

## Payroll Module

The Payroll Dashboard includes:

- Individual Payroll Processing
- Bulk Payroll Processing
- Payroll Processing for All Cutoffs
- Payroll Summary Dashboard
- Payroll Validation
- Payroll Search
- Payroll Sorting
- Payroll Status Management
- Payroll Report PDF Export

---

# FEATURE IMPLEMENTATION

## Feature 1 – Graphical User Interface

Implemented:

- Login Screen
- Employee Dashboard
- HR Dashboard
- Payroll Dashboard
- Responsive Navigation Panels
- Modern Java Swing Interface
- Interactive Components
- JTable Data Presentation
- Custom Rounded Buttons
- Input Validation
- Exception Handling

---

## Feature 2 – Employee Record Management

Implemented:

- Employee List Viewing
- Employee Search
- Employee Sorting
- Employee Record Viewing
- Add Employee
- Bulk Employee Import
- Duplicate Employee Detection
- Duplicate Employee Number Validation
- CSV-Based Data Storage
- Automatic JTable Refresh
- Employee Information Validation

---

## Feature 3 – Payroll and Salary Computation

Implemented:

- Single Employee Payroll Processing
- Bulk Payroll Processing
- Payroll Processing for All Employees
- Payroll Processing by Cutoff Period
- Gross Pay Computation
- Hourly Rate Computation
- Overtime Computation
- Allowance Computation
- Semi-Monthly Payroll Computation
- Government Deduction Computation
- Payroll Validation
- Payroll Status Tracking

### Government Deductions

- SSS
- PhilHealth
- Pag-IBIG
- Withholding Tax

### Salary Computation

- Hourly Pay
- Overtime Pay
- Gross Pay
- Total Allowance
- Total Deductions
- Net Salary

---

## Feature 4 – Employee Update and Deactivation

Implemented:

- View Employee Information
- Edit Employee Information
- Employee Record Validation
- Employee Record Update
- Employee Deactivation
- CSV Record Rewriting
- Confirmation Dialogs
- Automatic Employee Table Refresh
- Error Handling

---

## Feature 5 – Payroll Summary Display

Implemented:

- Payroll Summary Generation
- Number of Employees Summary
- Total Gross Pay Calculation
- Total Deductions Calculation
- Average Net Pay Calculation
- Payroll Summary Dashboard
- Automatic Summary Updates After Payroll Processing
- Summary Display for Single Employee Payroll
- Summary Display for Bulk Payroll Processing
- Summary Display for All Payroll Cutoffs
- Reuse of Existing Salary Computation Logic
- Reuse of Existing Deduction Computation Logic
- Modular Payroll Summary Helper Methods

Summary information displayed:

- Number of Employees
- Total Gross Pay
- Total Deductions
- Average Net Pay

---

# ADDITIONAL SYSTEM FEATURES

## Attendance Management

Implemented:

- Attendance Record Loading
- Cutoff-Based Attendance Filtering
- Dynamic Hours Worked Computation
- Overtime Hours Computation

---

## Compensation Management

Implemented:

- Monthly Salary
- Hourly Rate
- Rice Subsidy
- Phone Allowance
- Clothing Allowance

---

## Payroll Dashboard

Implemented:

- Number of Employees Dashboard Card
- Payroll Timeline
- Payroll Search
- Payroll Sorting
- Payroll Validation
- Payroll Status Management
- Payroll Summary
- PDF Export

---

## Employee Validation

The application validates:

- Required Fields
- Empty Fields
- Employee Number
- Phone Number
- Birthday
- Monthly Salary
- Rice Subsidy
- Phone Allowance
- Clothing Allowance
- SSS Number
- PhilHealth Number
- TIN
- Pag-IBIG Number

---

## Bulk Employee Import

The application:

- Validates CSV Format
- Detects Duplicate Employees
- Detects Duplicate Employee Numbers
- Skips Existing Employees
- Imports Valid Records Only
- Displays Import Summary

---

## Error Handling

Implemented:

- Login Validation
- File Read Errors
- File Write Errors
- CSV Validation
- Number Format Validation
- Confirmation Dialogs
- Runtime Exception Handling
- Payroll Validation

---

# DATA MANAGEMENT

The application uses CSV files as its primary data storage.

Implemented operations:

- Employee Record Loading
- Employee Record Creation
- Employee Record Updating
- Employee Record Deactivation
- Attendance Record Loading
- Employee Search
- Employee Filtering
- Employee Sorting
- Bulk Employee Import
- Payroll Processing
- Payroll Summary Generation
- CSV Read Operations
- CSV Write Operations

Primary Data Manager:

```
EmployeeDataManager.java
```

---

# PROJECT STRUCTURE

## Main Application

- MotorPHEmployeeApp.java
- LoginFrame.java
- UserAuthentication.java

---

## Dashboards

- EmployeeDashboard.java
- HRDashboard.java
- PayrollDashboard.java

---

## Employee Management

- Employee.java
- EmployeeDataManager.java
- HREmployeeListPanel.java
- AddEmployeeDialog.java
- BulkAddEmployeeDialog.java
- EditEmployee.java
- ViewEmployee.java
- EmployeeRemoval.java

---

## Payroll

- PayrollPanel.java
- Payroll.java
- SalaryComputationModule.java
- Deduction.java

---

## Attendance

- AttendanceRecord.java
- DailyTimeRecordPanel.java
- TimeSheetPanel.java

---

## Employee Services

- MyPayslipPanel.java
- TimeOffRequestPanel.java
- PayrollDisputePanel.java
- OvertimeRequestPanel.java

---

## Utilities

- RoundedButton.java

---

# BULK EMPLOYEE IMPORT

## Steps

1. Login as HR.

Employee Number:

```
10006
```

Password:

```
12345
```

2. Open **Employee List**.

3. Click **Add Bulk**.

4. Select:

```
src/main/resources/JuneNewEmployees.csv
```

5. Click **Import**.

The system automatically:

- Validates employee information
- Detects duplicate employees
- Detects duplicate employee numbers
- Skips existing records
- Imports valid employees
- Refreshes the employee table
- Displays the import summary

---

# PAYROLL PROCESSING

The Payroll Dashboard supports:

- Individual Employee Payroll
- Bulk Payroll Processing
- Payroll Processing for All Cutoffs
- Payroll Processing by Payroll Cutoff
- Payroll Summary Display
- Payroll Validation
- Payroll Report Export

---

# PDF EXPORT

Payroll reports can be exported as PDF files.

The generated report includes:

- Payroll Report Title
- Selected Payroll Cutoff
- Payroll Summary
  - Number of Employees
  - Total Gross Pay
  - Total Deductions
  - Average Net Pay
- Employee Payroll Table
- Gross Pay
- Allowances
- Deductions
- Net Salary

---

# AUTHORS

- Dether Dacuma
- Heidi Dsouza
- Andrea Victoria Magallanes
- Rodine Dayn Hidalgo

---

Developed as the final project for

**MO-IT103 Computer Programming 2**

---

# LICENSE

This project was developed for educational purposes only.

The application demonstrates Java Swing desktop application development, employee management, attendance monitoring, payroll processing, payroll summary reporting, compensation management, CSV-based persistence, and PDF report generation using object-oriented programming principles.
