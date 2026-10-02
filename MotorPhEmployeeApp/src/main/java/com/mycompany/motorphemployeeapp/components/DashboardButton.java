package com.mycompany.motorphemployeeapp.components;

import java.awt.*;
import javax.swing.*;

public class DashboardButton extends JButton {

    private Color backgroundColor = new Color(78, 149, 242);
    private Color hoverColor = new Color(66, 133, 230);

    private boolean hovered = false;

    public DashboardButton(String text) {
        super(text);

        setFont(new Font("Arial", Font.BOLD, 18));
        setForeground(Color.BLACK);

        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);

        setHorizontalAlignment(SwingConstants.LEFT);
        setMargin(new Insets(0, 20, 0, 0));

        addMouseListener(new java.awt.event.MouseAdapter() {

                    @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hovered ? hoverColor : backgroundColor);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                20,
                20);

        super.paintComponent(g);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                20,
                20);

        g2.dispose();
    }

    public void setButtonColor(Color color) {
        backgroundColor = color;
        repaint();
    }

    public void setHoverColor(Color color) {
        hoverColor = color;
    }
}
