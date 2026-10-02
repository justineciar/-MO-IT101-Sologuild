package com.mycompany.motorphemployeeapp;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class BulkAddEmployeeDialog extends JDialog {

    private JTextField fileField;

    private File selectedFile;

    private HREmployeeListPanel parentPanel;

    public BulkAddEmployeeDialog(
            Window parent,
            HREmployeeListPanel parentPanel
            ) {

        super(
                parent,
                "Bulk Add Employees",
                ModalityType.APPLICATION_MODAL
                );

        this.parentPanel = parentPanel;

        setSize(650, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel =
                new JLabel("Bulk Add Employees");

        titleLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                24
                )
                );

        titleLabel.setBounds(
                25,
                20,
                300,
                35
                );

        add(titleLabel);

        JLabel fileLabel =
                new JLabel("CSV File:");

        fileLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                14
                )
                );

        fileLabel.setBounds(
                25,
                90,
                100,
                35
                );

        add(fileLabel);

        fileField =
                new JTextField();

        fileField.setEditable(false);

        fileField.setBounds(
                110,
                90,
                360,
                35
                );

        add(fileField);

        JButton browseButton =
                createRoundedButton(
                "Browse",
                new Color(33,150,243),
                Color.WHITE
                );

        browseButton.setBounds(
                490,
                90,
                110,
                35
                );

        add(browseButton);

        JButton importButton =
                createRoundedButton(
                "Import",
                new Color(46,125,50),
                Color.WHITE
                );

        importButton.setBounds(
                210,
                180,
                100,
                40
                );

        add(importButton);

        JButton cancelButton =
                createRoundedButton(
                "Cancel",
                new Color(220,60,60),
                Color.WHITE
                );

        cancelButton.setBounds(
                330,
                180,
                100,
                40
                );

        add(cancelButton);

        browseButton.addActionListener(
                e -> chooseFile()
                );

        importButton.addActionListener(
                e -> importEmployees()
                );

        cancelButton.addActionListener(
                e -> dispose()
                );

        setVisible(true);
    }

    private void chooseFile() {

        JFileChooser chooser =
                new JFileChooser();

        int result =
                chooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){

            selectedFile =
                    chooser.getSelectedFile();

            fileField.setText(
                    selectedFile.getAbsolutePath()
                    );
        }
    }

    private void importEmployees() {

        if(selectedFile == null){

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a CSV file."
                    );

            return;
        }

        try {

            EmployeeDataManager manager =
                    new EmployeeDataManager();

            String resultMessage =
                    manager.importEmployeesFromCSV(
                    selectedFile
                    );

            showImportResult(resultMessage);

            parentPanel.refreshTable();

            dispose();

        } catch(Exception ex){

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Import Error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }
    }

    private void showImportResult(
            String resultMessage
            ) {

        JDialog dialog =
                new JDialog(
                this,
                "Bulk Import Result",
                true
                );

        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel =
                new JPanel();

        mainPanel.setBackground(Color.WHITE);

        mainPanel.setLayout(
                new BorderLayout(
                10,
                10
                )
                );

        JLabel title =
                new JLabel(
                "✓ Bulk Import Complete"
                );

        title.setForeground(
                new Color(46,125,50)
                );

        title.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                28
                )
                );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                20,
                20,
                10,
                20
                )
                );

        mainPanel.add(
                title,
                BorderLayout.NORTH
                );

        JPanel summaryPanel =
                new JPanel();

        summaryPanel.setBackground(
                new Color(232,245,233)
                );

        summaryPanel.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                new Color(220,230,201)
                ),
                BorderFactory.createEmptyBorder(
                20,
                20,
                20,
                20
                )
                )
                );

        JLabel summaryLabel =
                new JLabel(
                "Action Completed",
                SwingConstants.CENTER
                );

        summaryLabel.setForeground(
                new Color(46,125,50)
                );

        summaryLabel.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                18
                )
                );

        summaryPanel.add(
                summaryLabel
                );

        mainPanel.add(
                summaryPanel,
                BorderLayout.NORTH
                );

        JTextArea resultArea = new JTextArea(resultMessage);

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        resultArea.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                15
                )
                );

        JScrollPane resultScroll =
                new JScrollPane(resultArea);

        resultScroll.setBorder(null);

        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(null);

        resultArea.setFont(
                new Font(
                "Segoe UI",
                Font.PLAIN,
                16
                )
                );

        JPanel centerPanel =
                new JPanel(
                new BorderLayout()
                );

        centerPanel.setBackground(
                Color.WHITE
                );

        centerPanel.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                new Color(220,220,220),
                1,
                true
                ),
                BorderFactory.createEmptyBorder(
                25,
                25,
                25,
                25
                )
                )
                );

        centerPanel.add(
                resultArea,
                BorderLayout.NORTH
                );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
                );

        JButton okButton =
                createRoundedButton(
                "OK",
                new Color(33,150,243),
                Color.WHITE
                );

        okButton.addActionListener(
                e -> dialog.dispose()
                );

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(
                Color.WHITE
                );

        okButton.setPreferredSize(
                new Dimension(
                150,
                45
                )
                );

        buttonPanel.add(
                okButton
                );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
                );

        dialog.add(mainPanel);

        dialog.setVisible(true);
    }

    private JButton createRoundedButton(
            String text,
            Color background,
            Color foreground
            ) {

        JButton button =
                new JButton(text);

        button.setBackground(background);
        button.setForeground(foreground);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                13
                )
                );

        button.setCursor(
                new Cursor(
                Cursor.HAND_CURSOR
                )
                );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                8,
                15,
                8,
                15
                )
                );

        return button;
    }
}
