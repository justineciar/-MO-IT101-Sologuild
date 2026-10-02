package com.mycompany.motorphemployeeapp;

public class OvertimeAccess {

    public static boolean canReviewOvertime(
            Employee employee
            ) {

        if(employee == null
                || employee.getPosition() == null){

            return false;
        }

        String position =
                employee.getPosition()
                .toLowerCase();

        return position.contains("manager")
                || position.contains("head")
                || position.contains("team leader")
                || position.contains("chief")
                || position.contains("officer");
    }
}
