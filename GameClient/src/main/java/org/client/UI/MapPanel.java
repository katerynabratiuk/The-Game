package org.client.UI;

import lombok.Getter;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.GameState;
import org.lib.data_structures.payloads.actors.PlayerCharacter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapPanel extends JPanel {
    @Getter
    private GameState gameState;
    private final int SCALE = 5;

    public MapPanel() {
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

        for (Actor actor : gameState.getActorsSnapshot()) {
            Coordinates pos = convertToMapCoordinates(actor.getCoordinates().getX(), actor.getCoordinates().getY(), (int)actor.getRadius());

            g2d.setColor(actor.color());
            g2d.fillOval(pos.getX(), pos.getY(), (int) actor.getRadius()*2, (int)actor.getRadius()*2);
            g2d.setColor(Color.BLACK);

            if (actor instanceof PlayerCharacter pc) {
                int maxHP = pc.getMaxHp();
                int diameter = (int) actor.getRadius() * 2;
                int currentHP = pc.getHitPoints();
                int barHeight = 4;
                int healthWidth = (int) (((double) currentHP / maxHP) * diameter);

                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(pos.getX(), pos.getY() - barHeight - 2, diameter, barHeight);

                g2d.setColor(Color.GREEN);
                g2d.fillRect(pos.getX(), pos.getY() - barHeight - 2, healthWidth, barHeight);

                g2d.setColor(Color.BLACK);
                g2d.drawRect(pos.getX(), pos.getY() - barHeight - 2, diameter, barHeight);
            }
        }
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