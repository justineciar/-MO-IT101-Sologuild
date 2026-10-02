package com.mycompany.motorphemployeeapp;

import java.awt.*;
import javax.swing.*;

public class LoginSuccessDialog extends JDialog {

    private boolean showCheck = false;

    public LoginSuccessDialog(JFrame parent) {
        super(parent, true);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(220,220);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel() {

            private float angle = 0;

            {
                Timer timer = new Timer(15, e -> {

                            if(!showCheck){
                                angle += 8;
                                repaint();
                            }

                        });

                timer.start();

                new Timer(600, e -> {

                            showCheck = true;
                            repaint();

                            ((Timer)e.getSource()).stop();

                            new Timer(500, x -> {

                                dispose();
                                ((Timer)x.getSource()).stop();

                            }).start();

                        }).start();
            }

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if(!showCheck){

                    g2.setStroke(new BasicStroke(6));

                    g2.setColor(new Color(78,149,242));

                    int size = 60;
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;

                    g2.setStroke(new BasicStroke(
                            5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));

                    g2.setColor(new Color(78,149,242));

                    g2.drawArc(x, y, size, size, (int)angle, 280);

                }else{

                    g2.setColor(new Color(46,204,113));

                    int circle = 60;
                    int cx = (getWidth() - circle) / 2;
                    int cy = (getHeight() - circle) / 2;

                    g2.setColor(new Color(46,204,113));
                    g2.fillOval(cx, cy, circle, circle);

                    g2.setColor(Color.WHITE);

                    g2.setStroke(new BasicStroke(8,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));

                    g2.setStroke(new BasicStroke(
                            5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));

                    g2.drawLine(cx + 16, cy + 31, cx + 27, cy + 43);
                    g2.drawLine(cx + 27, cy + 43, cx + 45, cy + 18);
                }
            }
        };

        panel.setOpaque(false);

        add(panel);
    }
}
