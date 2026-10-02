package com.mycompany.motorphemployeeapp;

public class Deduction {

    // =========================
    // SSS
    // =========================

    public double calculateSSS(
            double monthlySalary
            ) {

        if(monthlySalary < 5250){
            return 250;
        }

        double salaryCredit;

        if(monthlySalary >= 34750){

            salaryCredit =
                    35000;

        }else{

            salaryCredit =
                    Math.floor(
                    (monthlySalary + 250) / 500
                    ) * 500;
        }

        if(salaryCredit < 5000){
            salaryCredit = 5000;
        }

        double regularSalaryCredit =
                Math.min(
                salaryCredit,
                20000
                );

        double mandatoryProvidentFundCredit =
                Math.max(
                0,
                salaryCredit - 20000
                );

        double employeeShare =
                (regularSalaryCredit * 0.05)
                + (mandatoryProvidentFundCredit * 0.05);

        return employeeShare / 2;
    }

    public double computeSSS(
            double monthlySalary
            ) {

        return calculateSSS(
                monthlySalary
                );
    }

    // =========================
    // PHILHEALTH
    // =========================

    public double calculatePhilHealth(
            double monthlySalary
            ) {

        double salaryBasis =
                monthlySalary;

        if(salaryBasis < 10000){

            salaryBasis = 10000;
        }

        if(salaryBasis > 100000){

            salaryBasis = 100000;
        }

        double employeeShare =
                (salaryBasis * 0.05) / 2;

        return employeeShare / 2;
    }

    public double computePhilHealth(
            double monthlySalary
            ) {

        return calculatePhilHealth(
                monthlySalary
                );
    }

    // =========================
    // PAG-IBIG
    // =========================

    public double calculatePagIbig(
            double monthlySalary
            ) {

        double salaryBasis =
                Math.min(
                monthlySalary,
                10000
                );

        double employeeShare =
                salaryBasis * 0.02;

        if(employeeShare > 200){

            employeeShare = 200;
        }

        return employeeShare / 2;
    }

    public double computePagIBIG(
            double monthlySalary
            ) {

        return calculatePagIbig(
                monthlySalary
                );
    }

    // =========================
    // TAX
    // =========================

    public double calculateTax(double taxableIncome) {

        double tax;

        if (taxableIncome <= 20833) {

            tax = 0;

        } else if (taxableIncome <= 33333) {

            tax = (taxableIncome - 20833) * 0.15;

        } else if (taxableIncome <= 66667) {

            tax = 1875 + ((taxableIncome - 33333) * 0.20);

        } else if (taxableIncome <= 166667) {

            tax = 8541.80 + ((taxableIncome - 66667) * 0.25);

        } else if (taxableIncome <= 666667) {

            tax = 33541.80 + ((taxableIncome - 166667) * 0.30);

        } else {

            tax = 183541.80 + ((taxableIncome - 666667) * 0.35);
        }

        return tax / 2;
    }

    public double computeWithholdingTax(
            double taxableIncome
            ) {

        return calculateTax(
                taxableIncome
                );
    }

    // =========================
    // TOTAL DEDUCTIONS
    // =========================

    public double computeDeductions(
            double monthlySalary,
            double taxableIncome
            ) {

        return computeSSS(monthlySalary)
                + computePhilHealth(monthlySalary)
                + computePagIBIG(monthlySalary)
                + computeWithholdingTax(taxableIncome);
    }

}
