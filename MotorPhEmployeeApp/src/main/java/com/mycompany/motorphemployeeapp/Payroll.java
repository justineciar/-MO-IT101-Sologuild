package com.mycompany.motorphemployeeapp;

public class Payroll {

    private double regularHours;
    private double overtimeHours;
    private double hourlyRate;

    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;

    private double grossSalary;
    private double totalDeductions;
    private double withholdingTax;
    private double netSalary;

    public Payroll() {
    }

    // =========================
    // GROSS PAY
    // =========================

    public double calculateGrossSalary(
            double regularHours,
            double overtimeHours,
            double hourlyRate,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance
            ) {

        this.regularHours = regularHours;
        this.overtimeHours = overtimeHours;
        this.hourlyRate = hourlyRate;

        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;

        double regularPay =
                regularHours * hourlyRate;

        double overtimePay =
                overtimeHours
                * hourlyRate
                * 1.25;

        grossSalary =
                regularPay
                + overtimePay
                + riceSubsidy
                + phoneAllowance
                + clothingAllowance;

        return grossSalary;
    }

    // =========================
    // DEDUCTIONS
    // =========================

    public double calculateTotalDeductions(
            double sss,
            double philHealth,
            double pagIbig
            ) {

        totalDeductions =
                sss
                + philHealth
                + pagIbig;

        return totalDeductions;
    }

    // =========================
    // WITHHOLDING TAX
    // =========================

    public double calculateWithholdingTax(
            double tax
            ) {

        withholdingTax = tax;

        return withholdingTax;
    }

    // =========================
    // NET PAY
    // =========================

    public double calculateNetSalary(
            double grossSalary,
            double deductions,
            double withholdingTax
            ) {

        netSalary =
                grossSalary
                - deductions
                - withholdingTax;

        return netSalary;
    }

    // =========================
    // GETTERS
    // =========================

    public double getGrossSalary() {

        return grossSalary;
    }

    public double getTotalDeductions() {

        return totalDeductions;
    }

    public double getWithholdingTax() {

        return withholdingTax;
    }

    public double getNetSalary() {

        return netSalary;
    }
}
