package org.client.UI;

import lombok.Getter;
import lombok.Setter;
import org.lib.data.payloads.actors.Actor;
import org.lib.data.payloads.actors.Coordinates;
import org.lib.data.payloads.game.GameState;
import org.lib.data.payloads.actors.PlayerCharacter;

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
    @Getter private final RankingPanel rankingPanel;
    @Getter private final InventoryPanel inventoryPanel;
    private final int SCALE = 1;
    private Image backgroundImage;
    private JButton reenterMapButton;

    @Setter
    private InputCallback inputCallback;
    private KeyListener keyListener;
    private MouseListener mouseListener;

    public MapPanel() {
        this.gameState = new GameState(new ArrayList<>(), new ConcurrentHashMap<>());
        this.rankingPanel = new RankingPanel();
        this.inventoryPanel = new InventoryPanel();
        setupInputListeners();
        loadBackgroundImage();
        setupReenterMapButton();
    }

    private void setupInputListeners() {
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inputCallback != null) {
                    inputCallback.onKeyPressed(e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (inputCallback != null) {
                    inputCallback.onKeyReleased(e.getKeyCode());
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
        if (reenterMapButton != null) {
            reenterMapButton.setVisible(true);
            reenterMapButton.requestFocusInWindow();
        }
    }
    
    public void enableInput() {
        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
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
        setBackgroundImage(g2d);
        drawActors(g2d);
        updateRankingPanel();
        updateInventoryPanel();
    }

    private void setBackgroundImage(Graphics2D g2d) {
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void drawActors(Graphics2D g2d) {
        for (Actor actor : gameState.getActors()) {
            drawActor(g2d, actor);
        }
    }

    private void drawActor(Graphics2D g2d, Actor actor) {
        Coordinates pos = convertToMapCoordinates(actor.getCoordinates().getX(), actor.getCoordinates().getY(), (int)actor.getRadius());
        int diameter = (int) actor.getRadius() * 2;
        if (actor instanceof PlayerCharacter pc) {
            drawPlayerCharacter(g2d, pc, pos, diameter);
        } else {
            g2d.setColor(actor.color());
            g2d.fillOval(pos.getX(), pos.getY(), diameter, diameter);
        }
    }

    private void drawPlayerCharacter(Graphics2D g2d, PlayerCharacter player, Coordinates pos, int diameter) {
        drawPlayerImage(g2d, player, pos, diameter);
        drawHealthBar(g2d, player, pos, diameter);
        drawUsername(g2d, player, pos, diameter);
        drawCooldownIndicator(g2d, player, pos, diameter);
    }

    private void drawPlayerImage(Graphics2D g2d, PlayerCharacter player, Coordinates pos, int diameter) {
        if (player.getImagePath() != null && !player.getImagePath().isEmpty()) {
            try {
                String imagePath = player.getImagePath();
                var imageURL = getClass().getResource(imagePath);
                if (imageURL != null) {
                    var icon = new ImageIcon(imageURL);
                    var scaledImage = icon.getImage().getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
                    var scaledIcon = new ImageIcon(scaledImage);
                    g2d.drawImage(scaledIcon.getImage(), pos.getX(), pos.getY(), this);
                }
            } catch (Exception e) {
                g2d.setColor(player.color());
                g2d.fillOval(pos.getX(), pos.getY(), diameter, diameter);
            }
        } else {
            g2d.setColor(player.color());
            g2d.fillOval(pos.getX(), pos.getY(), diameter, diameter);
        }
    }

    private void drawCooldownIndicator(Graphics2D g2d, PlayerCharacter player, Coordinates pos, int diameter) {
        if (player.getRateOfFire() > 0) {
            if (player.determineAttackReady()) return;

            double cooldownRatio = player.calcCooldownRatio();

            int innerDiameter = (int)(diameter * cooldownRatio);
            int innerX = pos.getX() + (diameter - innerDiameter)/2;
            int innerY = pos.getY() + (diameter - innerDiameter)/2;

            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillOval(innerX, innerY, innerDiameter, innerDiameter);

            g2d.setColor(new Color(255, 255, 0, 150));
            g2d.fillArc(innerX, innerY, innerDiameter, innerDiameter,
                    90, (int)(360 * cooldownRatio));
        }
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
        String username = player.getUsername();
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

    private void loadBackgroundImage() {
        try {
            var imageUrl = getClass().getResource("/images/moon_surface.jpg");
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
    }

    private void setupReenterMapButton() {
        reenterMapButton = new JButton("Reenter Map");
        reenterMapButton.setVisible(false);
        reenterMapButton.setFocusable(false);
        reenterMapButton.addActionListener(e -> {
            reenterMapButton.setVisible(false);
            org.client.Startup.getPacketsSenderService().sendCharacterListRequest();
        });
        setLayout(null); 
        add(reenterMapButton);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (reenterMapButton != null) {
            int btnWidth = 180, btnHeight = 40;
            reenterMapButton.setBounds((width - btnWidth) / 2, (height - btnHeight) / 2, btnWidth, btnHeight);
        }
    }

}