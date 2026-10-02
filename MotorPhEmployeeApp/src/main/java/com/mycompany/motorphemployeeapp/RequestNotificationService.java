package com.mycompany.motorphemployeeapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestNotificationService {

    private static final String LEAVE_REQUEST_FILE =
            "src/main/resources/LeaveRequests.csv";

    private static final String OVERTIME_REQUEST_FILE =
            "src/main/resources/OvertimeRequests.csv";

    public static List<String> getDecisionMessages(
            Employee employee
            ) {

        List<String> messages =
                new ArrayList<>();

        if(employee == null
                || employee.getEmployeeNumber() == null){

            return messages;
        }

        loadLeaveDecisionMessages(
                employee.getEmployeeNumber(),
                messages
                );

        loadOvertimeDecisionMessages(
                employee.getEmployeeNumber(),
                messages
                );

        Collections.reverse(messages);

        if(messages.size() > 4){
            return new ArrayList<>(
                    messages.subList(
                    0,
                    4
                    )
                    );
        }

        return messages;
    }

    private static void loadLeaveDecisionMessages(
            String employeeId,
            List<String> messages
            ) {

        File file =
                new File(LEAVE_REQUEST_FILE);

        if(!file.exists()){
            return;
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

                if(data.length < 12
                        || !data[1].trim().equals(employeeId.trim())){

                    continue;
                }

                String status =
                        getLeaveField(
                        data,
                        9,
                        10
                        ).trim();

                if(!isDecisionStatus(status)){
                    continue;
                }

                String leaveType =
                        getLeaveField(
                        data,
                        3,
                        4
                        );

                String startDate =
                        getLeaveField(
                        data,
                        4,
                        5
                        );

                String reviewedBy =
                        getLeaveField(
                        data,
                        10,
                        11
                        );

                messages.add(
                        "Leave "
                        + status
                        + ": "
                        + leaveType
                        + " on "
                        + startDate
                        + getReviewerText(reviewedBy)
                        );
            }

        } catch(Exception e){

            e.printStackTrace();
        }
    }

    private static void loadOvertimeDecisionMessages(
            String employeeId,
            List<String> messages
            ) {

        File file =
                new File(OVERTIME_REQUEST_FILE);

        if(!file.exists()){
            return;
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

                if(data.length < 13
                        || !data[1].trim().equals(employeeId.trim())){

                    continue;
                }

                String status =
                        data[10].trim();

                if(!isDecisionStatus(status)){
                    continue;
                }

                messages.add(
                        "Overtime "
                        + status
                        + ": "
                        + data[7].trim()
                        + " hour(s) on "
                        + data[4].trim()
                        + getReviewerText(data[11])
                        );
            }

        } catch(Exception e){

            e.printStackTrace();
        }
    }

    private static boolean isDecisionStatus(
            String status
            ) {

        return status.equalsIgnoreCase("Approved")
                || status.equalsIgnoreCase("Rejected");
    }

    private static String getReviewerText(
            String reviewedBy
            ) {

        if(reviewedBy == null
                || reviewedBy.trim().isEmpty()){

            return "";
        }

        return " by "
                + reviewedBy.trim();
    }

    private static String getLeaveField(
            String[] data,
            int oldIndex,
            int newIndex
            ) {

        int index =
                data.length >= 13
                ? newIndex
                : oldIndex;

        if(index >= data.length){
            return "";
        }

        return data[index];
    }

    private static String[] parseCSVLine(
            String line
            ) {

        return line.split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                -1
                );
    }
}
