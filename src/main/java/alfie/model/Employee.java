/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.model;

public class Employee {

    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String birthDate;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String status;
    private String position;
    private String immediateSupervision;
    private Double basicSalary;
    private Double riceSubsidy;
    private Double phoneAllowance;
    private Double clothingAllowance;
    private Double grossRate;
    private Double monthlyRate;

    public Employee() {
    }

    // Full constructor (optional)
    public Employee(String employeeNumber, String lastName, String firstName, String birthDate,
                    String address, String phoneNumber, String sssNumber, String philHealthNumber,
                    String tin, String pagIbigNumber, String status, String position,
                    String immediateSupervision, String basicSalary, String riceSubsidy,
                    String phoneAllowance, String clothingAllowance, String grossRate, String monthlyRate) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tin = tin;
        this.pagIbigNumber = pagIbigNumber;
        this.status = status;
        this.position = position;
        this.immediateSupervision = immediateSupervision;
        this.basicSalary = parseDoubleOrZero(basicSalary);
        this.riceSubsidy = parseDoubleOrZero(riceSubsidy);
        this.phoneAllowance = parseDoubleOrZero(phoneAllowance);
        this.clothingAllowance = parseDoubleOrZero(clothingAllowance);
        this.grossRate = parseDoubleOrZero(grossRate);
        this.monthlyRate = parseDoubleOrZero (monthlyRate);
    }

    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    public String toCSV() {
        return String.join(",",
            employeeNumber, lastName, firstName, birthDate, address, phoneNumber,
            sssNumber, philHealthNumber, tin, pagIbigNumber, status, position,
            immediateSupervision,
            String.valueOf(basicSalary), String.valueOf(riceSubsidy),
            String.valueOf(phoneAllowance), String.valueOf(clothingAllowance),
            String.valueOf(grossRate), String.valueOf(monthlyRate)
        );
    }

    // Getters
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getBirthDay() { return birthDate; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilHealthNumber() { return philHealthNumber; }
    public String getTin() { return tin; }
    public String getPagIbigNumber() { return pagIbigNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervision() { return immediateSupervision; }
    public Double getBasicSalary() { return basicSalary; }
    public Double getRiceSubsidy() { return riceSubsidy; }
    public Double getPhoneAllowance() { return phoneAllowance; }
    public Double getClothingAllowance() { return clothingAllowance; }
    public Double getGrossRate() { return grossRate; }
    public Double getMonthlyRate() { return monthlyRate; }

    // Setters
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }
    public void setTin(String tin) { this.tin = tin; }
    public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setPosition(String position) { this.position = position; }
    public void setImmediateSupervision(String immediateSupervision) { this.immediateSupervision = immediateSupervision; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }
    public void setRiceSubsidy(Double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setPhoneAllowance(Double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    public void setClothingAllowance(Double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setGrossRate(Double grossRate) { this.grossRate = grossRate; }
    public void setMonthlyRate(Double monthlyRate) { this.monthlyRate = monthlyRate; }
}