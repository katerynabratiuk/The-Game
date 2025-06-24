package org.client.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class InventoryPanel extends JPanel {
    private static final int MAX_SLOTS = 3;
    private static final int SLOT_SIZE = 60;
    private static final int SLOT_PADDING = 5;

    private final InventorySlot[] slots;

    public InventoryPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, SLOT_PADDING, SLOT_PADDING));
        setOpaque(false);
        setPreferredSize(new Dimension(MAX_SLOTS * SLOT_SIZE + (MAX_SLOTS + 1) * SLOT_PADDING, SLOT_SIZE + 2 * SLOT_PADDING));

        slots = new InventorySlot[MAX_SLOTS];
        initializeSlots();
    }

    private void initializeSlots() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            slots[i] = new InventorySlot();
            add(slots[i]);
        }

        // mock bindings
        setSlotItem(0, createMockImage(Color.RED), "F1");
        setSlotItem(1, createMockImage(Color.GREEN), "F2");
        setSlotItem(2, createMockImage(Color.BLUE), "F3");
    }

    public void setSlotItem(int slotIndex, BufferedImage image, String keyBinding) {
        if (slotIndex >= 0 && slotIndex < MAX_SLOTS) {
            slots[slotIndex].setItem(image, keyBinding);
        }
    }

    private BufferedImage createMockImage(Color color) {
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 40, 40);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, 39, 39);
        g2d.dispose();
        return image;
    }

    private static class InventorySlot extends JPanel {
        private BufferedImage itemImage;
        private String keyBinding;

        public InventorySlot() {
            setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        }

        public void setItem(BufferedImage image, String keyBinding) {
            this.itemImage = image;
            this.keyBinding = keyBinding;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // image
            if (itemImage != null) {
                int imageX = (getWidth() - itemImage.getWidth()) / 2;
                int imageY = (getHeight() - itemImage.getHeight()) / 2 - 10;
                g2d.drawImage(itemImage, imageX, imageY, null);
            }

            //  keybinding text
            if (keyBinding != null) {
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                g2d.setColor(Color.WHITE);

                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(keyBinding)) / 2;
                int textY = getHeight() - 5;

                g2d.setColor(Color.BLACK);
                g2d.drawString(keyBinding, textX + 1, textY + 1);

                g2d.setColor(Color.WHITE);
                g2d.drawString(keyBinding, textX, textY);
            }
        }
    }
}