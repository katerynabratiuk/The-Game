package org.client.UI;

import lombok.Getter;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ActorPanel extends JPanel {
    @Getter
    private GameState gameState;
    private final int SCALE = 5;

    public ActorPanel() {
        this.gameState = new GameState(new ArrayList<>());
    }

    public void updateGameState(GameState gameState) {
        // set new game state for all actors on the map (temporary method)
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
            Coordinates pos = convertToMapCoordinates(actor.getCoordinates().getX(), actor.getCoordinates().getY(), (int)actor.getRadius());

            g2d.setColor(actor.color());
            g2d.fillOval(pos.getX(), pos.getY(), (int) actor.getRadius()*2, (int)actor.getRadius()*2);
            g2d.setColor(Color.BLACK);
            // g2d.drawString(randomId, pos.getY() - 5);  // temp actor id
        }

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, centerY, getWidth(), centerY);
        g2d.drawLine(centerX, 0, centerX, getHeight());
    }

    public Coordinates convertToMapCoordinates(int x, int y, int actorSizeOffset) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int xOut = centerX + x * SCALE - actorSizeOffset;
        int yOut = centerY + y * SCALE - actorSizeOffset;
        return new Coordinates(xOut, yOut);
    }

    public Coordinates convertToGameCoordinates(int screenX, int screenY) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int gameX = (screenX - centerX) / SCALE;
        int gameY = (screenY - centerY) / SCALE;
        return new Coordinates(gameX, gameY);
    }

}