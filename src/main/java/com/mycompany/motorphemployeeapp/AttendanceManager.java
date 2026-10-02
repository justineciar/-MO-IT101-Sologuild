package com.mycompany.motorphemployeeapp;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceManager {

    private static final String ACTIVE_SESSION_FILE =
            "src/main/resources/ActiveClockSessions.csv";

    private static final String ATTENDANCE_FILE =
            "src/main/resources/MotorPH_Employee Data - Attendance Record.csv";

    private boolean isClockedIn;
    private boolean isBreakStarted;
    private LocalTime clockInTime;
    private LocalTime breakStartTime;
    private int breakSeconds;
    private long workedMinutes;
    private boolean isIdle;

    public boolean clockOut(Employee employee) {

        if (isBreakStarted) {

            breakSeconds = getTotalBreakSeconds();

            breakStartTime = null;
            isBreakStarted = false;
        }

        LocalTime clockOutTime = LocalTime.now();

        workedMinutes =
                Duration.between(
                clockInTime,
                clockOutTime
                ).toMinutes();

        workedMinutes -= breakSeconds / 60;

        if (workedMinutes < 0) {
            workedMinutes = 0;
        }

        boolean attendanceSaved =
                appendAttendanceRecord(employee, clockOutTime);

        if (!attendanceSaved) {
            return false;
        }

        removeActiveClockSession(employee);

        isClockedIn = false;
        isBreakStarted = false;

        clockInTime = null;
        breakStartTime = null;

        breakSeconds = 0;
        isIdle = false;

        return true;
    }

    public void restoreClockSession(Employee employee) {

        File file = new File(ACTIVE_SESSION_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length < 6) {
                    continue;
                }

                if (!data[0].trim().equals(employee.getEmployeeNumber())) {
                    continue;
                }

                if (!data[1].trim().equals(LocalDate.now().toString())) {
                    continue;
                }

                clockInTime = LocalTime.parse(data[2].trim());

                breakSeconds = Integer.parseInt(data[3].trim());

                isBreakStarted = Boolean.parseBoolean(data[4].trim());

                if (isBreakStarted && !data[5].trim().isEmpty()) {
                    breakStartTime = LocalTime.parse(data[5].trim());
                }

                isClockedIn = true;

                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveActiveClockSession(Employee employee) {

        try {

            File file =
                    new File(ACTIVE_SESSION_FILE);

            List<String> lines =
                    new ArrayList<>();

            lines.add(
                    "Employee #,Date,Clock In,Break Seconds,Break Started,Break Start"
                    );

            if(file.exists()){

                List<String> existingLines =
                        Files.readAllLines(
                        file.toPath()
                        );

                for(int i = 1; i < existingLines.size(); i++){

                    String line =
                            existingLines.get(i);

                    String[] data =
                            line.split(
                            ",",
                            -1
                            );

                    if(data.length > 1
                            && data[0].trim().equals(employee.getEmployeeNumber())
                            && data[1].trim().equals(LocalDate.now().toString())){

                        continue;
                    }

                    lines.add(line);
                }
            }

            String breakStart =
                    breakStartTime == null
                    ? ""
                    : breakStartTime.toString();

            lines.add(
                    employee.getEmployeeNumber()
                    + ","
                    + LocalDate.now()
                    + ","
                    + clockInTime
                    + ","
                    + breakSeconds
                    + ","
                    + isBreakStarted
                    + ","
                    + breakStart
                    );

            Files.write(
                    file.toPath(),
                    lines
                    );

        } catch(Exception e){

            e.printStackTrace();
        }
    }

    private void removeActiveClockSession(Employee employee) {

        File file =
                new File(ACTIVE_SESSION_FILE);

        if(!file.exists()){
            return;
        }

        try {

            List<String> existingLines =
                    Files.readAllLines(
                    file.toPath()
                    );

            List<String> updatedLines =
                    new ArrayList<>();

            if(existingLines.isEmpty()){
                return;
            }

            updatedLines.add(existingLines.get(0));

            for(int i = 1; i < existingLines.size(); i++){

                String line =
                        existingLines.get(i);

                String[] data =
                        line.split(
                        ",",
                        -1
                        );

                if(data.length > 1
                        && data[0].trim().equals(employee.getEmployeeNumber())
                        && data[1].trim().equals(LocalDate.now().toString())){

                    continue;
                }

                updatedLines.add(line);
            }

            Files.write(
                    file.toPath(),
                    updatedLines
                    );

        } catch(Exception e){

            e.printStackTrace();
        }
    }

    private boolean appendAttendanceRecord(
            Employee employee,
            LocalTime clockOutTime
            ) {

        try {

            File file =
                    new File(ATTENDANCE_FILE);

            boolean fileExists =
                    file.exists();

            if(fileExists){
                ensureAttendanceHeader(file);
            }

            try(FileWriter writer =
                    new FileWriter(
                    file,
                    true
                    )){

                if(!fileExists){

                    writer.write(
                            "Employee #,Last Name,First Name,Date,Log In,Log Out,Break Minutes,Status"
                            );
                }

                writer.write(
                        System.lineSeparator()
                        + employee.getEmployeeNumber()
                        + ","
                        + employee.getLastName()
                        + ","
                        + employee.getFirstName()
                        + ","
                        + LocalDate.now().format(
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        )
                        + ","
                        + clockInTime.format(
                        DateTimeFormatter.ofPattern("H:mm")
                        )
                        + ","
                        + clockOutTime.format(
                        DateTimeFormatter.ofPattern("H:mm")
                        )
                        + ","
                        + (breakSeconds / 60)
                        + ","
                        + (isIdle ? "Idle" : "Present")
                        );
            }

            return true;

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    private void ensureAttendanceHeader(File file) throws Exception {

        List<String> lines = Files.readAllLines(file.toPath());

        if (lines.isEmpty()) {
            return;
        }

        String header = lines.get(0);

        if (!header.toLowerCase().contains("break minutes")) {
            header += ",Break Minutes";
        }

        if (!header.toLowerCase().contains("status")) {
            header += ",Status";
        }

        lines.set(0, header);

        Files.write(file.toPath(), lines);
    }

    public boolean hasCompletedAttendanceToday(Employee employee) {

        File file = new File(ATTENDANCE_FILE);

        if (!file.exists()) {
            return false;
        }

        String today =
                LocalDate.now().format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length >= 8
                        && data[0].trim().equals(employee.getEmployeeNumber())
                        && data[3].trim().equals(today)) {

                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public AttendanceManager() {
    }

    public boolean isClockedIn() {
        return isClockedIn;
    }

    public boolean isBreakStarted() {
        return isBreakStarted;
    }

    public LocalTime getClockInTime() {
        return clockInTime;
    }

    public int getTotalBreakSeconds() {

        if (isBreakStarted && breakStartTime != null) {

            return breakSeconds
                    + (int) Duration.between(
                    breakStartTime,
                    LocalTime.now()
                    ).getSeconds();
        }

        return breakSeconds;
    }

    public void clockIn(Employee employee) {

        clockInTime = LocalTime.now();

        breakSeconds = 0;
        breakStartTime = null;

        isClockedIn = true;
        isBreakStarted = false;

        saveActiveClockSession(employee);
    }

    public void startMealBreak(Employee employee) {

        breakStartTime = LocalTime.now();

        isBreakStarted = true;

        saveActiveClockSession(employee);
    }

    public void stopMealBreak(Employee employee) {

        if (!isBreakStarted || breakStartTime == null) {
            return;
        }

        int currentBreakSeconds =
                (int) Duration.between(
                breakStartTime,
                LocalTime.now()
                ).getSeconds();

        breakSeconds += currentBreakSeconds;

        if (breakSeconds > 3600) {
            isIdle = true;
        } else {
            isIdle = false;
        }

        breakStartTime = null;

        isBreakStarted = false;

        saveActiveClockSession(employee);
    }

    public boolean isIdle() {
        return isIdle;
    }

    public String getWorkedHours() {

        long hours = workedMinutes / 60;

        long minutes = workedMinutes % 60;

        return hours
                + ":"
                + String.format("%02d", minutes)
                + " hours";
    }

}
