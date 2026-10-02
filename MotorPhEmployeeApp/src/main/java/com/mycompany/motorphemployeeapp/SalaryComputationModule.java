package com.mycompany.motorphemployeeapp;

public class SalaryComputationModule {

    public static double computeGrossPay(
            double hourlyRate,
            double hoursWorked,
            double overtimeHours
            ){

        double overtimePay =
                overtimeHours
                * hourlyRate
                * 1.25;

        return (hourlyRate * hoursWorked)
                + overtimePay;
    }

    public static double computeNetPay(
            double grossPay,
            double allowance,
            double deductions
            ){

        return grossPay
                + allowance
                - deductions;
    }
}
