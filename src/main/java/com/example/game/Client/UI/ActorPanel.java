package com.example.game.Client.UI;

import com.example.game.DataStructures.Actor;
import com.example.game.DataStructures.Coordinates;
import com.example.game.DataStructures.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ActorPanel extends JPanel {
    private GameState gameState;
    private final int ACTOR_SIZE = 20;
    private final int SCALE = 10;

    public ActorPanel() {
        this.gameState = new GameState(new ArrayList<>());
    }

    public void updateAllActors(GameState gameState) {
        this.gameState = gameState;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (Actor actor : gameState.getActorsSnapshot()) {
            Coordinates pos = actor.getCoordinates();
            int x = centerX + pos.x() * SCALE - ACTOR_SIZE / 2;
            int y = centerY + pos.y() * SCALE - ACTOR_SIZE / 2;

            g2d.setColor(Color.BLUE);
            g2d.fillOval(x, y, ACTOR_SIZE, ACTOR_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawString(actor.getClientId().substring(0, 4), x + ACTOR_SIZE / 2, y - 5);  // temp actor id
        }

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, centerY, getWidth(), centerY);
        g2d.drawLine(centerX, 0, centerX, getHeight());
    }

    public GameState getGameState() {
        return gameState;
    }
}