package com.mycompany.motorphemployeeapp;

public class Employee {

    // =========================
    // EMPLOYEE INFORMATION
    // =========================

    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String birthday;
    private String address;
    private String supervisor;
    private String dateHired;

    // =========================
    // EMPLOYMENT INFORMATION
    // =========================

    private double hourlyRate;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlyRate;

    private String position;
    private String password;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Employee(
            String employeeNumber,
            String firstName,
            String lastName,
            String birthday,
            double hourlyRate,
            double basicSalary,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance,
            double grossSemiMonthlyRate,
            String position,
            String password,
            String address,
            String supervisor
            ) {

        this(
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
                ""
                );
    }

    public Employee(
            String employeeNumber,
            String firstName,
            String lastName,
            String birthday,
            double hourlyRate,
            double basicSalary,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance,
            double grossSemiMonthlyRate,
            String position,
            String password,
            String address,
            String supervisor,
            String dateHired
            ) {

        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;

        this.hourlyRate = hourlyRate;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;

        this.position = position;
        this.password = password;

        this.address = address;
        this.supervisor = supervisor;
        this.dateHired = dateHired;
    }

    // =========================
    // GETTERS
    // =========================

    public String getEmployeeNumber() {

        return employeeNumber;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public String getBirthday() {

        return birthday;
    }

    public String getAddress() {

        return address;
    }

    public String getSupervisor() {

        return supervisor;
    }

    public String getDateHired() {

        return dateHired;
    }

    public double getHourlyRate() {

        return hourlyRate;
    }

    public double getBasicSalary() {

        return basicSalary;
    }

    public double getRiceSubsidy() {

        return riceSubsidy;
    }

    public double getPhoneAllowance() {

        return phoneAllowance;
    }

    public double getClothingAllowance() {

        return clothingAllowance;
    }

    public double getGrossSemiMonthlyRate() {

        return grossSemiMonthlyRate;
    }

    public String getPosition() {

        return position;
    }

    public String getPassword() {

        return password;
    }

    // =========================
    // SETTERS
    // =========================

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public void setBirthday(String birthday) {

        this.birthday = birthday;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public void setSupervisor(String supervisor) {

        this.supervisor = supervisor;
    }

    public void setDateHired(String dateHired) {

        this.dateHired = dateHired;
    }

    public void setHourlyRate(double hourlyRate) {

        this.hourlyRate = hourlyRate;
    }

    public void setBasicSalary(double basicSalary) {

        this.basicSalary = basicSalary;
    }

    public void setRiceSubsidy(double riceSubsidy) {

        this.riceSubsidy = riceSubsidy;
    }

    public void setPhoneAllowance(double phoneAllowance) {

        this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(double clothingAllowance) {

        this.clothingAllowance = clothingAllowance;
    }

    public void setGrossSemiMonthlyRate(
            double grossSemiMonthlyRate
            ) {

        this.grossSemiMonthlyRate =
                grossSemiMonthlyRate;
    }

    public void setPosition(String position) {

        this.position = position;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    // =========================
    // DISPLAY EMPLOYEE INFO
    // =========================

    public void getEmployeeInfo() {

        System.out.println(
                "================================"
                );

        System.out.println(
                "Employee Number: "
                + employeeNumber
                );

        System.out.println(
                "Employee Name: "
                + lastName
                + ", "
                + firstName
                );

        System.out.println(
                "Birthday: "
                + birthday
                );

        System.out.println(
                "Address: "
                + address
                );

        System.out.println(
                "Supervisor: "
                + supervisor
                );

        System.out.println(
                "Position: "
                + position
                );

        System.out.println(
                "Basic Salary: "
                + basicSalary
                );

        System.out.println(
                "Hourly Rate: "
                + hourlyRate
                );

        System.out.println(
                "================================"
                );
    }

    // =========================
    // DISPLAY HEADER
    // =========================

    public void displayEmployeeHeader() {

        System.out.println(
                "================================"
                );

        System.out.println(
                "Employee #: "
                + employeeNumber
                );

        System.out.println(
                "Employee Name: "
                + lastName
                + ", "
                + firstName
                );

        System.out.println(
                "Position: "
                + position
                );

        System.out.println(
                "================================"
                );
    }
}
