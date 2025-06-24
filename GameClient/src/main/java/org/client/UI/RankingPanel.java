package org.client.UI;

import javax.swing.*;
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
        setPreferredSize(new Dimension(200, 300));

        tableModel = new DefaultTableModel(new Object[]{"Rank", "Player", "Kills"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable rankingTable = new JTable(tableModel);
        rankingTable.setOpaque(false);
        rankingTable.setFont(new Font("Arial", Font.PLAIN, 12));
        rankingTable.setRowHeight(20);
        rankingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Style the table
        rankingTable.setForeground(Color.WHITE);
        rankingTable.setBackground(new Color(0, 0, 0, 150));
        rankingTable.getTableHeader().setBackground(new Color(0, 0, 0, 200));
        rankingTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

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
