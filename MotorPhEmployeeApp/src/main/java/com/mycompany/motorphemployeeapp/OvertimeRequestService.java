package com.mycompany.motorphemployeeapp;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OvertimeRequestService {

    private static final String OVERTIME_REQUEST_FILE =
            "src/main/resources/OvertimeRequests.csv";

    private static final String HEADER =
            "Request ID,Employee ID,Employee Name,Supervisor,Date,Start Time,End Time,Overtime Hours,Reason,Submitted Date,Status,Reviewed By,Review Date";

    public static boolean submitRequest(
            Employee employee,
            String date,
            String startTime,
            String endTime,
            String reason
            ) {

        try {

            File file =
                    getOvertimeRequestFile();

            ensureFileExists(file);

            String requestId =
                    "OT-"
                    + LocalDateTime.now()
                    .format(
                    DateTimeFormatter.ofPattern(
                    "yyyyMMddHHmmssSSS"
                    )
                    );

            String employeeName =
                    employee.getFirstName()
                    + " "
                    + employee.getLastName();

            String[] data = {
                requestId,
                        employee.getEmployeeNumber(),
                        employeeName,
                        employee.getSupervisor(),
                        date,
                        startTime,
                        endTime,
                        String.format(
                        "%.2f",
                        computeRequestedHours(
                        startTime,
                        endTime
                        )
                        ),
                        reason,
                        LocalDateTime.now().toString(),
                        "Pending",
                        "",
                        ""
                    };

            try(BufferedWriter writer =
                    new BufferedWriter(
                    new FileWriter(
                    file,
                    true
                    )
                    )){

                writer.newLine();

                writer.write(
                        formatCSVLine(data)
                        );
            }

            return true;

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    public static List<String[]> getPendingRequestsForSupervisor(
            Employee supervisor
            ) {

        List<String[]> requests =
                new ArrayList<>();

        if(supervisor == null){
            return requests;
        }

        try {

            File file =
                    getOvertimeRequestFile();

            if(!file.exists()){
                return requests;
            }

            List<String> lines =
                    Files.readAllLines(
                    file.toPath()
                    );

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        parseCSVLine(lines.get(i));

                if(data.length >= 13
                        && canReviewerSeeRequest(
                        data[3],
                        supervisor
                        )
                        && data[10].trim().equalsIgnoreCase("Pending")){

                    requests.add(data);
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return requests;
    }

    public static boolean reviewRequest(
            String requestId,
            boolean approve,
            Employee reviewer
            ) {

        try {

            File file =
                    getOvertimeRequestFile();

            if(!file.exists()){
                return false;
            }

            List<String> lines =
                    Files.readAllLines(
                    file.toPath()
                    );

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        parseCSVLine(lines.get(i));

                if(data.length < 13){
                    continue;
                }

                if(data[0].trim().equals(requestId.trim())
                        && data[10].trim().equalsIgnoreCase("Pending")){

                    data[10] =
                            approve
                            ? "Approved"
                            : "Rejected";

                    data[11] =
                            getEmployeeFullName(reviewer);

                    data[12] =
                            LocalDateTime.now().toString();

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    Files.write(
                            file.toPath(),
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

    public static double getApprovedOvertimeHours(
            String employeeId,
            LocalDate attendanceDate
            ) {

        double approvedHours =
                0;

        try {

            File file =
                    getOvertimeRequestFile();

            if(!file.exists()){
                return approvedHours;
            }

            List<String> lines =
                    Files.readAllLines(
                    file.toPath()
                    );

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        parseCSVLine(lines.get(i));

                if(data.length < 13){
                    continue;
                }

                if(!data[1].trim().equals(employeeId.trim())
                        || !data[10].trim().equalsIgnoreCase("Approved")){
                    continue;
                }

                LocalDate requestDate =
                        parseRequestDate(data[4].trim());

                if(requestDate != null
                        && requestDate.equals(attendanceDate)){

                    approvedHours +=
                            parseDouble(data[7]);
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return approvedHours;
    }

    public static double getPayableOvertimeHours(
            String employeeId,
            LocalDate attendanceDate,
            double actualOvertimeHours
            ) {

        double approvedHours =
                getApprovedOvertimeHours(
                employeeId,
                attendanceDate
                );

        return Math.min(
                actualOvertimeHours,
                approvedHours
                );
    }

    public static double computeRequestedHours(
            String startTime,
            String endTime
            ) {

        LocalTime start =
                LocalTime.parse(
                startTime,
                DateTimeFormatter.ofPattern("hh:mm a")
                );

        LocalTime end =
                LocalTime.parse(
                endTime,
                DateTimeFormatter.ofPattern("hh:mm a")
                );

        long minutes =
                Duration.between(
                start,
                end
                ).toMinutes();

        if(minutes < 0){
            minutes += 24 * 60;
        }

        return minutes / 60.0;
    }

    public static String getEmployeeFullName(
            Employee employee
            ) {

        if(employee == null){
            return "";
        }

        return (employee.getFirstName()
                + " "
                + employee.getLastName()).trim();
    }

    private static boolean isSupervisorMatch(
            String requestSupervisor,
            Employee reviewer
            ) {

        String normalizedRequestSupervisor =
                normalizeName(requestSupervisor);

        return normalizedRequestSupervisor.equals(
                normalizeName(
                getEmployeeFullName(reviewer)
                )
                ) || normalizedRequestSupervisor.equals(
                normalizeName(
                getLastNameFirstName(reviewer)
                )
                ) || hasSameNameParts(
                normalizedRequestSupervisor,
                normalizeName(
                getEmployeeFullName(reviewer)
                )
                ) || hasSameNameParts(
                normalizedRequestSupervisor,
                normalizeName(
                getLastNameFirstName(reviewer)
                )
                );
    }

    private static boolean canReviewerSeeRequest(
            String requestSupervisor,
            Employee reviewer
            ) {

        if(isSupervisorMatch(
                requestSupervisor,
                reviewer
                )){

            return true;
        }

        return isFallbackReviewer(reviewer)
                && !doesSupervisorExist(requestSupervisor);
    }

    private static boolean isFallbackReviewer(
            Employee reviewer
            ) {

        if(reviewer == null
                || reviewer.getPosition() == null){

            return false;
        }

        String position =
                reviewer.getPosition()
                .trim()
                .toLowerCase();

        return position.equals("hr manager")
                || position.equals("payroll manager");
    }

    private static boolean doesSupervisorExist(
            String requestSupervisor
            ) {

        try {

            File file =
                    new File(
                    "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                    );

            if(!file.exists()){
                return false;
            }

            try(BufferedReader reader =
                    new BufferedReader(
                    new FileReader(file)
                    )){

                String line;

                reader.readLine();

                while((line = reader.readLine()) != null){

                    String[] data =
                            parseCSVLine(line);

                    if(data.length < 13){
                        continue;
                    }

                    String employeeName =
                            data[2] + " " + data[1];

                    String lastNameFirstName =
                            data[1] + ", " + data[2];

                    if(hasSameNameParts(
                            normalizeName(requestSupervisor),
                            normalizeName(employeeName)
                            ) || hasSameNameParts(
                            normalizeName(requestSupervisor),
                            normalizeName(lastNameFirstName)
                            )){

                        return true;
                    }
                }
            }

        } catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    private static String getLastNameFirstName(
            Employee employee
            ) {

        if(employee == null){
            return "";
        }

        return (employee.getLastName()
                + ", "
                + employee.getFirstName()).trim();
    }

    private static String normalizeName(
            String name
            ) {

        if(name == null){
            return "";
        }

        return name.replace("\"", "")
                .replace(",", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
    }

    private static boolean hasSameNameParts(
            String firstName,
            String secondName
            ) {

        if(firstName.isEmpty()
                || secondName.isEmpty()){

            return false;
        }

        String[] firstParts =
                firstName.split(" ");

        String[] secondParts =
                secondName.split(" ");

        Arrays.sort(firstParts);
        Arrays.sort(secondParts);

        if(Arrays.equals(
                firstParts,
                secondParts
                )){
            return true;
        }

        return hasEnoughSharedNameParts(
                firstParts,
                secondParts
                );
    }

    private static boolean hasEnoughSharedNameParts(
            String[] firstParts,
            String[] secondParts
            ) {

        int sharedParts =
                0;

        for(String firstPart : firstParts){

            for(String secondPart : secondParts){

                if(firstPart.equals(secondPart)){
                    sharedParts++;
                    break;
                }
            }
        }

        return sharedParts >= 2;
    }

    private static File getOvertimeRequestFile() {

        return new File(
                OVERTIME_REQUEST_FILE
                );
    }

    private static void ensureFileExists(
            File file
            ) throws Exception {

        File parent =
                file.getParentFile();

        if(parent != null
                && !parent.exists()){

            parent.mkdirs();
        }

        if(!file.exists()){

            try(BufferedWriter writer =
                    new BufferedWriter(
                    new FileWriter(file)
                    )){

                writer.write(HEADER);
            }
        }
    }

    private static LocalDate parseRequestDate(
            String dateText
            ) {

        String[] formats = {
            "MM/dd/yyyy",
                    "M/d/yyyy"
                };

        for(String format : formats){

            try {

                return LocalDate.parse(
                        dateText,
                        DateTimeFormatter.ofPattern(format)
                        );

            } catch(Exception e){

                // Try the next accepted format.
            }
        }

        return null;
    }

    private static double parseDouble(
            String value
            ) {

        try {

            return Double.parseDouble(
                    value.replace("\"", "")
                    .replace(",", "")
                    .trim()
                    );

        } catch(Exception e){

            return 0;
        }
    }

    private static String[] parseCSVLine(String line) {

        return line.split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                -1
                );
    }

    private static String formatCSVLine(String[] data) {

        StringBuilder line =
                new StringBuilder();

        for(int i = 0; i < data.length; i++){

            String value =
                    data[i] == null ? "" : data[i].trim();

            if(value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")){

                value =
                        "\""
                        + value.replace("\"", "\"\"")
                        + "\"";
            }

            line.append(value);

            if(i < data.length - 1){
                line.append(",");
            }
        }

        return line.toString();
    }
}
