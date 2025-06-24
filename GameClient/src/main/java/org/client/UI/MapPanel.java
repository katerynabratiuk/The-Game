package org.client.UI;

import lombok.Getter;
import lombok.Setter;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.game.Coordinates;
import org.lib.data_structures.payloads.game.GameState;
import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.client.game_logic.PlayerNameMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MapPanel extends JPanel {
    @Getter private GameState gameState;
    private final RankingPanel rankingPanel;
    private final InventoryPanel inventoryPanel;
    private final int SCALE = 5;

    @Setter
    private InputCallback inputCallback;
    private KeyListener keyListener;
    private MouseListener mouseListener;

    public MapPanel() {
        this.gameState = new GameState(new ArrayList<>(), new ConcurrentHashMap<>());
        this.rankingPanel = new RankingPanel();
        this.inventoryPanel = new InventoryPanel();
        setupInputListeners();
    }

    private void setupInputListeners() {
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inputCallback != null) {
                    inputCallback.onKeyPressed(e.getKeyCode());
                }
            }
        };
        
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (inputCallback != null) {
                    Coordinates gameCoords = convertToGameCoordinates(e.getX(), e.getY());
                    inputCallback.onMouseClicked(gameCoords.getX(), gameCoords.getY());
                }
            }
        };
    }
    
    public void onKill() {
        removeKeyListener(keyListener);
        removeMouseListener(mouseListener);
        setFocusable(false);
    }
    
    public void enableInput() {
        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
        PlayerNameMapper.updatePlayerNameMappings(gameState);
        repaint();
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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawActors(g2d);
        updateRankingPanel();
        updateInventoryPanel(); // currently is updated each frame
    }

    private void drawActors(Graphics2D g2d) {
        for (Actor actor : gameState.getActorsSnapshot()) {
            drawActor(g2d, actor);
        }
    }

    private void drawActor(Graphics2D g2d, Actor actor) {
        Coordinates pos = convertToMapCoordinates(actor.getCoordinates().getX(), actor.getCoordinates().getY(), (int)actor.getRadius());

        g2d.setColor(actor.color());
        g2d.fillOval(pos.getX(), pos.getY(), (int) actor.getRadius()*2, (int)actor.getRadius()*2);
        g2d.setColor(Color.BLACK);

        if (actor instanceof PlayerCharacter pc) {
            drawPlayerCharacter(g2d, pc, pos);
        }
    }

    private void drawPlayerCharacter(Graphics2D g2d, PlayerCharacter player, Coordinates pos) {
        int diameter = (int) player.getRadius() * 2;
        drawHealthBar(g2d, player, pos, diameter);
        drawUsername(g2d, player, pos, diameter);
    }

    private void drawHealthBar(Graphics2D g2d, PlayerCharacter player, Coordinates pos, int diameter) {
        int maxHP = player.getMaxHp();
        int currentHP = player.getHitPoints();
        int barHeight = 4;
        int healthWidth = (int) (((double) currentHP / maxHP) * diameter);

        // background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(pos.getX(), pos.getY() - barHeight - 2, diameter, barHeight);

        // fill
        g2d.setColor(Color.GREEN);
        g2d.fillRect(pos.getX(), pos.getY() - barHeight - 2, healthWidth, barHeight);

        // border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(pos.getX(), pos.getY() - barHeight - 2, diameter, barHeight);
    }

    private void drawUsername(Graphics2D g2d, PlayerCharacter player, Coordinates pos, int diameter) {
        String username = PlayerNameMapper.getUsername(player.getClientUUID());
        if (username == null) return;

        // font
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(username);
        int textX = pos.getX() + (diameter - textWidth) / 2;
        int textY = pos.getY() - 12;

        // shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(username, textX + 1, textY + 1);
        
        // text
        g2d.setColor(Color.WHITE);
        g2d.drawString(username, textX, textY);
    }

    private void updateRankingPanel() {
        rankingPanel.updateRankings(gameState.getPlayerKills());
        rankingPanel.setBounds(
                getWidth() - rankingPanel.getPreferredSize().width - 10,
                10,
                rankingPanel.getPreferredSize().width,
                rankingPanel.getPreferredSize().height
        );
        add(rankingPanel);
        rankingPanel.repaint();
    }
    
    private void updateInventoryPanel() {
        inventoryPanel.setBounds(
                10,
                getHeight() - inventoryPanel.getPreferredSize().height - 10,
                inventoryPanel.getPreferredSize().width,
                inventoryPanel.getPreferredSize().height
        );
        add(inventoryPanel);
        inventoryPanel.repaint();
    }

}