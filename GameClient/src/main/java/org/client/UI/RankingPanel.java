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
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(250, 350));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new Object[]{"#", "Player", "Kills"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable rankingTable = new JTable(tableModel);
        rankingTable.setRowHeight(28);
        rankingTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rankingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));


        rankingTable.setBackground(new Color(248, 249, 250));
        rankingTable.setForeground(Color.DARK_GRAY);
        rankingTable.setGridColor(new Color(222, 226, 230));

        rankingTable.setSelectionBackground(new Color(204, 229, 255));
        rankingTable.setSelectionForeground(Color.BLACK);

        rankingTable.getTableHeader().setBackground(new Color(52, 58, 64)); // dark header
        rankingTable.getTableHeader().setForeground(Color.WHITE);
        rankingTable.getTableHeader().setOpaque(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        rankingTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        rankingTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JLabel title = new JLabel("Playerboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(33, 37, 41));
        title.setBorder(new EmptyBorder(5, 5, 10, 5));

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
}
