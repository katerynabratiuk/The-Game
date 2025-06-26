package org.client.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class RankingPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final Map<String, Integer> playerKills = new HashMap<>();

    public RankingPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(250, 220));
        setLayout(new BorderLayout(0, 5));

        tableModel = new DefaultTableModel(new Object[]{"#", "Player", "Kills"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable rankingTable = new JTable(tableModel);
        rankingTable.setOpaque(false);
        rankingTable.setRowHeight(24);
        rankingTable.setFont(new Font("Arial", Font.PLAIN, 12));
        rankingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        rankingTable.getTableHeader().setReorderingAllowed(false);
        rankingTable.setShowGrid(false);
        rankingTable.setFocusable(false);
        rankingTable.setRowSelectionAllowed(false);
        rankingTable.setBackground(new Color(0, 0, 0, 0));
        rankingTable.setForeground(Color.WHITE);
        rankingTable.getTableHeader().setOpaque(false);
        rankingTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
        rankingTable.getTableHeader().setForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        rankingTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        rankingTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JLabel title = new JLabel("Playerboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(5, 0, 0, 0));

        RoundedPanel background = new RoundedPanel();
        background.setLayout(new BorderLayout());
        background.setOpaque(false);
        background.setBorder(new EmptyBorder(10, 10, 10, 10));
        background.add(title, BorderLayout.NORTH);
        background.add(scrollPane, BorderLayout.CENTER);

        add(background, BorderLayout.CENTER);
    }

    public void updateRankings(Map<String, Integer> newRankings) {
        playerKills.clear();
        playerKills.putAll(newRankings);
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        playerKills.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(entry -> {
                    tableModel.addRow(new Object[]{
                            tableModel.getRowCount() + 1,
                            entry.getKey(),
                            entry.getValue()
                    });
                });
    }

    private static class RoundedPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(50, 50, 50, 150));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
    }
}
