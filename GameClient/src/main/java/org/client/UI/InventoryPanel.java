package org.client.UI;

import org.lib.data.dto.ItemDTO;
import org.lib.data.payloads.actors.Inventory;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private static final int MAX_SLOTS = 6;
    private static final int SLOT_SIZE = 60;
    private static final int SLOT_PADDING = 5;
    private static final String[] DEFAULT_KEY_BINDINGS = {"1", "2", "3", "4", "5", "6"};

    private final InventorySlot[] slots;

    public InventoryPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, SLOT_PADDING, SLOT_PADDING));
        setOpaque(false);
        setPreferredSize(new Dimension(
                MAX_SLOTS * SLOT_SIZE + (MAX_SLOTS + 1) * SLOT_PADDING,
                SLOT_SIZE + 2 * SLOT_PADDING
        ));
        slots = new InventorySlot[MAX_SLOTS];
        initializeSlots();
    }

    private void initializeSlots() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            slots[i] = new InventorySlot();
            add(slots[i]);
            setSlotItem(i, null, DEFAULT_KEY_BINDINGS[i], null);
        }
    }

    public void updateInventory(Inventory inventory) {
        if (inventory == null || inventory.getItems() == null) {
            return;
        }

        var items = inventory.getItems();
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (i < items.size()) {
                ItemDTO item = items.get(i);
                ImageIcon icon = loadItemImage(item.getImagePath());
                setSlotItem(i, icon, DEFAULT_KEY_BINDINGS[i], item.getName());
            } else {
                setSlotItem(i, null, DEFAULT_KEY_BINDINGS[i], null);
            }
        }
    }

    private ImageIcon loadItemImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }

        try {
            var imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                var icon = new ImageIcon(imageURL);
                var scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Failed to load inventory image: " + e.getMessage());
        }
        return null;
    }

    private void setSlotItem(int slotIndex, ImageIcon image, String keyBinding, String itemName) {
        if (slotIndex >= 0 && slotIndex < MAX_SLOTS) {
            slots[slotIndex].setItem(image, keyBinding, itemName);
        }
    }

    private static class InventorySlot extends JPanel {
        private ImageIcon itemImage;
        private String keyBinding;
        private String itemName;

        public InventorySlot() {
            setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
            setOpaque(false);
        }

        public void setItem(ImageIcon image, String keyBinding, String itemName) {
            this.itemImage = image;
            this.keyBinding = keyBinding;
            this.itemName = itemName;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(new Color(50, 50, 50, 150));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);

            if (itemImage != null) {
                int imageX = (getWidth() - itemImage.getIconWidth()) / 2;
                int imageY = (getHeight() - itemImage.getIconHeight()) / 2 - 10;
                g2d.drawImage(itemImage.getImage(), imageX, imageY, null);

                if (itemName != null) {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(itemName);
                    int textX = (getWidth() - textWidth) / 2;
                    int textY = getHeight() - 15;

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(itemName, textX + 1, textY + 1);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(itemName, textX, textY);
                }
            }

            if (keyBinding != null) {
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(keyBinding)) / 2;
                int textY = getHeight() - 5;
                g2d.setColor(Color.BLACK);
                g2d.drawString(keyBinding, textX + 1, textY + 1);
                g2d.setColor(Color.YELLOW);
                g2d.drawString(keyBinding, textX, textY);
            }
        }
    }
}