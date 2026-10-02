package com.mycompany.motorphemployeeapp;

import java.awt.*;
import javax.swing.*;

public class RoundedButton extends JButton {

    private final Color bgColor;

    public RoundedButton(
            String text,
            Color background,
            Color foreground
            ) {

        super(text);

        this.bgColor = background;

        setForeground(foreground);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        setFont(
                new Font(
                "Segoe UI",
                Font.BOLD,
                13
                )
                );
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
                );

        g2.setColor(bgColor);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                30,
                30
                );

        super.paintComponent(g2);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
    }
}
