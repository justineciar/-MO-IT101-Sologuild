package com.mycompany.motorphemployeeapp;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class SupervisorRequestsPanel extends JPanel {

    private static final String LEAVE_REQUEST_FILE =
            "src/main/resources/LeaveRequests.csv";

    private static final String OVERTIME_REQUEST_FILE =
            "src/main/resources/OvertimeRequests.csv";

    private JTable requestTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;
    private JComboBox<String> filterBox;

    private JTabbedPane requestTabs;

    private JButton approveButton;
    private JButton rejectButton;

    private Employee reviewer;

    public SupervisorRequestsPanel(Employee reviewer) {

        this.reviewer = reviewer;

        setLayout(null);
        setBackground(new Color(227,234,231));

        initializeHeader();
        initializeSearch();
        initializeTabs();
        initializeTable();
        initializeBottomButtons();

        registerEvents();

        loadLeaveRequests();

    }

    private void initializeHeader() {

        JLabel breadcrumb =
                new JLabel("Home > Requests");

        breadcrumb.setBounds(40,40,250,20);

        breadcrumb.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                12
                )
                );

        breadcrumb.setForeground(
                new Color(120,120,120)
                );

        add(breadcrumb);

        JLabel title =
                new JLabel("Requests");

        title.setBounds(
                40,
                85,
                300,
                40
                );

        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                32
                )
                );

        add(title);

    }

    private void initializeSearch() {

        JLabel searchLabel =
                new JLabel("Search");

        searchLabel.setBounds(
                40,
                145,
                60,
                30
                );

        add(searchLabel);

        searchField =
                new JTextField();

        searchField.setBounds(
                100,
                145,
                220,
                34
                );

        add(searchField);

        JLabel filterLabel =
                new JLabel("Filter");

        filterLabel.setBounds(
                340,
                145,
                45,
                30
                );

        add(filterLabel);

        filterBox =
                new JComboBox<>(
                new String[]{
                    "All",
                    "Pending",
                    "Approved",
                    "Rejected"
                }
                );

        filterBox.setBounds(
                390,
                145,
                130,
                34
                );

        add(filterBox);

    }

    private void initializeTabs() {

        requestTabs = new JTabbedPane();

        requestTabs.setBounds(
                40,
                190,
                700,
                44
                );

        requestTabs.addTab(
                "Leave Requests",
                new JPanel()
                );

        requestTabs.addTab(
                "Overtime Requests",
                new JPanel()
                );

        requestTabs.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                13
                )
                );

        requestTabs.setUI(new BasicTabbedPaneUI() {

                    @Override
            protected int calculateTabHeight(
                    int tabPlacement,
                    int tabIndex,
                    int fontHeight
                    ) {
                        return 36;
                    }

                });

        requestTabs.setBackground(Color.WHITE);

        add(requestTabs);

    }

    private void initializeTable() {

        tableModel =
                new DefaultTableModel(
                new String[]{
                    "Request ID",
                    "Employee",
                    "Date",
                    "Start Time",
                    "End Time",
                    "Hours",
                    "Status"
                },
                0
                ){

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
                    ){
                return false;
            }

        };

        requestTable = new JTable(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        requestTable.setDefaultRenderer(
                Object.class,
                centerRenderer
                );

        sorter =
                new TableRowSorter<>(tableModel);

        requestTable.setRowSorter(sorter);

        requestTable.getColumnModel().getColumn(0).setMinWidth(0);
        requestTable.getColumnModel().getColumn(0).setMaxWidth(0);
        requestTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        requestTable.setRowHeight(36);

        requestTable.getTableHeader().setPreferredSize(
                new Dimension(0, 36)
                );

        requestTable.getTableHeader().setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                12
                )
                );

        requestTable.setShowGrid(false);

        requestTable.setIntercellSpacing(
                new Dimension(0,0)
                );

        requestTable.setFillsViewportHeight(true);

        requestTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer) requestTable
                .getTableHeader()
                .getDefaultRenderer();

        headerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
                );

        JScrollPane scrollPane =
                new JScrollPane(requestTable);

        scrollPane.setBounds(
                40,
                225,
                700,
                325
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
                );

        add(scrollPane);

    }

    private void initializeBottomButtons() {

        approveButton =
                new RoundedButton(
                "Approve",
                new Color(46, 125, 50),
                Color.WHITE
                );

        approveButton.setBounds(
                235,
                585,
                135,
                42
                );

        add(approveButton);

        rejectButton =
                new RoundedButton(
                "Reject",
                new Color(198, 40, 40),
                Color.WHITE
                );

        rejectButton.setBounds(
                410,
                585,
                135,
                42
                );

        add(rejectButton);

    }

    private void loadLeaveRequests() {

        tableModel.setColumnIdentifiers(
                new String[]{
                    "Employee",
                    "Leave Type",
                    "Start Date",
                    "End Date",
                    "Status",
                    "Submitted"
                });

        tableModel.setRowCount(0);

        java.util.List<String[]> requests =
                getPendingLeaveRequests();

        for(String[] request : requests){

            tableModel.addRow(
                    new Object[]{
                        request[2],
                        getLeaveField(request,3,4),
                        getLeaveField(request,4,5),
                        getLeaveField(request,5,6),
                        getLeaveField(request,9,10),
                        formatDateOnly(getLeaveField(request,8,9))
                    }
                    );

        }
        requestTable.getColumnModel().getColumn(0).setPreferredWidth(160); // Employee
                requestTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Leave Type
                requestTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Start Date
                requestTable.getColumnModel().getColumn(3).setPreferredWidth(100); // End Date
                requestTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
                requestTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Submitted
            }

    private java.util.List<String[]> getPendingLeaveRequests(){

        java.util.List<String[]> requests =
                new java.util.ArrayList<>();

        java.io.File file =
                new java.io.File(LEAVE_REQUEST_FILE);

        if(!file.exists()){

            return requests;

        }

        try(java.io.BufferedReader reader =
                new java.io.BufferedReader(
                new java.io.FileReader(file))){

            String line;

            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] data =
                        parseCSVLine(line);

                if(data.length >= 12
                        && getLeaveStatus(data).equalsIgnoreCase("Pending")
                        && canReviewerSeeRequest(data)){

                    requests.add(data);

                }

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return requests;

    }

    private String getLeaveField(
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

    private String getLeaveStatus(
            String[] data
            ) {

        return getLeaveField(
                data,
                9,
                10
                ).trim();

    }

    private String[] parseCSVLine(String line) {

        return line.split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                -1
                );

    }

    private String getReviewerName() {

        if(reviewer == null){

            return "HR Staff";

        }

        String reviewerName =
                (reviewer.getFirstName()
                + " "
                + reviewer.getLastName()).trim();

        if(reviewerName.isEmpty()){

            return "HR Staff";

        }

        return reviewerName;

    }

    private String getLastNameFirstName(
            Employee employee
            ) {

        if(employee == null){

            return "";

        }

        return (
                employee.getLastName()
                + ", "
                + employee.getFirstName()
                ).trim();

    }

    private String normalizeName(
            String name
            ) {

        if(name == null){

            return "";

        }

        return name.replace("\"","")
                .replace(","," ")
                .replaceAll("\\s+"," ")
                .trim()
                .toLowerCase();

    }

    private boolean isHRPersonnel() {

        if (reviewer == null || reviewer.getPosition() == null) {
            return false;
        }

        String position = reviewer.getPosition().trim().toLowerCase();

        return position.equals("hr manager")
                || position.equals("hr team leader")
                || position.equals("hr rank and file");
    }

    private boolean isOwnRequest(String[] data) {

        if (reviewer == null
                || reviewer.getEmployeeNumber() == null
                || data.length <= 1) {

            return false;
        }

        return reviewer.getEmployeeNumber()
                .trim()
                .equals(data[1].trim());
    }

    private boolean canReviewerSeeRequest(String[] data) {

        if (isHRPersonnel()) {
            return !isOwnRequest(data);
        }

        if (data.length < 13) {
            return true;
        }

        String requestSupervisor = data[3];

        if (isSupervisorMatch(requestSupervisor, reviewer)) {
            return true;
        }

        return isFallbackReviewer()
                && !doesSupervisorExist(requestSupervisor);
    }

    private boolean isSupervisorMatch(
            String requestSupervisor,
            Employee reviewer
            ) {

        String normalizedRequestSupervisor =
                normalizeName(requestSupervisor);

        return normalizedRequestSupervisor.equals(
                normalizeName(getReviewerName())
                ) || normalizedRequestSupervisor.equals(
                normalizeName(getLastNameFirstName(reviewer))
                ) || hasSameNameParts(
                normalizedRequestSupervisor,
                normalizeName(getReviewerName())
                ) || hasSameNameParts(
                normalizedRequestSupervisor,
                normalizeName(getLastNameFirstName(reviewer))
                );

    }

    private boolean isFallbackReviewer() {

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

    private boolean doesSupervisorExist(
            String requestSupervisor
            ) {

        try(java.io.BufferedReader reader =
                new java.io.BufferedReader(
                new java.io.FileReader(
                "src/main/resources/MotorPH_Employee Data - Employee Details.csv"
                )
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

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    private boolean hasSameNameParts(
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

        java.util.Arrays.sort(firstParts);
        java.util.Arrays.sort(secondParts);

        if(java.util.Arrays.equals(
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

    private boolean hasEnoughSharedNameParts(
            String[] firstParts,
            String[] secondParts
            ) {

        int sharedParts = 0;

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

    private void loadOvertimeRequests() {

        tableModel.setColumnIdentifiers(
                new String[]{
                    "Employee",
                    "Date",
                    "Start Time",
                    "End Time",
                    "Hours",
                    "Status"
                }
                );

        tableModel.setRowCount(0);

        java.util.List<String[]> requests =
                getPendingOvertimeRequests();

        for(String[] request : requests){

            tableModel.addRow(
                    new Object[]{
                        request[2],
                        request[4],
                        request[5],
                        request[6],
                        request[7],
                        request[10]
                    }
                    );

        }

        requestTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Employee
                requestTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
                requestTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Start Time
                requestTable.getColumnModel().getColumn(3).setPreferredWidth(100); // End Time
                requestTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Hours
                requestTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status

            }

    private java.util.List<String[]> getPendingOvertimeRequests(){

        java.util.List<String[]> requests =
                new java.util.ArrayList<>();

        File file =
                new File(OVERTIME_REQUEST_FILE);

        if(!file.exists()){

            return requests;

        }

        try(java.io.BufferedReader reader =
                new java.io.BufferedReader(
                new java.io.FileReader(file))){

            String line;

            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] data =
                        parseCSVLine(line);

                if(data.length >= 13
                        && data[10].trim().equalsIgnoreCase("Pending")
                        && canReviewerSeeRequest(data)){

                    requests.add(data);

                }

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return requests;

    }

    private void registerEvents() {

        requestTabs.addChangeListener(e -> {

                    if(requestTabs.getSelectedIndex() == 0){

                        loadLeaveRequests();

                    }else{

                        loadOvertimeRequests();

                    }

                });

        approveButton.addActionListener(e -> {

                    if(requestTabs.getSelectedIndex() == 0){

                        reviewSelectedLeaveRequest(true);

                    }else{

                        reviewSelectedOvertimeRequest(true);

                    }

                });

        rejectButton.addActionListener(e -> {

                    if(requestTabs.getSelectedIndex() == 0){

                        reviewSelectedLeaveRequest(false);

                    }else{

                        reviewSelectedOvertimeRequest(false);

                    }

                });

        searchField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {

                        filterTable();

                    }

                    @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {

                        filterTable();

                    }

                    @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {

                        filterTable();

                    }

                });

        filterBox.addActionListener(e -> filterTable());

    }

    private void filterTable() {

        RowFilter<DefaultTableModel, Object> filter = null;

        try {

            java.util.List<RowFilter<Object,Object>> filters =
                    new java.util.ArrayList<>();

            String search =
                    searchField.getText().trim();

            if(!search.isEmpty()){

                filters.add(
                        RowFilter.regexFilter(
                        "(?i)"
                        + java.util.regex.Pattern.quote(search)
                        )
                        );

            }

            String status =
                    filterBox.getSelectedItem().toString();

            if(!status.equals("All")){

                int statusColumn =
                        requestTabs.getSelectedIndex() == 0
                        ? 4
                        : 5;

                filters.add(
                        RowFilter.regexFilter(
                        "^" + status + "$",
                        statusColumn
                        )
                        );

            }

            filter =
                    RowFilter.andFilter(filters);

        }catch(Exception e){

        }

        sorter.setRowFilter(filter);

    }

    private void reviewSelectedLeaveRequest(boolean approve) {

        int row = requestTable.getSelectedRow();

        if(row == -1){

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please select a leave request."
                    );

            return;

        }

        row = requestTable.convertRowIndexToModel(row);

        String employeeName =
                tableModel.getValueAt(row,0).toString();

        String leaveType =
                tableModel.getValueAt(row,1).toString();

        String startDate =
                tableModel.getValueAt(row,2).toString();

        String endDate =
                tableModel.getValueAt(row,3).toString();

        boolean success =
                reviewLeaveRequest(
                employeeName,
                leaveType,
                startDate,
                endDate,
                approve
                );

        if(success){

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    approve
                    ? "Leave request approved."
                    : "Leave request rejected."
                    );

            loadLeaveRequests();

        }else{

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Unable to update request."
                    );

        }

    }

    private boolean reviewLeaveRequest(
            String employeeName,
            String leaveType,
            String startDate,
            String endDate,
            boolean approve
            ){

        try{

            File file =
                    new File(LEAVE_REQUEST_FILE);

            if(!file.exists()){

                return false;

            }

            java.util.List<String> lines =
                    java.nio.file.Files.readAllLines(
                    file.toPath()
                    );

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        parseCSVLine(lines.get(i));

                if(data.length < 12){

                    continue;

                }

                if(!getLeaveStatus(data)
                        .equalsIgnoreCase("Pending")){

                    continue;

                }

                if(data[2].trim().equals(employeeName)
                        && getLeaveField(data,3,4).trim().equals(leaveType)
                        && getLeaveField(data,4,5).trim().equals(startDate)
                        && getLeaveField(data,5,6).trim().equals(endDate)){

                    data[getStatusIndex(data)] =
                            approve
                            ? "Approved"
                            : "Rejected";

                    data[getReviewedByIndex(data)] =
                            getReviewerName();

                    data[getReviewDateIndex(data)] =
                            java.time.LocalDateTime.now().toString();

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    java.nio.file.Files.write(
                            file.toPath(),
                            lines
                            );

                    return true;

                }

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    private void reviewSelectedOvertimeRequest(boolean approve) {

        int row = requestTable.getSelectedRow();

        if(row == -1){

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please select an overtime request."
                    );

            return;

        }

        row = requestTable.convertRowIndexToModel(row);

        String requestId = tableModel.getValueAt(row,0).toString();

        boolean success =
                reviewOvertimeRequest(
                requestId,
                approve
                );

        if(success){

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    approve
                    ? "Overtime request approved."
                    : "Overtime request rejected."
                    );

            loadOvertimeRequests();

        }else{

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Unable to update request."
                    );

        }

    }

    private boolean reviewOvertimeRequest(
            String requestId,
            boolean approve
            ){

        try{

            File file =
                    new File(OVERTIME_REQUEST_FILE);

            if(!file.exists()){

                return false;

            }

            java.util.List<String> lines =
                    java.nio.file.Files.readAllLines(
                    file.toPath()
                    );

            for(int i = 1; i < lines.size(); i++){

                String[] data =
                        parseCSVLine(lines.get(i));

                if(data.length < 13){

                    continue;

                }

                if(!data[10].trim().equalsIgnoreCase("Approved")){

                    continue;

                }

                if(data[0].trim().equals(requestId)){

                    data[10] =
                            approve
                            ? "Reviewed"
                            : "Exit";

                    data[11] =
                            getReviewerName();

                    data[12] =
                            java.time.LocalDateTime.now().toString();

                    lines.set(
                            i,
                            formatCSVLine(data)
                            );

                    java.nio.file.Files.write(
                            file.toPath(),
                            lines
                            );

                    return true;

                }

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    private int getStatusIndex(String[] data){

        return data.length >= 13 ? 10 : 9;

    }

    private int getReviewedByIndex(String[] data){

        return data.length >= 13 ? 11 : 10;

    }

    private int getReviewDateIndex(String[] data){

        return data.length >= 13 ? 12 : 11;

    }

    private String formatCSVLine(String[] data){

        StringBuilder line =
                new StringBuilder();

        for(int i = 0; i < data.length; i++){

            String value =
                    data[i] == null
                    ? ""
                    : data[i].trim();

            if(value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")){

                value =
                        "\""
                        + value.replace("\"","\"\"")
                        + "\"";

            }

            line.append(value);

            if(i < data.length - 1){

                line.append(",");

            }

        }

        return line.toString();

    }

    private String formatDateOnly(String dateTime) {

        if (dateTime == null || dateTime.isBlank()) {
            return "";
        }

        int tIndex = dateTime.indexOf('T');

        if (tIndex > 0) {
            return dateTime.substring(0, tIndex);
        }

        return dateTime;
    }
}
