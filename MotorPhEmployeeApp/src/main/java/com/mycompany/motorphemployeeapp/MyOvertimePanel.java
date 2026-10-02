package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class MyOvertimePanel extends JPanel {

    private static final String OVERTIME_REQUEST_FILE =
            "src/main/resources/OvertimeRequests.csv";
    private JTable overtimeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterBox;
    private Employee employee;
    private TableRowSorter<DefaultTableModel> sorter;

    public MyOvertimePanel(Employee employee) {

        this.employee = employee;

        setLayout(null);
        setBackground(new Color(227,234,231));

        // =========================
        // BREADCRUMB
        // =========================

        JLabel breadcrumb =
                new JLabel("Home > My Overtime");

        breadcrumb.setBounds(
                40,
                40,
                250,
                20
                );

        breadcrumb.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                12
                )
                );

        breadcrumb.setForeground(
                new Color(
                120,
                120,
                120
                )
                );

        add(breadcrumb);

        // =========================
        // TITLE
        // =========================

        JLabel title =
                new JLabel("My Overtime");

        title.setBounds(
                40,
                85,
                350,
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

        // =========================
        // SEARCH LABEL
        // =========================

        JLabel searchLabel =
                new JLabel("Search");

        searchLabel.setBounds(
                40,
                145,
                60,
                30
                );

        searchLabel.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                15
                )
                );

        add(searchLabel);

        // =========================
        // SEARCH FIELD
        // =========================

        searchField =
                new JTextField();

        searchField.setBounds(
                100,
                145,
                240,
                34
                );

        searchField.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        add(searchField);

        // =========================
        // FILTER LABEL
        // =========================

        JLabel filterLabel =
                new JLabel("Filter");

        filterLabel.setBounds(
                370,
                145,
                45,
                30
                );

        filterLabel.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                15
                )
                );

        add(filterLabel);

        // =========================
        // FILTER COMBO
        // =========================

        filterBox =
                new JComboBox<>(
                new String[]{

                    "All",

                    "Pending",

                    "Approved",

                    "Rejected",

                    "January",

                    "February",

                    "March",

                    "April",

                    "May",

                    "June",

                    "July",

                    "August",

                    "September",

                    "October",

                    "November",

                    "December"

                }
                );

        filterBox.setBounds(
                420,
                145,
                180,
                34
                );

        filterBox.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        add(filterBox);

        // =========================
        // TABLE
        // =========================

        String[] columns = {

            "Date",

                    "Time In",

                    "Time Out",

                    "OT Hrs",

                    "Reason",

                    "Status",

                    "Approved By"

                };

        tableModel =
                new DefaultTableModel(
                columns,
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

        overtimeTable = new JTable(tableModel);
        overtimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sorter = new TableRowSorter<>(tableModel);
        sorter.setSortsOnUpdates(true);
        overtimeTable.setRowSorter(sorter);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        sorter.setComparator(0, new Comparator<String>() {

                    @Override
            public int compare(String d1, String d2) {

                        try {

                            Date date1 = sdf.parse(d1);
                            Date date2 = sdf.parse(d2);

                            return date2.compareTo(date1); // newest first

                        } catch (Exception e) {

                            return d2.compareTo(d1);

                        }

                    }

                });

        overtimeTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        overtimeTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        overtimeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        overtimeTable.getColumnModel().getColumn(3).setPreferredWidth(95);
        overtimeTable.getColumnModel().getColumn(4).setPreferredWidth(300);
        overtimeTable.getColumnModel().getColumn(5).setPreferredWidth(110);
        overtimeTable.getColumnModel().getColumn(6).setPreferredWidth(130);

        overtimeTable.setRowHeight(32);

        overtimeTable.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        overtimeTable.setBackground(
                new Color(
                245,
                245,
                245
                )
                );

        overtimeTable.setSelectionBackground(
                new Color(
                210,
                210,
                210
                )
                );

        overtimeTable.setShowGrid(false);

        overtimeTable.setIntercellSpacing(
                new Dimension(
                0,
                0
                )
                );

        overtimeTable.setFillsViewportHeight(true);

        overtimeTable.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                        JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column);

                        label.setHorizontalAlignment(SwingConstants.CENTER);

                        if (!isSelected) {

                            if (row % 2 == 0) {
                                label.setBackground(new Color(245,245,245));
                            } else {
                                label.setBackground(new Color(220,220,220));
                            }

                            label.setForeground(Color.BLACK);

                            if (column == 5) {

                                String status = value.toString();

                                switch (status) {

                                    case "Approved":
                                        label.setForeground(new Color(0,128,0));
                                        break;

                                    case "Pending":
                                        label.setForeground(new Color(255,140,0));
                                        break;

                                    case "Rejected":
                                        label.setForeground(Color.RED);
                                        break;

                                }

                            }

                        }

                        return label;

                    }

                }
                );

        overtimeTable.getTableHeader().setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        overtimeTable.getTableHeader().setBackground(
                new Color(
                210,
                210,
                210
                )
                );

        overtimeTable.getTableHeader().setForeground(Color.BLACK);

        overtimeTable.getTableHeader().setOpaque(true);

        overtimeTable.getTableHeader().setReorderingAllowed(false);

        overtimeTable.getTableHeader().setResizingAllowed(false);

        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer)
                overtimeTable.getTableHeader().getDefaultRenderer();

        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane =
                new JScrollPane(
                overtimeTable
                );

        scrollPane.setBounds(
                40,
                200,
                690,
                400
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
                );

        add(scrollPane);

        loadOvertimeRequests();

        initializeSearch();
        initializeFilter();
    }

    private void loadOvertimeRequests() {

        tableModel.setRowCount(0);

        File file =
                new File(OVERTIME_REQUEST_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(
                new FileReader(file))) {

            reader.readLine(); // Skip header

                    String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if (data.length < 11) {
                    continue;
                }

                if (!data[1].trim().equals(employee.getEmployeeNumber())) {
                    continue;
                }

                tableModel.addRow(

                        new Object[]{

                            data[4], // Date

                            data[5], // Time In

                            data[6], // Time Out

                            data[7], // OT Hours

                            data[8], // Reason

                            data[10], // Status

                            data[11] // Approved By

                        }

                        );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void initializeSearch() {

        searchField.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
            public void insertUpdate(DocumentEvent e) {
                        search();
                    }

                    @Override
            public void removeUpdate(DocumentEvent e) {
                        search();
                    }

                    @Override
            public void changedUpdate(DocumentEvent e) {
                        search();
                    }

                });

    }

    private void search() {

        applyFilter();

    }

    private void initializeFilter() {

        filterBox.addActionListener(e -> applyFilter());

    }

    private void applyFilter() {

        String searchText = searchField.getText().trim();
        String filter = filterBox.getSelectedItem().toString();

        RowFilter<DefaultTableModel, Object> filterRule = null;

        switch (filter) {

            case "Pending":
                filterRule = RowFilter.regexFilter("^Pending$", 5);
                break;

            case "Approved":
                filterRule = RowFilter.regexFilter("^Approved$", 5);
                break;

            case "Rejected":
                filterRule = RowFilter.regexFilter("^Rejected$", 5);
                break;

            case "January":
                filterRule = RowFilter.regexFilter("^01/.*", 0);
                break;

            case "February":
                filterRule = RowFilter.regexFilter("^02/.*", 0);
                break;

            case "March":
                filterRule = RowFilter.regexFilter("^03/.*", 0);
                break;

            case "April":
                filterRule = RowFilter.regexFilter("^04/.*", 0);
                break;

            case "May":
                filterRule = RowFilter.regexFilter("^05/.*", 0);
                break;

            case "June":
                filterRule = RowFilter.regexFilter("^06/.*", 0);
                break;

            case "July":
                filterRule = RowFilter.regexFilter("^07/.*", 0);
                break;

            case "August":
                filterRule = RowFilter.regexFilter("^08/.*", 0);
                break;

            case "September":
                filterRule = RowFilter.regexFilter("^09/.*", 0);
                break;

            case "October":
                filterRule = RowFilter.regexFilter("^10/.*", 0);
                break;

            case "November":
                filterRule = RowFilter.regexFilter("^11/.*", 0);
                break;

            case "December":
                filterRule = RowFilter.regexFilter("^12/.*", 0);
                break;

            default:
                filterRule = null;
                break;
        }

        RowFilter<DefaultTableModel, Object> searchRule = null;

        if (!searchText.isEmpty()) {

            searchRule = RowFilter.regexFilter("(?i)" + searchText);

        }

        if (filterRule != null && searchRule != null) {

            sorter.setRowFilter(
                    RowFilter.andFilter(
                    java.util.Arrays.asList(
                    filterRule,
                    searchRule
                    )
                    )
                    );

        } else if (filterRule != null) {

            sorter.setRowFilter(filterRule);

        } else if (searchRule != null) {

            sorter.setRowFilter(searchRule);

        } else {

            sorter.setRowFilter(null);

        }

    }
}
