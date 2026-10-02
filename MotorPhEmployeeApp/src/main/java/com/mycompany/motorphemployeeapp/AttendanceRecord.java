package com.mycompany.motorphemployeeapp;

import java.time.Duration;
import java.time.LocalTime;

public class AttendanceRecord {

    private String employeeNumber;
    private String attendanceDate;
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private double hoursWorked;
    private int breakMinutes;
    private String status = "Present";

    public AttendanceRecord(
            String employeeNumber,
            String attendanceDate,
            LocalTime loginTime,
            LocalTime logoutTime
            ) {

        this(
                employeeNumber,
                attendanceDate,
                loginTime,
                logoutTime,
                0
                );
    }

    public AttendanceRecord(
            String employeeNumber,
            String attendanceDate,
            LocalTime loginTime,
            LocalTime logoutTime,
            int breakMinutes
            ) {

        this.employeeNumber = employeeNumber;
        this.attendanceDate = attendanceDate;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.breakMinutes = breakMinutes;
    }

    // =========================
    // COMPUTE REGULAR HOURS
    // =========================

    public double computeHours() {

        LocalTime officialStart =
                LocalTime.of(8, 0);

        LocalTime officialEnd =
                LocalTime.of(17, 0);

        LocalTime adjustedLogin =
                loginTime;

        LocalTime adjustedLogout =
                logoutTime;

        if (adjustedLogin.isBefore(
                officialStart
                )) {

            adjustedLogin =
                    officialStart;
        }

        if (adjustedLogout.isAfter(
                officialEnd
                )) {

            adjustedLogout =
                    officialEnd;
        }

        hoursWorked =
                Duration.between(
                adjustedLogin,
                adjustedLogout
                ).toMinutes() / 60.0;

        hoursWorked -=
                breakMinutes / 60.0;

        if (hoursWorked < 0) {

            hoursWorked = 0;
        }

        return hoursWorked;
    }

    // =========================
    // COMPUTE OVERTIME HOURS
    // =========================

    public double computeOvertimeHours() {

        LocalTime officialEnd =
                LocalTime.of(17, 0);

        if (logoutTime.isAfter(
                officialEnd
                )) {

            return Duration.between(
                    officialEnd,
                    logoutTime
                    ).toMinutes() / 60.0;
        }

        return 0;
    }

    // =========================
    // GETTERS
    // =========================

    public String getEmployeeNumber() {

        return employeeNumber;
    }

    public String getDate() {

        return attendanceDate;
    }

    public String getTimeIn() {

        return loginTime.toString();
    }

    public String getTimeOut() {

        return logoutTime.toString();
    }

    public double getOvertimeHours() {

        return computeOvertimeHours();
    }

    public int getBreakMinutes() {

        return breakMinutes;
    }

    public String getBreakDisplay() {

        int hours =
                breakMinutes / 60;

        int minutes =
                breakMinutes % 60;

        return hours + ":"
                + String.format("%02d", minutes)
                + " hour";
    }

    public double getTotalHoursWorked() {

        return computeHours();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
