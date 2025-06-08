package com.example.game.Client.UI;

import com.example.game.DataStructures.ConcurrentQueue;

import javax.swing.*;
import java.awt.*;

public class ActorPanel extends JPanel {
    private ConcurrentQueue.Coordinates actorPosition = new ConcurrentQueue.Coordinates(0, 0);
    private final int ACTOR_SIZE = 20;
    private final int SCALE = 10; // for visible movement (1:10 px)

    public void updatePosition(ConcurrentQueue.Coordinates newPosition) {
        this.actorPosition = newPosition;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g; // actor circle object
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);


        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int screenX = centerX + (actorPosition.x() * SCALE) - ACTOR_SIZE / 2;
        int screenY = centerY + (actorPosition.y() * SCALE) - ACTOR_SIZE / 2;

        g2d.setColor(Color.BLUE);
        g2d.fillOval(screenX, screenY, ACTOR_SIZE, ACTOR_SIZE);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawOval(screenX, screenY, ACTOR_SIZE, ACTOR_SIZE);

        // coordinate axes
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, centerY, getWidth(), centerY);
        g2d.drawLine(centerX, 0, centerX, getHeight());

        // text hints
        g2d.setColor(Color.BLACK);
        g2d.drawString("(" + actorPosition.x() + ", " + actorPosition.y() + ")",
                screenX + ACTOR_SIZE + 5, screenY + ACTOR_SIZE / 2);
    }
}
