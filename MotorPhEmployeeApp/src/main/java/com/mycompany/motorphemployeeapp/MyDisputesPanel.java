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

public class MyDisputesPanel extends JPanel {

    private static final String PAYROLL_DISPUTE_FILE =
            "src/main/resources/PayrollDisputeRequests.csv";

    private JTable disputeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterBox;
    private Employee employee;
    private TableRowSorter<DefaultTableModel> sorter;

    public MyDisputesPanel(Employee employee) {

        this.employee = employee;

        setLayout(null);
        setBackground(new Color(227,234,231));

        // =========================
        // BREADCRUMB
        // =========================

        JLabel breadcrumb =
                new JLabel("Home > My Disputes");

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
                new JLabel("My Disputes");

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

                    "Resolved"

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

            "Cutoff Period",

                    "Date",

                    "Category",

                    "Status",

                    "Reviewed By",

                    "Remarks"

                };

        tableModel =
                new DefaultTableModel(
                columns,
                0
                ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
                    ) {

                return false;

            }

        };

        disputeTable = new JTable(tableModel);

        disputeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        sorter = new TableRowSorter<>(tableModel);

        sorter.setSortsOnUpdates(true);

        disputeTable.setRowSorter(sorter);

        disputeTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        disputeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        disputeTable.getColumnModel().getColumn(2).setPreferredWidth(125);
        disputeTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        disputeTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        disputeTable.getColumnModel().getColumn(5).setPreferredWidth(140);

        disputeTable.setRowHeight(32);

        disputeTable.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                14
                )
                );

        disputeTable.setBackground(
                new Color(
                245,
                245,
                245
                )
                );

        disputeTable.setSelectionBackground(
                new Color(
                210,
                210,
                210
                )
                );

        disputeTable.setShowGrid(false);

        disputeTable.setIntercellSpacing(
                new Dimension(
                0,
                0
                )
                );

        disputeTable.setFillsViewportHeight(true);

        disputeTable.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
                    ) {

                        JLabel label =
                        (JLabel) super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                        );

                        label.setHorizontalAlignment(
                        SwingConstants.CENTER
                        );

                        if (!isSelected) {

                            if (row % 2 == 0) {

                                label.setBackground(
                                new Color(245,245,245)
                                );

                            } else {

                                label.setBackground(
                                new Color(220,220,220)
                                );

                            }

                            label.setForeground(Color.BLACK);

                            if (column == 4) {

                                String status = value.toString();

                                switch (status) {

                                    case "Approved":

                                        label.setForeground(
                                        new Color(0,128,0)
                                        );
                                        break;

                                    case "Pending":

                                        label.setForeground(
                                        new Color(255,140,0)
                                        );
                                        break;

                                    case "Rejected":

                                        label.setForeground(
                                        Color.RED
                                        );
                                        break;

                                    case "Resolved":

                                        label.setForeground(
                                        new Color(0,102,204)
                                        );
                                        break;

                                }

                            }

                        }

                        return label;

                    }

                }

                );

        disputeTable.getTableHeader().setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        disputeTable.getTableHeader().setBackground(
                new Color(
                210,
                210,
                210
                )
                );

        disputeTable.getTableHeader().setForeground(
                Color.BLACK
                );

        disputeTable.getTableHeader().setOpaque(true);

        disputeTable.getTableHeader().setReorderingAllowed(false);

        disputeTable.getTableHeader().setResizingAllowed(false);

        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer)
                disputeTable.getTableHeader().getDefaultRenderer();

        headerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
                );

        JScrollPane scrollPane =
                new JScrollPane(
                disputeTable
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

        loadDisputes();

        initializeSearch();

        initializeFilter();

    }

    /**
     * Loads payroll dispute requests of the current employee.
     */
    private void loadDisputes() {

        tableModel.setRowCount(0);

        File file =
                new File(PAYROLL_DISPUTE_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(
                new FileReader(file))) {

            // Skip header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split(
                        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",
                        -1
                        );

                if (data.length < 15) {
                    continue;
                }

                if (!data[1].trim().equals(employee.getEmployeeNumber())) {
                    continue;
                }

                tableModel.addRow(

                        new Object[]{

                            data[3].replace("\"", ""), // Cutoff Period

                            data[4], // Date

                            data[8], // Category

                            data[12], // Status

                            data[13], // Reviewed By

                            data[10] // Remarks

                        }

                        );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void initializeSearch() {

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

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

                }
                );

    }

    private void search() {

        applyFilter();

    }

    private void initializeFilter() {

        filterBox.addActionListener(e -> applyFilter());

    }

    private void applyFilter() {

        String searchText =
                searchField.getText().trim();

        String filter =
                filterBox.getSelectedItem().toString();

        RowFilter<DefaultTableModel, Object> statusFilter = null;

        switch (filter) {

            case "Pending":

                statusFilter =
                        RowFilter.regexFilter(
                        "^Pending$",
                        4
                        );
                break;

            case "Approved":

                statusFilter =
                        RowFilter.regexFilter(
                        "^Approved$",
                        4
                        );
                break;

            case "Rejected":

                statusFilter =
                        RowFilter.regexFilter(
                        "^Rejected$",
                        4
                        );
                break;

            case "Resolved":

                statusFilter =
                        RowFilter.regexFilter(
                        "^Resolved$",
                        4
                        );
                break;

            default:

                statusFilter = null;

        }

        RowFilter<DefaultTableModel, Object> searchFilter = null;

        if (!searchText.isEmpty()) {

            searchFilter =
                    RowFilter.regexFilter(
                    "(?i)" + searchText
                    );

        }

        if (statusFilter != null && searchFilter != null) {

            sorter.setRowFilter(

                    RowFilter.andFilter(

                    java.util.Arrays.asList(

                    statusFilter,
                    searchFilter

                    )

                    )

                    );

        } else if (statusFilter != null) {

            sorter.setRowFilter(statusFilter);

        } else if (searchFilter != null) {

            sorter.setRowFilter(searchFilter);

        } else {

            sorter.setRowFilter(null);

        }

    }

}
