/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Alfie
 */
package alfie.view;

import javax.swing.*;
import java.awt.*;

public class LogsPanel {
    private final JPanel panel;

    public LogsPanel(JList<String> attendanceLogList) {
        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 0));

        JLabel attendanceLogLabel = new JLabel("Attendance Logs");
        JScrollPane attendanceScrollPane = new JScrollPane(attendanceLogList);

        panel.add(attendanceLogLabel, BorderLayout.NORTH);
        panel.add(attendanceScrollPane, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
